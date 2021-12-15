package serverbot.channel;

import org.springframework.stereotype.Controller;

@Controller
public class ChannelController {
    private final ChannelManagement channelManagement;

    public ChannelController(ChannelManagement channelManagement) {
        this.channelManagement = channelManagement;
    }
}
