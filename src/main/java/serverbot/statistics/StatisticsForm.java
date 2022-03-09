package serverbot.statistics;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nonnegative;
import java.time.LocalDateTime;

@Getter
public class StatisticsForm {

    @Nonnegative
    private Long words;

    @Nonnegative
    private Long messages;

    @Nonnegative
    private Long chars;

    @Nonnegative
    private Long voiceTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime firstJoin;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime lastJoin;

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime lastLeave;

    @Nonnegative
    private Long xp;
    @Nonnegative
    private Long level;

    @Nonnegative
    private Long currency;

    public StatisticsForm(Long words, Long messages, Long chars, Long voiceTime, LocalDateTime firstJoin,
    LocalDateTime lastJoin, LocalDateTime lastLeave, Long xp, Long level, Long currency) {
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
}
