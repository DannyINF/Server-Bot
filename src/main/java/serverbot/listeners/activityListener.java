package serverbot.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class activityListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        int length = event.getMessage().getContentRaw().length();
        if (length > 200)
            length = 200;
        int activity = length / 20;
        String[] answer = null;
        /*try { this
            answer = databaseHandler.database(STATIC.getGuild().getId(), "select recent_activity from users where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int answerint;
        if (answer == null || answer.length == 0) {
            try {
                databaseHandler.database(STATIC.getGuild().getId(), "insert into users (id, intro, profile, words, msg, chars, voicetime, verifystatus, activity, recent_activity, first_join, last_join, language, country, sex, xp, level) " +
                        "values ('" + event.getAuthor().getId() + "', 0, '', 0, 0, 0, 0, false, 0, 0, CURRENT_DATE, CURRENT_DATE, '', '', 'm', 0, 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            answerint = 0;
        } else {
            answerint = Integer.parseInt(answer[0]);
        }
        if ((1000 - answerint) < activity)
            activity = 1000 - Integer.parseInt(answer[0]);
        try {
            databaseHandler.database(STATIC.getGuild().getId(), "update users set activity = activity + " + activity + ", " +
                    "recent_activity = recent_activity + " + activity + " where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(event.getMember()).getRoles().contains(STATIC.getNewcomer()[0])) {
            String[] answerRoles = null;
            try {
                answerRoles = databaseHandler.database(STATIC.getGuild().getId(), "select {fn timestampdiff(SQL_TSI_FRAC_SECOND, last_join, CURRENT_DATE)} / 86400000000000 from users where id = '" + event.getAuthor().getId() + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert answerRoles != null;
            if (Integer.parseInt(answerRoles[0]) <= 7)
                STATIC.getGuild().addRoleToMember(event.getAuthor().getId(), STATIC.getNewcomer()[0]).queue();
        }*/
    }
}
