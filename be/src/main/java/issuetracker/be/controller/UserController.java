package issuetracker.be.controller;

import issuetracker.be.domain.User;
import issuetracker.be.dto.IssueAssigneeUpdateRequest;
import issuetracker.be.dto.UserResponse;
import issuetracker.be.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/user")
  public List<UserResponse> showAssignees() {
    return userService.getAllUsers();
  }

  @PatchMapping("/issue/assignee")
  public void updateAssignee(@RequestBody IssueAssigneeUpdateRequest issueAssigneeUpdateRequest) {
    userService.updateAssignee(issueAssigneeUpdateRequest);
  }
}