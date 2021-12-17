package serverbot.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
@IdClass(RoleId.class)
public class Role {

    @Id
    private String roleId;

    @Id
    private final String serverId;

    private RoleType roleType;

    public Role(String roleId, String serverId, RoleType roleType) {
        this.roleId = roleId;
        this.serverId = serverId;
        this.roleType = roleType;
    }
}
