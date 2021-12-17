package serverbot.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
@IdClass(StatisticsId.class)
public class Statistics {
    @Id
    private final String userId;

    @Id
    private final String serverId;

    private Long words;
    private Long messages;
    private Long chars;

    private Long voiceTime;

    private LocalDateTime firstJoin;
    private LocalDateTime lastJoin;
    private LocalDateTime lastLeave;

    private Long xp;
    private Long level;

    private Long currency;

    public Statistics(String userId, String serverId, Long words,
                      Long messages, Long chars,
                      Long voiceTime, LocalDateTime firstJoin, LocalDateTime lastJoin,
                      LocalDateTime lastLeave, Long xp,
                      Long level, Long currency) {
        this.userId = userId;
        this.serverId = serverId;
        this.words = words;
        this.messages = messages;
        this.chars = chars;
        this.voiceTime = voiceTime;
        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.lastLeave = lastLeave;
        this.xp = xp;
        this.level = level;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "userId='" + userId + '\'' +
                ", serverId='" + serverId + '\'' +
                ", words=" + words +
                ", messages=" + messages +
                ", chars=" + chars +
                ", voiceTime=" + voiceTime +
                ", firstJoin=" + firstJoin +
                ", lastJoin=" + lastJoin +
                ", lastLeave=" + lastLeave +
                ", xp=" + xp +
                ", level=" + level +
                ", currency=" + currency +
                '}';
    }
}
