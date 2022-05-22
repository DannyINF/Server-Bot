package serverbot.core;

import serverbot.commands.Command;

import java.util.HashMap;

public class CommandHandlerMusic {

    public static final HashMap<String, Command> commands = new HashMap<>();

    /*public static void handleCommand(CommandParser.commandContainer cmd) throws Exception {
        if (commands.containsKey(cmd.invoke) && cmd.invoke.equals("music")) {
            boolean safe = commands.get(cmd.invoke).called();
            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
            }
        }
    }*/
}