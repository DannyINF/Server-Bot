package serverbot.channel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
public class Channel {
    @Id
    private final String channelId;

    private final String serverId;

    private Float xpMultiplier;
    private Long id;

    public Channel(String channelId, String serverId, Float xpMultiplier) {
        this.channelId = channelId;
        this.serverId = serverId;
        this.xpMultiplier = xpMultiplier;
    }
}
