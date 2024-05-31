package issuetracker.be.repository;

import issuetracker.be.domain.Comment;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

  @Query("SELECT * FROM comment WHERE issue_id = :id")
  List<Comment> findByIssueId(Long id);

  List<Comment> findByReporter(String reporter);

  @Modifying
  @Query("DELETE FROM comment WHERE issue_id = :id")
  void deleteIssue(@Param("id") Long id);
}