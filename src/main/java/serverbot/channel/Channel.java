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

    private Float xpMultiplier;

    public Channel(String channelId, String serverId, ChannelType channelType, Float xpMultiplier) {
        this.channelId = channelId;
        this.serverId = serverId;
        this.channelType = channelType;
        this.xpMultiplier = xpMultiplier;
    }
}
