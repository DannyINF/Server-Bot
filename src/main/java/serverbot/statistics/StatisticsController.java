package serverbot.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import serverbot.channel.ChannelManagement;
import org.springframework.stereotype.Controller;

@Controller
public class StatisticsController {

    @Autowired
    private StatisticsManagement statisticsManagement;

    @Autowired
    private ChannelManagement channelManagement;
}
