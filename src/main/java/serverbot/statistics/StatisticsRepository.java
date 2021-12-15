package serverbot.statistics;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, String> {
    Streamable<Statistics> findAll();

    Optional<Statistics> findByUserIdAndServerId(String userId, String serverId);
}
