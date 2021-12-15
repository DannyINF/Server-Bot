package serverbot.commands;

import serverbot.core.messageActions;
import serverbot.core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.util.getUser;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class cmdBan implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // only members with the ban permission are able to ban using this command
        if (permissionChecker.checkPermission(new Permission[]{Permission.BAN_MEMBERS}, event.getMember())) {
            //TextChannel modlog = STATIC.getModlog();

            // getting the data.user
            ArrayList<String> args2 = new ArrayList<>();
            args2.add(args[0]);
            String[] args3 = new String[args2.size()];
            args3 = args2.toArray(args3);
            Member member = getUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getChannel());
            assert member != null;
            User user = member.getUser();
            int delay = Integer.parseInt(args[1]);

            // building a string with the reason
            StringBuilder sb = new StringBuilder();
            int i = 2;
            while (i < args.length) {
                sb.append(args[i]);
                sb.append(" ");
                i++;
            }
            // setting up and sending the msg for the data.user
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red);
            embed.setTitle(messageActions.getLocalizedString("banned_msg_title", "serverbot/user", event.getAuthor().getId()));
            embed.setDescription("**" + messageActions.getLocalizedString("banned_msg_content",
                    "serverbot/user", event.getAuthor().getId()) + "**\n" + sb.toString());
            user.openPrivateChannel().queue(channel ->
                    channel. sendMessageEmbeds(embed.build()).queue()
            );
            // setting up and sending the msg for the #modlog
            EmbedBuilder embed1 = new EmbedBuilder();
            embed1.setColor(Color.red);
            embed1.setTitle(messageActions.getLocalizedString("banned_msg_title", "serverbot/user", event.getAuthor().getId()));
            embed1.setDescription(messageActions.getLocalizedString("log_mod_ban", "server", event.getGuild().getId())
                    .replace("[USER]", user.getAsTag()).replace("[REASON]", sb.toString()));
            //assert modlog != null;
            //modlog. sendMessageEmbeds(embed1.build()).queue(msg -> event.getGuild().ban(user, delay, sb.toString()).queue());
        } else {
            permissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }
    }
}
