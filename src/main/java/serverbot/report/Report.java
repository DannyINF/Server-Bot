package serverbot.report;

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
@IdClass(ReportId.class)
public class Report {
    @Id
    private LocalDateTime dateTime;

    @Id
    private String userId;

    @Id
    private String serverId;

    private String offenderId;

    private String channelId;

    private String cause;

    private String info;

    private RulingType rulingType;

    private String messageId;

    private Long trollCredits;

    public Report(LocalDateTime dateTime, String userId, String serverId, String offenderId, String channelId,
                  String cause, String info, RulingType rulingType, String messageId, Long trollCredits) {
        this.dateTime = dateTime;
        this.userId = userId;
        this.serverId = serverId;
        this.offenderId = offenderId;
        this.channelId = channelId;
        this.cause = cause;
        this.info = info;
        this.rulingType = rulingType;
        this.messageId = messageId;
        this.trollCredits = trollCredits;
    }
}
