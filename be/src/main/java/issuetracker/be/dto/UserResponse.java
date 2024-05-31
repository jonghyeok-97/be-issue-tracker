package issuetracker.be.dto;

import issuetracker.be.domain.User;

public record UserResponse(
    String name,
    String image_path
) {

  public static UserResponse toDto(User user) {
    String name = user.getId();
    String imagePath = user.getImage_path();
    return new UserResponse(name, imagePath);
  }
}
