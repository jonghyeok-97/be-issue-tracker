package issuetracker.be.config.oauth.dto;

import issuetracker.be.dto.UserResponse;


public record UserInfoWithTokenResponse(
    String token,
    UserResponse user_info
) {


}
