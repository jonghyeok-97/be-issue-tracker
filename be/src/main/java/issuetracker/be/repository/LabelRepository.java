package issuetracker.be.repository;

import issuetracker.be.domain.Label;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends CrudRepository<Label, Long> {

  @Override
  List<Label> findAll();

  @Modifying
  @Query("DELETE FROM label WHERE id = :id")
  void deleteLabel(@Param("id") Long id);
}