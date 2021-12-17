package serverbot.moderation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
@IdClass(ModerationId.class)
public class Moderation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Id
    private String userId;

    @Id
    private String serverId;

    private ModerationType moderationType;

    private Long durationInSeconds;

    private boolean deletedMessages;

    private String reason;

    public Moderation(String userId, String serverId, ModerationType moderationType, Long durationInSeconds,
                      boolean deletedMessages, String reason) {
        this.userId = userId;
        this.serverId = serverId;
        this.moderationType = moderationType;
        this.durationInSeconds = durationInSeconds;
        this.deletedMessages = deletedMessages;
        this.reason = reason;
    }
}
