package issuetracker.be.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import issuetracker.be.domain.Milestone;
import issuetracker.be.dto.MilestoneSaveRequest;
import issuetracker.be.dto.MilestoneUpdateRequest;
import issuetracker.be.exception.MilestoneHasAssociatedIssuesException;
import issuetracker.be.repository.MilestoneRepository;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MilestoneServiceTest {

  @Mock
  private MilestoneRepository milestoneRepository;
  @InjectMocks
  private MilestoneService milestoneService;
  @Mock
  private IssueService issueService;

  @Test
  void 정상적으로_마일스톤이_저장되는지_확인한다() {
    MilestoneSaveRequest milestoneSaveRequest = new MilestoneSaveRequest("first milestone", null,
        LocalDate.now());
    Milestone milestone = new Milestone();
    milestone.setName("first milestone");

    when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);

    Milestone result = milestoneService.save(milestoneSaveRequest);

    verify(milestoneRepository).save(any(Milestone.class));
    assertNotNull(milestone);
    assertThat(milestone.getName()).isEqualTo(result.getName());
  }

  @Test
  void 정상적으로_마일스톤이_삭제되는지_확인한다() {
    Milestone milestone = new Milestone();
    milestone.setId(1L);

    when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));
    when(issueService.isIssueExistBy(1L)).thenReturn(false);

    Long deletedResult = milestoneService.delete(1L);

    verify(milestoneRepository).delete(any(Milestone.class));
    assertThat(1L).isEqualTo(deletedResult);
  }

  @Test
  void 존재하지_않는_마일스톤을_삭제하는_경우_예외가_발생한다() {
    when(milestoneRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> milestoneService.delete(1L))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("해당 마일스톤이 존재하지 않습니다.");
  }

  @Test
  void 이슈가_포함된_마일스톤을_삭제하는_경우_예외가_발생한다() {
    Milestone milestone = new Milestone();
    milestone.setId(1L);

    when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));
    when(issueService.isIssueExistBy(1L)).thenReturn(true);

    assertThatThrownBy(() -> milestoneService.delete(1L))
        .isInstanceOf(MilestoneHasAssociatedIssuesException.class)
        .hasMessageContaining("이슈가 존재하는 마일스톤은 삭제할 수 없습니다.");
  }

  @Test
  void 마일스톤_업데이트를_확인한다() {
    Milestone milestone = new Milestone();
    milestone.setId(1L);
    milestone.setName("first milestone");

    MilestoneUpdateRequest milestoneUpdateRequest = new MilestoneUpdateRequest(1L, "테스트 마일스톤",
        LocalDate.now(), null);

    when(milestoneRepository.save(any(Milestone.class))).thenReturn(
        milestoneUpdateRequest.toEntity());
    when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));

    Milestone result = milestoneService.update(milestoneUpdateRequest);

    assertThat(result.getName()).isEqualTo("updated milestone");
  }
}