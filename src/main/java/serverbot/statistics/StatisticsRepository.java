package serverbot.statistics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, StatisticsId> {
    Streamable<Statistics> findAll();

    Optional<Statistics> findByUserIdAndServerId(String userId, String serverId);
}
