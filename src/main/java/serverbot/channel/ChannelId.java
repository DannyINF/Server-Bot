package serverbot.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class ChannelId implements Serializable {
    private String userId;
    private String serverId;

    public ChannelId(String userId, String serverId) {
        this.userId = userId;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        serverbot.channel.ChannelId channelId = (serverbot.channel.ChannelId) o;
        return Objects.equals( userId, channelId.userId ) &&
                Objects.equals( serverId, channelId.serverId );
    }

    @Override
    public int hashCode() {
        return Objects.hash( userId, serverId );
    }
}