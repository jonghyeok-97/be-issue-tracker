package issuetracker.be.repository;

import issuetracker.be.domain.Milestone;
import issuetracker.be.dto.MilestoneWithIssueCountDto;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MilestoneRepository extends CrudRepository<Milestone, Long> {

  @Query("SELECT m.*, " +
      "(SELECT COUNT(id) FROM issue WHERE milestone_id = m.id AND is_open = 1) AS open_issue, " +
      "(SELECT COUNT(id) FROM issue WHERE milestone_id = m.id AND is_open = 0) AS close_issue " +
      "FROM milestone m ")
  List<MilestoneWithIssueCountDto> findAllWithIssueCount();
}