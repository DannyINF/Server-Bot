package serverbot.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ChannelManagement {

    @Autowired
    private ChannelRepository channelRepository;

    public Streamable<Channel> findAll() {
        return channelRepository.findAll();
    }

    public Optional<Channel> findById(String id) {
        return channelRepository.findById(id);
    }

    public Optional<Channel> findByChannelIdAndServerId(String channelId, String serverId) {
        return channelRepository.findByChannelIdAndServerId(channelId, serverId);
    }

    public Streamable<Channel> findByServerIdAndChannelType(String serverId, ChannelType channelType) {
        return channelRepository.findByServerIdAndChannelType(serverId, channelType);
    }

    public void save(Channel channel) {
        channelRepository.save(channel);
    }

    public void delete(Channel channel) {
        channelRepository.delete(channel);
    }
}
