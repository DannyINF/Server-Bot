package serverbot.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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

    @ElementCollection
    private List<String> roles;

    public Member(String userId, String serverId, boolean isExiled, boolean isBanned, boolean isMuted, List<String> roles) {
        this.userId = userId;
        this.serverId = serverId;
        this.isExiled = isExiled;
        this.isBanned = isBanned;
        this.isMuted = isMuted;
        this.roles = roles;
    }
}
