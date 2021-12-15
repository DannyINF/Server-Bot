package serverbot.user;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Component
@Order(10)
public class UserDataInitializer {

    private final UserRepository userRepository;

    public UserDataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void initialize() {
        User user = new User("277746420281507841", Locale.GERMAN);
        userRepository.save(user);
    }
}
