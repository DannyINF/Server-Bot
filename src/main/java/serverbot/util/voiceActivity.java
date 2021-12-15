package serverbot.util;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.sql.SQLException;

import static java.lang.StrictMath.sqrt;

public class voiceActivity {
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

                    giveXP.giveXPToMember(member, guild, xp);
                    String[] answer = null;
                    /*try {
                        answer = databaseHandler.database(STATIC.getGuild().getId(), "select recent_activity from users where id = '" + member.getId() + "'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    int activity = 1;
                    assert answer != null;
                    if (Integer.parseInt(answer[0]) == 1000)
                        activity = 0;
                    //data.statistics
                    /*if (membercount>1) {
                        databaseHandler.database(guild.getId(), "update users set voicetime = voicetime + 1, activity = activity + " + activity + ", "  +
                                "recent_activity = recent_activity + " + activity + " where id = '" + member.getId() + "'");
                    }*/
                }
            }
        }
    }
}