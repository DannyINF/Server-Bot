package serverbot.commands;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class CmdEdit implements Command {

    //TODO: create

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws SQLException {
        /*if (Objects.requireNonNull(event.getMember()).getUser().getId().equals("277746420281507841")) {
            System.out.println(Arrays.toString(
                    databaseHandler.database(args[0], event.getMessage().getContentRaw().replace("/edit ", "").replaceFirst(args[0] + " ", ""))));
        }*/
    }
}
