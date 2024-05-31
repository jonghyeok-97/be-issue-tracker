package issuetracker.be.service;

import issuetracker.be.domain.Issue;
import issuetracker.be.domain.IssueFilters;
import issuetracker.be.domain.Label;
import issuetracker.be.domain.User;
import issuetracker.be.domain.issueFilter.IssueFilterFactory;
import issuetracker.be.dto.CommentResponse;
import issuetracker.be.dto.IssueDetailResponse;
import issuetracker.be.dto.IssueFilterRequest;
import issuetracker.be.dto.IssueListResponse;
import issuetracker.be.dto.IssueMilestoneUpdateRequest;
import issuetracker.be.dto.IssueSaveRequest;
import issuetracker.be.dto.IssueShowResponse;
import issuetracker.be.dto.IssueTitleUpdateRequest;
import issuetracker.be.dto.MilestoneWithIssueCountResponse;
import issuetracker.be.dto.UserResponse;
import issuetracker.be.repository.CommentRepository;
import issuetracker.be.dto.OpenStatusChangeRequest;
import issuetracker.be.dto.UserResponse;
import issuetracker.be.repository.IssueRepository;
import issuetracker.be.repository.MilestoneRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class IssueService {

  private IssueRepository issueRepository;
  private CommentService commentService;
  private UserService userService;
  private LabelService labelService;
  private MilestoneRepository milestoneRepository;

  @Autowired
  public IssueService(IssueRepository issueRepository, CommentService commentService,
      UserService userService, LabelService labelService, MilestoneRepository milestoneRepository) {
    this.issueRepository = issueRepository;
    this.commentService = commentService;
    this.userService = userService;
    this.labelService = labelService;
    this.milestoneRepository = milestoneRepository;
  }

  @Transactional
  public Long save(IssueSaveRequest issueSaveRequest) {
    Issue issue = issueSaveRequest.toEntity(LocalDateTime.now());
    Issue saveIssue = issueRepository.save(issue);
    log.debug("저장된 이슈 : {}", saveIssue);

    if (issueSaveRequest.comment() != null) {
      commentService.saveComment(saveIssue.getId(), saveIssue.getReporter(),
          saveIssue.getCreated_at(), issueSaveRequest.comment());
    }
    return issue.getId();
  }

  @Transactional
  public void updateTitle(IssueTitleUpdateRequest issueTitleUpdateRequest) {
    String title = issueTitleUpdateRequest.title();
    Long id = issueTitleUpdateRequest.id();
    issueRepository.updateTitle(title, id);
  }

  @Transactional
  public void updateMilestoneId(IssueMilestoneUpdateRequest issueMilestoneUpdateRequest){
    Long id = issueMilestoneUpdateRequest.issue_id();
    Long milestoneId = issueMilestoneUpdateRequest.milestone_id();
    if(milestoneId != null) {
      issueRepository.updateMilestoneId(milestoneId, id);
    } else {
      issueRepository.deleteMilestoneId(id);
    }
  }

  @Transactional
  public void deleteIssue(Long id) {
    labelService.deleteLabelRef(id);
    userService.deleteAssigneeRef(id);
    commentService.deleteIssue(id);
    issueRepository.deleteIssue(id);
  }

  public IssueListResponse getAllIssue() {
    List<IssueShowResponse> closeIssues = generateIssueShowDto(issueRepository.findByIsOpen(false));
    List<IssueShowResponse> openIssues = generateIssueShowDto(issueRepository.findByIsOpen(true));

    return new IssueListResponse(closeIssues, openIssues);
  }

  public IssueDetailResponse getDetailResponse(Long issueId) {
    List<CommentResponse> commentResponse = commentService.getCommentResponse(issueId);

    Issue issue = getIssue(issueId);
    List<Label> label = getLabels(issue);

    List<UserResponse> assignees = getAssignees(issue);


    MilestoneWithIssueCountResponse milestone = getMilestoneWithIssueCountResponse(issue);

    UserResponse reporter = userService.getUser(issue.getReporter());

    return new IssueDetailResponse(issue, assignees, label, milestone, reporter, commentResponse);
  }

  public boolean isIssueExistBy(Long milestoneId) {
    return issueRepository.existsByMilestoneId(milestoneId);
  }

  private List<IssueShowResponse> generateIssueShowDto(List<Issue> issues) {
    List<IssueShowResponse> result = new ArrayList<>();
    for (Issue issue : issues) {
      List<Label> label = getLabels(issue);

      MilestoneWithIssueCountResponse milestone = getMilestoneWithIssueCountResponse(issue);

      UserResponse reporter = userService.getUser(issue.getReporter());

      IssueShowResponse issueShowResponse = new IssueShowResponse(issue, label, milestone, reporter);
      result.add(issueShowResponse);
    }
    return result;
  }

  public IssueListResponse getFilteredIssue(IssueFilterRequest filterRequest) {

    List<Issue> closeIssues = issueRepository.findByIsOpen(false);
    List<Issue> openIssues = issueRepository.findByIsOpen(true);

    IssueFilters issueFilters = new IssueFilterFactory().createIssueFilters(
        filterRequest.assignee(),
        filterRequest.label(),
        filterRequest.milestone(),
        filterRequest.reporter(),
        commentService.getComments(filterRequest.commentReporter())
    );

    List<Issue> filteredCloseIssues = issueFilters.doFilter(closeIssues);
    List<Issue> filteredOpenIssues = issueFilters.doFilter(openIssues);

    List<IssueShowResponse> filteredCloseIssueResponses = generateIssueShowDto(filteredCloseIssues);
    List<IssueShowResponse> filteredOpenIssueResponses = generateIssueShowDto(filteredOpenIssues);

    return new IssueListResponse(filteredCloseIssueResponses, filteredOpenIssueResponses);
  }

  /**
   * 이슈의 열림/닫힘 상태를 변경합니다.
   *
   * @param openStatusChangeRequest 상태 수정 대상 이슈 ID가 담긴 DTO
   * @param status                  바꾸려는 상태
   * @throws NoSuchElementException 해당하는 이슈가 없는 경우 예외가 발생한다.
   */
  @Transactional
  public void changeIssueStatus(OpenStatusChangeRequest openStatusChangeRequest, boolean status) {
    openStatusChangeRequest.id().stream()
        .map(this::getIssue)
        .forEach(i -> {
          i.setIs_open(status);
          issueRepository.save(i);
        });
  }

  private Issue getIssue(Long issueId) {
    return issueRepository.findById(issueId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이슈입니다."));
  }

  private MilestoneWithIssueCountResponse getMilestoneWithIssueCountResponse(Issue issue) {
    return (issue.getMilestone_id() != null) ? milestoneRepository.findWithIssueCountBy(
            issue.getMilestone_id())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 마일스톤입니다."))
        : null;
  }

  private List<Label> getLabels(Issue issue) {
    return issue.getLabels().isEmpty() ?
        null : issue.getLabels().stream()
        .map(labelRef -> labelService.findById(labelRef.getLabel_id()))
        .collect(Collectors.toList());
  }

  private List<UserResponse> getAssignees(Issue issue) {
    return issue.getAssignees().isEmpty() ?
        null : userService.findByIssueId(issue.getId());
  }
}