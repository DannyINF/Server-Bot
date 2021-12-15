package serverbot.statistics;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serverbot.channel.ChannelManagement;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class StatisticsManagement {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private ChannelManagement channelManagement;

    public void save(Statistics statistics) {
        statisticsRepository.save(statistics);
    }

    public Streamable<Statistics> findAll() {
        return statisticsRepository.findAll();
    }

    public Optional<Statistics> findByUserIdAndServerId(String userId, String serverId) {
        return statisticsRepository.findByUserIdAndServerId(userId, serverId);
    }

    public void addXpToUser(String userId, String serverId, Long amount) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setXp(statistics.getXp() + amount);
        save(statistics);
    }

    public void addWordsToUser(String userId, String serverId, Long amount) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setWords(statistics.getWords() + amount);
        save(statistics);
    }

    public void addMessagesToUser(String userId, String serverId, Long amount) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setMessages(statistics.getMessages() + amount);
        save(statistics);
    }

    public void addCharsToUser(String userId, String serverId, Long amount) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setChars(statistics.getChars() + amount);
        save(statistics);
    }

    public void giveXP(GuildMessageReceivedEvent event) throws SQLException {
        Float xp;
        int amount = event.getMessage().getContentRaw().length();

        // only the first 140 characters count
        xp = Math.min(amount, 140F);

        if (!event.getMessage().getAttachments().isEmpty()) {
            xp += 15;
        }

        xp *= channelManagement.findByChannelIdAndServerId(event.getChannel().getId(), event.getGuild().getId()).get().getXpMultiplier();


        addXpToUser(event.getMember().getId(), event.getGuild().getId(), xp.longValue());
    }
}
