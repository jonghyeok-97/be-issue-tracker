package issuetracker.be.config.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import issuetracker.be.domain.User;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) //정의되지 않은 필드는 무시
public class GithubUserProfileDto {

  private String login;
  @JsonProperty("avatar_url")
  private String avatarUrl;

  public User toEntity() {
    User user = new User();
    user.setName(login);
    user.setImage_path(avatarUrl);
    return user;
  }
}
