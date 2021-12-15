package serverbot.channel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

public interface ChannelRepository extends CrudRepository<Channel, String> {
    Streamable<Channel> findAll();

    Optional<Channel> findByChannelIdAndServerId(String channelId, String serverId);
}
