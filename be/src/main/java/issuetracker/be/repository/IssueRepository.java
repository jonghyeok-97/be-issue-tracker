package issuetracker.be.repository;

import issuetracker.be.domain.Issue;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IssueRepository extends CrudRepository<Issue, Long> {

  @Query("SELECT COUNT(*) > 0 FROM issue WHERE milestone_id = :milestoneId")
  boolean existsByMilestoneId(@Param("milestoneId") Long milestoneId);

  @Query("SELECT * FROM issue WHERE is_open = :isOpen")
  List<Issue> findByIsOpen(@Param("isOpen") boolean isOpen);

  @Modifying
  @Query("UPDATE issue SET title = :title WHERE id = :id")
  void updateTitle(@Param("title") String title, @Param("id") Long id);

  @Modifying
  @Query("DELETE FROM issue WHERE id = :id")
  void deleteIssue(@Param("id") Long id);

  @Modifying
  @Query("UPDATE issue SET milestone_id = :milestoneId WHERE id = :id")
  void updateMilestoneId(@Param("milestoneId") Long milestoneId, @Param("id") Long id);

  @Modifying
  @Query("UPDATE issue SET milestone_id = NULL WHERE id = :id")
  void deleteMilestoneId(@Param("id") Long id);
}
