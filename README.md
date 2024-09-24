# issue-tracker
코드스쿼드 2024 마스터즈 그룹 프로젝트 Github Issue Tracker 입니다.
해당 [Repo](https://github.com/codesquad-members-2024/issue-tracker)에서 Team06, 그로밋으로 참여하였습니다.

## Spring Security 없이 OAuth 활용한 로그인 구현
- [원본 Github 링크](https://github.com/jonghyeok-97/be-issue-tracker/blob/be-dev-gromit/be/src/main/java/issuetracker/be/config/oauth/OAuthController.java)
1. 사용자가 깃헙 로그인 요청 시 Resource Server(Github) 의 로그인 페이지를 보여준다.
2. 사용자가 로그인을 하면, Github Oauth 를 사용하기 위해 등록해 두었던 Redirect URL 로 GET요청을 보내며 쿼리파라미터에 로그인을 했다는 의미인 특정 문자열을 담아 보낸다.
3. 해당 Redirect URL 요청을 Controller 에서 처리한다.
4. Github 에 서비스를 등록하며 얻은 client-id 와 client-secret 을 Environment 로 가져온다.
5. Github 에게 쿼리파라미터로 온 문자열과 client-id 와 client-secret 을 body에 담아 RestTemplate 으로 POST요청을 보낸다.
6. 요청이 성공적이면 Github 은 응답으로 Access Token 을 주고, 내 서비스는 Github 에게 해당 사용자의 정보를 얻을 때마다 Access Token 을 이용하게 된다.
7. 이 때, Access Token 을 절때로 사용자에게 건네주면 안된다.
8. 사용자 로그인 상태를 기억하기 위해 새로운 JWT 를 만들어서 헤더/바디에 담아서 건네준다.
```
@Slf4j
@RestController
public class OAuthController {

  private final Environment environment;
  private final OAuthService oAuthService;
  private final JwtUtil jwtUtil;

  @Autowired
  public OAuthController(Environment environment, OAuthService oAuthService, JwtUtil jwtUtil) {
    this.environment = environment;
    this.oAuthService = oAuthService;
    this.jwtUtil = jwtUtil;
  }
 
  @PostMapping("/login/github")   /* 사용자가 Github에서 로그인이 성공했다는 의미의 문자열(code)을 쿼리 파라미터로 받는다 */
  public UserInfoWithTokenResponse getGithubUserProfile(@RequestParam String code) throws JsonProcessingException {
    OAuthToken oAuthToken = getOAuthToken(code);                                 /* 문자열(code), client-id, client-secret 으로 Github 에게 해당 사용자의 권한을 위임받기 위해 POST요청을 보낸다 */
    GithubUserProfileDto githupProfileDto = getGithubUserProfile(oAuthToken);    /* 권한을 위임받은 의미인 AccessToken 으로 Github에게 사용자의 정보를 받기위해 GET요청을 보낸다 */
    UserResponse userResponse = oAuthService.save(githupProfileDto);             /* 해당 사용자가 회원가입이 되어있지 않으면 회원가입을 시킨다 */
    return new UserInfoWithTokenResponse(jwtUtil.createToken(userResponse.name()), userResponse);  /* 해당 사용자의 상태를 기억하기 위해 AccessToken 이 아닌 새로운 jwt 를 발급해준다 */
  }

  private OAuthToken getOAuthToken(String code) throws JsonProcessingException {
    RestTemplate tokenRequestTemplate = new RestTemplate();
    ResponseEntity<String> response = tokenRequestTemplate.exchange(
        "https://github.com/login/oauth/access_token",
        HttpMethod.POST,
        getCodeRequestHttpEntity(code),
        String.class
    );
    log.debug("리스폰스 바디 : {}", response.getBody());
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(response.getBody(), OAuthToken.class);
  }

  private HttpEntity<MultiValueMap<String, String>> getCodeRequestHttpEntity(String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("client_id", environment.getProperty("client-id"));
    params.add("client_secret", environment.getProperty("client-secret"));
    params.add("code", code);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    return new HttpEntity<>(params, headers);
  }

  private GithubUserProfileDto getGithubUserProfile(OAuthToken oAuthToken) throws JsonProcessingException{
    RestTemplate profileRequestTemplate = new RestTemplate();
    ResponseEntity<String> profileResponse = profileRequestTemplate.exchange(
        "https://api.github.com/user",
        HttpMethod.GET,
        getProfileRequestEntity(oAuthToken),
        String.class
    );
    log.debug("프로필 정보 : {}", profileResponse.getBody());
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(profileResponse.getBody(), GithubUserProfileDto.class);
  }

  private HttpEntity<MultiValueMap<String, String>> getProfileRequestEntity(OAuthToken oAuthToken) {
    HttpHeaders profileRequestHeaders = new HttpHeaders();
    profileRequestHeaders.add("Authorization", "token " + oAuthToken.getAccessToken());
    return new HttpEntity<>(profileRequestHeaders);
  }
}
```
## OOP, 전략패턴, 함수형 프로그래밍 적극 활용하여 동적으로 필터 기능 구현
- 요청 URL : `/issue/filter/?assignee=값&label=값&milestone=값&reporter=값&comment=값`
- 해당 요청이 들어올 때, 이슈 담당자, 라벨, 마일스톤, 댓글, 글쓴이에 따라 해당 이슈를 보여주는 기능입니다.
- Spring 을 학습하고 있었을 뿐더러 프로젝트 기간 상 동적 쿼리를 지원하는 QueryDsl, MyBatis 를 추가로 학습하지 않고, Vanilla Java 로 구현하게 되었습니다.

- be/src/main/java/issuetracker/be/domain/issueFilter의 issueFilterFactory 설명
  - 쿼리 파라미터로 들어온 값이 null 인지 확인하고, 값이 null 이 아닌 것만 Runtime 시점에 List에 추가하였습니다.
  - 함수형 프로그래밍과, 전략 패턴을 사용하여 Runtime 시점에 동적 필터를 선택하게 하였습니다. 
```
public class IssueFilterFactory {

  public IssueFilters createIssueFilters(String assignee, String label, String milestone,
      String reporter, List<Comment> comments) {
    List<IssueFilter> issueFilters = new ArrayList<>();
    addFilter(assignee, IssueAssigneeFilter::new, issueFilters);
    addFilter(label, IssueLabelFilter::new, issueFilters);
    addFilter(milestone, IssueMilestoneFilter::new, issueFilters);
    addFilter(reporter, IssueReporterFilter::new, issueFilters);
    addFilter(comments, IssueCommentFilter::new, issueFilters);
    return new IssueFilters(issueFilters);
  }

  private void addFilter(String value, Function<String, IssueFilter> valueToIssueFilter,
      List<IssueFilter> issueFilters) {
    Optional.ofNullable(value)
        .map(valueToIssueFilter)
        .ifPresent(issueFilters::add);
  }

  private void addFilter(List<Comment> comments, Function<List<Comment>, IssueFilter> commentToIssueFilter,
      List<IssueFilter> issueFilters) {
    if (!comments.isEmpty()) {
      issueFilters.add(commentToIssueFilter.apply(comments));
    }
  }
}
```

그 외 객체 설명
- IssueFilterFactory
  - URL에 있는 요청한 쿼리 파라미터에 값이 들어왔는지(null 이 아닌지) 확인
  - null 이 아닌것에 대해 IssueFilter 생성
- IssueFilter
  - IssueAssigneeFilter , IssueLabelFilter , IssueMilestoneFilter , IssueAssigneeFilter, IssueCommentFilter 의 인터페이스
- IssueFilterFactory
  - Service 에서 IssueFilter 를 생성하고 사용하는 것을 분리하고자 오브젝트의 Factory 패턴을 참고하여 구현
  - IssueFilter 들의 구현체를 생성
- IssueFilters
  - IssueFilter 의 구현체들이 List 로 담겨있고, IssueFilter 들을 관리하는 역할.
