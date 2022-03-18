package serverbot.util;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import serverbot.statistics.StatisticsManagement;

import java.sql.SQLException;

import static java.lang.StrictMath.sqrt;

public class VoiceActivity {
    public static void giveVoiceActivity(JDA jda) throws SQLException {
        for (Guild guild : jda.getGuilds()) {
            for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
                double membercount = voiceChannel.getMembers().size();
                for (Member member : voiceChannel.getMembers())
                    if (member.getUser().isBot())
                        membercount--;
                if (membercount == 1) {
                    membercount = 0;
                }

                for (Member member : voiceChannel.getMembers()) {
                    long xp;

                    xp = (long) (sqrt(2520 * membercount - 671) / 9 - 43 / 9);

                    GiveXP.giveXPToMember(member, guild, xp);
                    StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
                    statisticsManagement.addVoiceTimeToUser(member.getId(), guild.getId(), 1L);

                }
            }
        }
    }
}