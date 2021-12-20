package serverbot.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverbot.role.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
@IdClass(MemberId.class)
public class Member {

    @Id
    private String userId;

    @Id
    private String serverId;

    private boolean isExiled;
    private boolean isBanned;
    private boolean isMuted;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    public Member(String userId, String serverId, boolean isExiled, boolean isBanned, boolean isMuted, List<Role> roles) {
        this.userId = userId;
        this.serverId = serverId;
        this.isExiled = isExiled;
        this.isBanned = isBanned;
        this.isMuted = isMuted;
        this.roles = roles;
    }
}
