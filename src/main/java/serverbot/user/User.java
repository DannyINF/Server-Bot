package serverbot.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Locale;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
public class User {
    @Id
    private final String id;

    private Locale language;

    private boolean isExiled;

    public User(String id, Locale language, boolean isExiled) {
        this.id = id;
        this.language = language;
        this.isExiled = isExiled;
    }
}