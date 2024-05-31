package issuetracker.be.repository;

import issuetracker.be.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, String> {
  @Override
  List<User> findAll();

  Optional<User> findByNameEquals(String userName);
}
