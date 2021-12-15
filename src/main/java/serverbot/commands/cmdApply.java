package serverbot.commands;

import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class cmdApply implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        //TODO: close reports that are open
        databaseHandler.database(event.getGuild().getId(), "delete from reports where victim_id = '" + event.getAuthor().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4' or report_id = '5')");
        databaseHandler.database(event.getGuild().getId(), "delete from applications where id = '" + event.getAuthor().getId() + "' and step < 10");
        //core.databaseHandler.database(event.getGuild().getId(), "delete from quizquestions where author_id = '" + event.getAuthor().getId() + "' and status < 14");
        databaseHandler.database(event.getGuild().getId(), "insert into applications (id, step, name, age, location, language, about_you, change) " +
                "values ('" + event.getAuthor().getId() + "', 1, '', '', '', '', '', '')");
        event.getAuthor().openPrivateChannel().queue(channel -> {
            channel.sendMessage(">>> Hey **" + event.getAuthor().getAsTag() + "**,\n" +
                            "um eine Bewerbung zu erstellen, musst du mir noch einige Fragen beantworten:").queue();
            channel.sendMessage(">>> **Achte darauf, dass du KEINE vertrauliche oder pers\u00f6nliche Informationen weitergibst!**").queue();
            channel.sendMessage(">>> Wie sollen wir dich nennen? Am liebsten w\u00e4re uns nat\u00fcrlich dein Vorname, aber wenn du andere Bezeichnungen vorziehst, dann wollen wir dir nichts vorschreiben. (Familiennamen sollen **NICHT** angegeben werden!) (max. 50 Zeichen)").queue();
        });
    }
}