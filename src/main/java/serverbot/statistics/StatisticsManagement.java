package serverbot.statistics;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serverbot.channel.ChannelManagement;
import serverbot.util.GiveXP;

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

    public Optional<Statistics> findById(StatisticsId statisticsId) {
        return statisticsRepository.findById(statisticsId);
    }

    public Optional<Statistics> findByUserIdAndServerId(String userId, String serverId) {
        return statisticsRepository.findByUserIdAndServerId(userId, serverId);
    }

    public void addXpToUser(String userId, String serverId, Long amount) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setXp(statistics.getXp() + amount);
        save(statistics);
    }

    public void addCreditsToUser(String userId, String serverId, Long amount) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setCurrency(statistics.getCurrency() + amount);
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

    public void addVoiceTimeToUser(String userId, String serverId, Long amount) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setVoiceTime(statistics.getChars() + amount);
        save(statistics);
    }

    public void setLevelOfUser(String userId, String serverId, Long level) {
        Statistics statistics = findByUserIdAndServerId(userId, serverId).get();
        statistics.setLevel(level);
        save(statistics);
    }

    public void giveXP(GuildMessageReceivedEvent event) {
        Double xp;
        int amount = event.getMessage().getContentRaw().length();

        // only the first 140 characters count
        xp = Math.min(amount, 140D);

        if (!event.getMessage().getAttachments().isEmpty()) {
            xp += 15;
        }

        GiveXP.giveXPToMember(event.getMember(), event.getGuild(), xp.longValue(), event.getChannel().getId());
    }
}
