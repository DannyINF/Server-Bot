package serverbot.moderation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
@IdClass(ModerationId.class)
public class Moderation {
    @Id
    private LocalDateTime dateTime;

    @Id
    private String userId;

    @Id
    private String serverId;

    private String moderatorId;

    private ModerationType moderationType;

    private Long durationInSeconds;

    private boolean deletedMessages;

    private String reason;

    public Moderation(LocalDateTime dateTime, String userId, String serverId, String moderatorId, ModerationType moderationType, Long durationInSeconds,
                      boolean deletedMessages, String reason) {
        this.dateTime = dateTime;
        this.userId = userId;
        this.serverId = serverId;
        this.moderatorId = moderatorId;
        this.moderationType = moderationType;
        this.durationInSeconds = durationInSeconds;
        this.deletedMessages = deletedMessages;
        this.reason = reason;
    }
}
