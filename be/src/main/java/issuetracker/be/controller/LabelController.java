package issuetracker.be.controller;

import issuetracker.be.domain.Label;
import issuetracker.be.dto.IssueLabelUpdateRequest;
import issuetracker.be.dto.LabelSaveRequest;
import issuetracker.be.dto.LabelUpdateRequest;
import issuetracker.be.service.LabelService;
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

@Slf4j
@RestController
public class LabelController {

  private final LabelService labelService;

  @Autowired
  public LabelController(LabelService labelService) {
    this.labelService = labelService;
  }

  @GetMapping("/label")
  public List<Label> getLabels() {
    return labelService.getAllLabel();
  }

  @PostMapping("/label")
  public void save(@RequestBody LabelSaveRequest labelSaveRequest) {
    labelService.save(labelSaveRequest);
  }

  @PutMapping("/label")
  public void update(@RequestBody LabelUpdateRequest labelUpdateRequest) {
    labelService.update(labelUpdateRequest);
  }

  @DeleteMapping("/label/{id}")
  public void delete(@PathVariable Long id) {
    labelService.delete(id);
  }

  @PatchMapping("/issue/label")
  public void updateAssignee(@RequestBody IssueLabelUpdateRequest issueLabelUpdateRequest) {
    labelService.updateLabel(issueLabelUpdateRequest);
  }
}
