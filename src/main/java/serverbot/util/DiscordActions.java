package serverbot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.moderation.Moderation;
import serverbot.moderation.ModerationManagement;
import serverbot.moderation.ModerationType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscordActions {
    public static void ban(Guild guild, Member member, String message, int delDays) {
        member.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Du wurdest gebannt.").queue(message1 -> {
                guild.ban(member, delDays, message).queue();
            });
        });
    }

    public static void kick(Guild guild, Member member, String message) {
        member.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Du wurdest gekickt.").queue(message1 -> {
                guild.kick(member, message).queue();
            });
        });
    }

    public static void exil(Guild guild, Member member, Member moderator, Long duration, String reason) {
        MemberManagement memberManagement = SpringContextUtils.getBean(MemberManagement.class);
        serverbot.member.Member selectedMember = memberManagement.findById(new MemberId(member.getId(), guild.getId())).get();

        Role exil = guild.getRolesByName("exil", true).get(0);

        if (selectedMember.isExiled()) {
            List<String> rolesFromDB = selectedMember.getRoles().stream().map(serverbot.role.Role::getRoleId).collect(
                    Collectors.toList());
            guild.removeRoleFromMember(member, exil).queue();

            //TODO: catch case where role is not existing anymore
            for (String id : rolesFromDB) {
                guild.addRoleToMember(member, Objects.requireNonNull(guild.getRoleById(id))).queue();
            }
            ModerationManagement moderationManagement = SpringContextUtils.getBean(ModerationManagement.class);
            moderationManagement.save(new Moderation(LocalDateTime.now(), member.getId(), guild.getId(), moderator.getId(),
                    ModerationType.UNEXIL, duration, false, reason));
            memberManagement.setIsExiled(new MemberId(member.getId(), guild.getId()), false);

        } else {
            //TODO: implement duration
            duration = -1L;
            List<String> voiceroles = new ArrayList<>();
            for (VoiceChannel vc : guild.getVoiceChannels()) {
                voiceroles.add(vc.getName().toLowerCase());
            }
            for (Role role : member.getRoles()) {
                if (!voiceroles.contains(role.getName().toLowerCase())) {
                    memberManagement.addRoleToMember(new MemberId(member.getId(), guild.getId()), role.getId());
                }
                guild.removeRoleFromMember(member, role).queue();
            }

            guild.addRoleToMember(member, exil).queue();
            try {
                guild.kickVoiceMember(member).queue();
            } catch (Exception ignored) {
            }
            ModerationManagement moderationManagement = SpringContextUtils.getBean(ModerationManagement.class);
            moderationManagement.save(new Moderation(LocalDateTime.now(), member.getId(), guild.getId(), moderator.getId(),
                    ModerationType.EXIL, duration, false, reason));
            memberManagement.setIsExiled(new MemberId(member.getId(), guild.getId()), true);

        }
    }

    public static void rename(Guild guild, Member member, String newName) {
        guild.modifyNickname(member, newName).queue();
    }
}
