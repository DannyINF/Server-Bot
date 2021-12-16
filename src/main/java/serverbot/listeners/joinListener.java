package serverbot.listeners;

//import serverbot.core.databaseHandler;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.role.RoleManagement;
import serverbot.role.RoleType;
import serverbot.user.User;
import serverbot.user.UserManagement;
import serverbot.util.SpringContextUtils;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;

import static java.lang.Boolean.FALSE;


public class joinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (!event.getMember().getUser().isBot()) {
            UserManagement userManagement = SpringContextUtils.getBean(UserManagement.class);
            RoleManagement roleManagement = SpringContextUtils.getBean(RoleManagement.class);
            Optional<User> userOptional = userManagement.findById(event.getMember().getId());

            if (userOptional.isPresent()) {
                if (userOptional.get().isExiled()) {
                    event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(
                            roleManagement.findByServerIdAndRoleType(event.getGuild().getId(), RoleType.EXIL).stream()
                                    .findFirst().get().getId())).queue();
                }
            } else {
                userManagement.save(new User(event.getMember().getId(), Locale.GERMAN, false));
            }
        }
    }
}
