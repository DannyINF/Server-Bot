package serverbot.channel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, ChannelId> {
    Streamable<Channel> findAll();

    Optional<Channel> findByChannelId(String channelId);

    Optional<Channel> findByChannelIdAndServerId(String channelId, String serverId);

    Streamable<Channel> findByServerIdAndChannelType(String serverId, ChannelType channelType);
}
