package serverbot.server;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends CrudRepository<Server, String> {
    Streamable<Server> findAll();
}
