package serverbot.commands;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class cmdReport implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        //TODO: close reports that are open
        /*databaseHandler.database(event.getGuild().getId(), "delete from reports where victim_id = '" + event.getAuthor().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4' or report_id = '5')");
        databaseHandler.database(event.getGuild().getId(), "delete from applications where id = '" + event.getAuthor().getId() + "' and step < 10");
        //core.databaseHandler.database(event.getGuild().getId(), "delete from quizquestions where author_id = '" + event.getAuthor().getId() + "' and status < 14");
        databaseHandler.database(event.getGuild().getId(), "insert into reports (report_id, victim_id, offender_id, channel, cause, info) " +
                "values ('1', '" + event.getAuthor().getId() + "', '', '', '', '')");
        event.getAuthor().openPrivateChannel().queue(channel -> {
            channel.sendMessage(">>> Hey **" + event.getAuthor().getAsTag() + "**,\n" +
                            "um einen Report zu erstellen, musst du mir noch einige Fragen beantworten:").queue();
            channel.sendMessage(">>> Wen m\u00f6chtest du reporten? Gib hier am besten alles so genau wie m\u00f6glich an, wie z.B. die ID oder den Namen mit Tag (Nutzer#1234).").queue();
        });*/
    }
}