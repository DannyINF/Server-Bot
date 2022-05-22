package serverbot.core;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import serverbot.role.Role;
import serverbot.role.RoleManagement;
import serverbot.role.RoleType;
import serverbot.util.SpringContextUtils;

public class PermissionChecker {
    public static boolean checkPermission(Permission[] permission, Member member) {
        boolean hasPermission = true;
        for (Permission perm : permission) {
            if (!member.hasPermission(perm)) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }
    public static boolean checkRole(RoleType roleType, Member member) {
        RoleManagement roleManagement = SpringContextUtils.getBean(RoleManagement.class);

        boolean hasRole = false;
        for (Role role : roleManagement.findByServerIdAndRoleType(member.getGuild().getId(), roleType)) {
            if (member.getRoles().contains(member.getGuild().getRoleById(role.getRoleId()))) {
                hasRole = true;
                break;
            }
        }
        return hasRole;
    }

    public static void noPower(TextChannel textChannel, Member member) {
        textChannel.sendMessage(member.getAsMention() + " https://giphy.com/gifs/RX3vhj311HKLe").queue();
    }

    public static void noPower(SlashCommandEvent event) {
        event.reply("https://giphy.com/gifs/RX3vhj311HKLe").queue();
    }
}
