package issuetracker.be.controller;

import issuetracker.be.domain.Milestone;
import issuetracker.be.dto.MilestoneSaveRequest;
import issuetracker.be.dto.MilestoneUpdateRequest;
import issuetracker.be.dto.MilestoneWithIssueCountResponse;
import issuetracker.be.service.MilestoneService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class MilestoneController {

  private final MilestoneService milestoneService;

  @Autowired
  public MilestoneController(MilestoneService milestoneService) {
    this.milestoneService = milestoneService;
  }

  @GetMapping("/milestone")
  public List<MilestoneWithIssueCountResponse> getAllMilestone() {
    return milestoneService.getAllMilestone();
  }

  @PostMapping("/milestone")
  public void save(@RequestBody MilestoneSaveRequest milestoneSaveRequest) {
    milestoneService.save(milestoneSaveRequest);
  }

  @PutMapping("/milestone/{milestoneId}")
  public void update(@PathVariable Long milestoneId, @RequestBody MilestoneUpdateRequest milestoneUpdateRequest) {
    Milestone result = milestoneService.update(milestoneUpdateRequest.withId(milestoneId));
    log.debug("수정한 마일스톤 {}", result);
  }

  @DeleteMapping("/milestone/{milestoneId}")
  public void delete(@PathVariable Long milestoneId) {
    milestoneService.delete(milestoneId);
  }

  @PatchMapping("/milestone/open/{id}")
  public void openIssues(@PathVariable Long id) {
    milestoneService.changeMilestoneStatus(id, true);
  }

  @PatchMapping("/milestone/close/{id}")
  public void closeIssue(@RequestBody @PathVariable Long id) {
    milestoneService.changeMilestoneStatus(id, false);
  }
}
