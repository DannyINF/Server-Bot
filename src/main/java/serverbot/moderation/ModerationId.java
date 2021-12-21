package serverbot.moderation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class ModerationId implements Serializable {
    private LocalDateTime dateTime;
    private String userId;
    private String serverId;

    public ModerationId(LocalDateTime dateTime, String userId, String serverId) {
        this.dateTime = dateTime;
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
        ModerationId moderationId = (ModerationId) o;
        return Objects.equals( dateTime, moderationId.dateTime ) &&
                Objects.equals( userId, moderationId.userId ) &&
                Objects.equals( serverId, moderationId.serverId );
    }

    @Override
    public int hashCode() {
        return Objects.hash( dateTime, userId, serverId );
    }
}
