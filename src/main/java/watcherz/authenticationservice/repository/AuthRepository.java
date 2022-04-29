package watcherz.authenticationservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import watcherz.authenticationservice.model.User;

import java.util.UUID;


@Repository
public interface AuthRepository extends CrudRepository<User, UUID> {
    User findByEmail(String email);
}
