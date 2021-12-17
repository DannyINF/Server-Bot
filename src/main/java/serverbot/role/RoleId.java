package serverbot.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class RoleId implements Serializable {
    private String roleId;
    private String serverId;

    public RoleId(String roleId, String serverId) {
        this.roleId = roleId;
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
        RoleId roleId = (RoleId) o;
        return Objects.equals( roleId, roleId.roleId ) &&
                Objects.equals( serverId, roleId.serverId );
    }

    @Override
    public int hashCode() {
        return Objects.hash( roleId, serverId );
    }
}
