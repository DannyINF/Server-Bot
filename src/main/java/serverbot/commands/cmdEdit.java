package serverbot.commands;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class cmdEdit implements Command {

    //TODO: create

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        /*if (Objects.requireNonNull(event.getMember()).getUser().getId().equals("277746420281507841")) {
            System.out.println(Arrays.toString(
                    databaseHandler.database(args[0], event.getMessage().getContentRaw().replace("/edit ", "").replaceFirst(args[0] + " ", ""))));
        }*/
    }
}
