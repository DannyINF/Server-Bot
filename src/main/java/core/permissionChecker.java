package core;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class permissionChecker {
    public static boolean checkPermission(Permission[] permission, Member member) {
        boolean hasPermission = true;
        for (Permission perm : permission) {
            if (!member.hasPermission(perm)) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }
    public static boolean checkRole(Role[] roles, Member member) {
        boolean hasRole = true;
        for (Role role : roles) {
            if (!member.getRoles().contains(role)) {
                hasRole = false;
            }
        }
        return hasRole;
    }

    public static void noPower(TextChannel textChannel, Member member) {
        textChannel.sendMessage(member.getAsMention() + " https://giphy.com/gifs/RX3vhj311HKLe").queue();
    }
}
