package serverbot.channel;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ChannelManagement {
    private ChannelRepository channelRepository;

    public ChannelManagement(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Streamable<Channel> findAll() {
        return channelRepository.findAll();
    }

    public Optional<Channel> findByChannelIdAndServerId(String channelId, String serverId) {
        return channelRepository.findByChannelIdAndServerId(channelId, serverId);
    }

    public void save(Channel channel) {
        channelRepository.save(channel);
    }
}
