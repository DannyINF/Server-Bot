package serverbot.channel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, String> {
    Streamable<Channel> findAll();

    Optional<Channel> findByChannelIdAndServerId(String channelId, String serverId);
}
