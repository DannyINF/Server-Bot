package serverbot.moderation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class ModerationId implements Serializable {
    private String id;
    private String userId;
    private String serverId;

    public ModerationId(String id, String userId, String serverId) {
        this.id = id;
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
        ModerationId statisticsId = (ModerationId) o;
        return Objects.equals( id, statisticsId.id ) &&
                Objects.equals( userId, statisticsId.userId ) &&
                Objects.equals( serverId, statisticsId.serverId );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, userId, serverId );
    }
}
