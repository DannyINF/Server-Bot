package serverbot.statistics;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nonnegative;
import java.time.LocalDateTime;

@Getter
public class StatisticsForm {

    @Nonnegative
    private final Long words;

    @Nonnegative
    private final Long messages;

    @Nonnegative
    private final Long chars;

    @Nonnegative
    private final Long voiceTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final String firstJoin;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final String lastJoin;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final String lastLeave;

    @Nonnegative
    private final Long xp;
    @Nonnegative
    private final Long level;

    @Nonnegative
    private final Long currency;

    public StatisticsForm(Long words, Long messages, Long chars, Long voiceTime, String firstJoin,
    String lastJoin, String lastLeave, Long xp, Long level, Long currency) {
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

    public LocalDateTime getFirstJoin() {
        return LocalDateTime.parse(firstJoin);
    }

    public LocalDateTime getLastJoin() {
        return LocalDateTime.parse(lastJoin);
    }

    public LocalDateTime getLastLeave() {
        return LocalDateTime.parse(lastLeave);
    }
}
