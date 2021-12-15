package serverbot.statistics;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface StatisticsRepository extends CrudRepository<Statistics, String> {
    Streamable<Statistics> findAll();

    Optional<Statistics> findByUserIdAndServerId(String userId, String serverId);
}
