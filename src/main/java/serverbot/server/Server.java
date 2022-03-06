package serverbot.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import serverbot.Main;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
public class Server {
    @Id
    private final String id;

    private int xpMultiplier;

    private String prefix;

    private int announcementsCounter;

    public Server(String id, int xpMultiplier, String prefix, int announcementsCounter) {
        this.id = id;
        this.xpMultiplier = xpMultiplier;
        this.prefix = prefix;
        this.announcementsCounter = announcementsCounter;
    }

    public String getName() {
        return Main.jda.getGuildById(this.getId()).getName();
    }

    public Long getIdLong() {
        return Long.parseLong(id);
    }

    public Long getOnlineMembers(JDA jda) {
        AtomicReference<Long> online = new AtomicReference<>(Long.valueOf(0));
        jda.getGuildById(id).getMembers().stream().forEach((member -> {
            if (member.getOnlineStatus() != OnlineStatus.OFFLINE) {
                online.getAndSet(online.get() + 1);
            }
        }));
        return online.get();
    }
}
