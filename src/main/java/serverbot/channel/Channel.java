package serverbot.channel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
@IdClass(ChannelId.class)
public class Channel {
    @Id
    private final String channelId;

    @Id
    private final String serverId;

    private ChannelType channelType;

    private Double xpMultiplier;

    public Channel(String channelId, String serverId, ChannelType channelType, Double xpMultiplier) {
        this.channelId = channelId;
        this.serverId = serverId;
        this.channelType = channelType;
        this.xpMultiplier = xpMultiplier;
    }

    public Long getChannelIdLong() {
        return Long.parseLong(channelId);
    }
}
