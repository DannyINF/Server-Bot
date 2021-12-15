package serverbot.commands;

import serverbot.core.databaseHandler;
import serverbot.core.messageActions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

class cmdXpRanking {

    public static void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        int start;
        try {
            start = Integer.parseInt(args[1]);
        } catch (Exception e) {
            start = Integer.parseInt(Objects.requireNonNull(databaseHandler.database(event.getGuild().getId(), "select * from (select row_number() over (), id from (select id, xp, level from users order by xp desc) as tmp) as temp where id = '" + event.getAuthor().getId() + "'"))[0]) - 5;
        }
        if (start <= 0)
            start = 1;
        int k;
        String name;
        String level;
        String xp;

        String[] answer = databaseHandler.database(event.getGuild().getId(), "select id, xp, level from users order by xp desc offset " + (start - 1) + " rows fetch next 10 rows only");

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < 10; j++) {
            try {
                name = Objects.requireNonNull(event.getJDA().getUserById(answer[j * 3])).getAsTag();
            } catch (Exception e) {
                name = Objects.requireNonNull(answer)[j * 3];
            }
            xp = answer[j*3+1];
            level = answer[j*3+2];

            if (name.equals(event.getAuthor().getAsTag()) && j == 0) {
                sb.append("```css\n");
            } else if (!name.equals(event.getAuthor().getAsTag()) && j == 0) {
                sb.append("```");
            } else if (name.equals(event.getAuthor().getAsTag())) {
                sb.append("\n``````css\n");
            }

            sb.append(start + j);
            sb.append(". ");
            sb.append(name);
            k = name.length();
            while (k < 35) {
                sb.append(" ");
                k++;
            }
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
            sb.append(messageActions.getLocalizedString("xp_ranking_level", "serverbot/user", event.getAuthor().getId()));
            sb.append(numberFormat.format(Long.parseLong(level)));
            k = level.length();
            while (k < 10) {
                sb.append(" ");
                k++;
            }
            sb.append(messageActions.getLocalizedString("xp_ranking_xp", "serverbot/user", event.getAuthor().getId()));
            sb.append(numberFormat.format(Long.parseLong(xp)));
            k = xp.length();
            while (k < 10) {
                sb.append(" ");
                k++;
            }
            sb.append("\n");
            if (name.equals(event.getAuthor().getAsTag()) && !(j == 9)) {
                sb.append("\n``````");
            }

        }
        sb.append("```");

        event.getChannel().sendMessage(sb.toString()).queue();

    }

}
