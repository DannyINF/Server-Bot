package serverbot.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.util.SpringContextUtils;

import java.util.Objects;

public class leaveListener extends ListenerAdapter {


    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        TextChannel welcome = event.getGuild().getDefaultChannel();

        assert welcome != null;
        welcome.sendMessage("Nam\u00E1ri\u00EB " + Objects.requireNonNull(event.getMember()).getAsMention() + " (" + event.getMember().getUser().getAsTag() + ")! Nai autuvaly\u00eb rain\u00eb!").queue();

        MemberManagement memberManagement = SpringContextUtils.getBean(MemberManagement.class);
        for (Role role : event.getMember().getRoles()) {
            memberManagement.addRoleToMember(new MemberId(event.getMember().getId(), event.getGuild().getId()), role.getId());
        }
    }
}
