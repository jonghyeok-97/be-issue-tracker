package issuetracker.be.service;

import issuetracker.be.domain.Label;
import issuetracker.be.dto.IssueLabelUpdateRequest;
import issuetracker.be.dto.LabelSaveRequest;
import issuetracker.be.dto.LabelUpdateRequest;
import issuetracker.be.repository.LabelRefRepository;
import issuetracker.be.repository.LabelRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class LabelService {

  private final LabelRepository labelRepository;
  private final LabelRefRepository labelRefRepository;

  @Autowired
  public LabelService(LabelRepository labelRepository, LabelRefRepository labelRefRepository) {
    this.labelRepository = labelRepository;
    this.labelRefRepository = labelRefRepository;
  }

  public List<Label> getAllLabel() {
    return labelRepository.findAll();
  }

  @Transactional
  public void save(LabelSaveRequest labelSaveRequest) {
    Label savedLabel = labelRepository.save(labelSaveRequest.toEntity());
    log.debug("저장된 라벨 정보 : {}", savedLabel);
  }

  @Transactional
  public void update(LabelUpdateRequest request) {
    Optional<Label> optOriginLabel = labelRepository.findById(request.id());
    log.debug("업데이트할 라벨 : {}", optOriginLabel.get());
    optOriginLabel.ifPresentOrElse(
        originLabel -> {
          Label entity = request.toEntity();
          Label updatedLabel = labelRepository.save(entity);
          log.debug("라벨 업데이트 정보 : {} -> {}", originLabel, updatedLabel);
        },
        () -> {
          throw new NoSuchElementException("업데이트할 라벨이 없습니다");
        });
  }

  @Transactional
  public void delete(Long labelId) {
    labelRefRepository.deleteByLabel(labelId);
    labelRepository.deleteLabel(labelId);
  }

  @Transactional
  public void deleteLabelRef(Long issueId) {
    labelRefRepository.deleteIssue(issueId);
  }

  public Label findById(Long labelId) {
    return labelRepository.findById(labelId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 라벨입니다."));
  }

  @Transactional
  public void updateLabel(IssueLabelUpdateRequest issueLabelUpdateRequest) {
    Long id = issueLabelUpdateRequest.issue_id();
    List<Long> labels = issueLabelUpdateRequest.label_id();

    if (labels != null) {
      deleteLabelRef(id);
      labels.forEach(label -> labelRefRepository.addLabelRef(id, label));
    } else {
      deleteLabelRef(id);
    }
  }
}
