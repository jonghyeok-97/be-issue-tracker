package issuetracker.be.repository;

import issuetracker.be.domain.AssigneeRef;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssigneeRefRepository extends CrudRepository<AssigneeRef, Long> {

  @Query("SELECT * FROM issue_assignee WHERE issue_id = :id")
  List<AssigneeRef> findAllUser(@Param("id") Long id);

  @Modifying
  @Query("DELETE FROM issue_assignee WHERE issue_id = :id")
  void deleteAllAssignee(@Param("id") Long id);

  @Modifying
  @Query("INSERT INTO issue_assignee (issue_id, user_name) VALUES (:issueId, :name)")
  void addAssignee(@Param("issueId") Long issueId, @Param("name") String name);

}
