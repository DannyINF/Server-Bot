package serverbot.commands;

//import serverbot.core.databaseHandler;
import serverbot.core.permissionChecker;
import serverbot.util.getUser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class cmdActivity implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            Member member = getUser.getMemberFromInput(args, event.getAuthor(), event.getGuild(), event.getChannel());
            int activity;
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

            assert member != null;
            /*String[] data = databaseHandler.database(event.getGuild().getId(), "select activity from users where id = '" + member.getId() + "'");
            activity = Integer.parseInt(Objects.requireNonNull(data)[0]);

            event.getChannel().sendMessage("**" + member.getUser().getAsTag() + "** besitzt eine Aktivit\u00e4t von **" + numberFormat.format(activity) + "**.").queue();*/
        } else {
            permissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }
    }
}
