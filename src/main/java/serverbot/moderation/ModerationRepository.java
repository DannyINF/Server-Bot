package serverbot.moderation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface ModerationRepository extends CrudRepository<Moderation, ModerationId> {
    Streamable<Moderation> findAll();
}
