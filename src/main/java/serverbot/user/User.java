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

    public User(String id, Locale language) {
        this.id = id;
        this.language = language;
    }
}