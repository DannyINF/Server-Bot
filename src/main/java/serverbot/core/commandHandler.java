package serverbot.core;

import serverbot.commands.Command;

import java.util.HashMap;

public class commandHandler {

    public static final HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(commandParser.commandContainer cmd) throws Exception {
        String addition = "";
        if (cmd.invoke.equals("server")) {
            addition = " " + cmd.args[0];
        }

        if (commands.containsKey(cmd.invoke + addition)) {
            boolean safe = commands.get(cmd.invoke + addition).called();
            if (!safe) {
                commands.get(cmd.invoke + addition).action(cmd.args, cmd.event);
            } /*else {
                commands.get(cmd.invoke + addition).executed(cmd.event);
            }*/
        }
    }
}
