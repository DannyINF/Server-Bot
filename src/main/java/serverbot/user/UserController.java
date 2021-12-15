package serverbot.user;

import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final UserManagement userManagement;

    public UserController(UserManagement userManagement) {
        this.userManagement = userManagement;
    }
}
