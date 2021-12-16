package serverbot.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

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
}
