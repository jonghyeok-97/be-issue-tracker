package issuetracker.be.config.oauth;

import issuetracker.be.config.oauth.dto.GithubUserProfileDto;
import issuetracker.be.domain.User;
import issuetracker.be.dto.UserResponse;
import issuetracker.be.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {

  private final UserRepository userRepository;

  @Autowired
  public OAuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // 깃헙으로 로그인한 사용자가 회원가입 하지 않았으면 자동으로 회원가입되도록 설정
  @Transactional
  public UserResponse save(GithubUserProfileDto dto) {
    User githubUser = dto.toEntity();
    Optional<User> existUser = userRepository.findByNameEquals(githubUser.getId());

    if (existUser.isEmpty()) {
      userRepository.save(githubUser);
    }

    return new UserResponse(githubUser.getId(), githubUser.getImage_path());
  }
}
