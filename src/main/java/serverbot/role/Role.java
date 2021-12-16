package serverbot.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
public class Role {

    @Id
    private String id;

    private final String serverId;

    private RoleType roleType;

    public Role(String id, String serverId, RoleType roleType) {
        this.id = id;
        this.serverId = serverId;
        this.roleType = roleType;
    }
}
