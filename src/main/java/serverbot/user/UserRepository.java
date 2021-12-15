package serverbot.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface UserRepository extends CrudRepository<User, String> {
    Streamable<User> findAll();
}
