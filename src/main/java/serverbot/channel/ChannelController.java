package serverbot.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ChannelController {

    @Autowired
    private ChannelManagement channelManagement;
}
