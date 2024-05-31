package issuetracker.be.controller;

import issuetracker.be.dto.IssueDetailResponse;
import issuetracker.be.dto.IssueFilterRequest;
import issuetracker.be.dto.IssueListResponse;
import issuetracker.be.dto.IssueMilestoneUpdateRequest;
import issuetracker.be.dto.IssueSaveRequest;
import issuetracker.be.dto.IssueTitleUpdateRequest;
import issuetracker.be.dto.OpenStatusChangeRequest;
import issuetracker.be.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IssueController {

  private final IssueService issueService;

  @Autowired
  public IssueController(IssueService issueService) {
    this.issueService = issueService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/issue")
  public Long saveIssue(@RequestBody IssueSaveRequest issueSaveRequest) {
    return issueService.save(issueSaveRequest);
  }

  @GetMapping("/issue")
  public IssueListResponse findAllIssues() {
    return issueService.getAllIssue();
  }

  /**
   * @param filterRequest : 부가적인 이슈 필터링(담당자, 라벨, 마일스톤, 작성자)
   * @return 필터링된 열려있거나 닫혀있는 모든 이슈
   */
  @GetMapping("/issue/filter")
  public IssueListResponse getAllFilteredIssues(@ModelAttribute IssueFilterRequest filterRequest) {
    log.debug("필터링 요청 정보 : {}", filterRequest);
    return issueService.getFilteredIssue(filterRequest);
  }

  @PatchMapping("/issue/open")
  public void openIssues(@RequestBody OpenStatusChangeRequest openStatusChangeRequest) {
    issueService.changeIssueStatus(openStatusChangeRequest, true);
  }

  @PatchMapping("/issue/close")
  public void closeIssue(@RequestBody OpenStatusChangeRequest openStatusChangeRequest) {
    issueService.changeIssueStatus(openStatusChangeRequest, false);
  }

  @GetMapping("/issue/{issueId}")
  public IssueDetailResponse issueDetail(@PathVariable Long issueId) {
    return issueService.getDetailResponse(issueId);
  }

  @PatchMapping("/issue/title")
  public void issueTitleUpdate(@RequestBody IssueTitleUpdateRequest issueTitleUpdateRequest) {
    issueService.updateTitle(issueTitleUpdateRequest);
  }

  @DeleteMapping("/issue/{issueId}")
  public void deleteIssue(@PathVariable Long issueId) {
    issueService.deleteIssue(issueId);
  }

  @PatchMapping("/issue/milestone")
  public void issueMilestoneUpdate(@RequestBody IssueMilestoneUpdateRequest issueMilestoneUpdateRequest) {
    issueService.updateMilestoneId(issueMilestoneUpdateRequest);
  }
}