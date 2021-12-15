package serverbot.statistics;

import serverbot.channel.ChannelManagement;
import org.springframework.stereotype.Controller;

@Controller
public class StatisticsController {
    private final StatisticsManagement statisticsManagement;
    private final ChannelManagement channelManagement;

    public StatisticsController(StatisticsManagement statisticsManagement, ChannelManagement channelManagement) {
        this.statisticsManagement = statisticsManagement;
        this.channelManagement = channelManagement;
    }
}
