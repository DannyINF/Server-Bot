package serverbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class cmdTalk implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getId().equals("277746420281507841")) {
            StringBuilder sb = new StringBuilder();
            for (String str : args) {
                sb.append(str);
                sb.append(" ");
            }
            event.getChannel().sendMessage(sb.toString()).queue();
        }
    }


}
