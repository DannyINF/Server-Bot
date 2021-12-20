package serverbot.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class MemberId implements Serializable {
    private String userId;
    private String serverId;

    public MemberId(String userId, String serverId) {
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
        MemberId memberId = (MemberId) o;
        return Objects.equals( userId, memberId.userId ) &&
                Objects.equals( serverId, memberId.serverId );
    }

    @Override
    public int hashCode() {
        return Objects.hash( userId, serverId );
    }
}
