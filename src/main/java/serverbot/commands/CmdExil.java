package serverbot.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import serverbot.core.PermissionChecker;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.moderation.Moderation;
import serverbot.moderation.ModerationManagement;
import serverbot.moderation.ModerationType;
import serverbot.util.SpringContextUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CmdExil {

    public static void exile(SlashCommandEvent event, Member member, OptionMapping reason) {
        event.deferReply(true);
        if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Mythen aus Mittelerde", true).get(0)}, event.getMember()) ||
                PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("YT-Team", true).get(0)}, event.getMember()) ||
                PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Moderator", true).get(0)}, event.getMember())) {
            event.reply("Ein Nutzer wurde de/exiliert.").queue();
            exileMember(event.getGuild(), member, event.getMember(), -1L, reason == null ? "" : reason.getAsString());
        } else {
            PermissionChecker.noPower(event);
        }
    }

    public static void exileMember(Guild guild, Member member, Member moderator, Long duration, String reason) {
        MemberManagement memberManagement = SpringContextUtils.getBean(MemberManagement.class);
        serverbot.member.Member selectedMember = memberManagement.findById(new MemberId(member.getId(), guild.getId())).get();

        Role exil = guild.getRolesByName("exil", true).get(0);

        if (selectedMember.isExiled()) {
            List<String> rolesFromDB = selectedMember.getRoles().stream().map(serverbot.role.Role::getRoleId).collect(
                    Collectors.toList());
            guild.removeRoleFromMember(member, exil).queue();
            assert rolesFromDB != null;
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
            } catch (Exception ignored) {}
            ModerationManagement moderationManagement = SpringContextUtils.getBean(ModerationManagement.class);
            moderationManagement.save(new Moderation(LocalDateTime.now(), member.getId(), guild.getId(), moderator.getId(),
                    ModerationType.EXIL, duration, false, reason));
            memberManagement.setIsExiled(new MemberId(member.getId(), guild.getId()), true);

        }
    }
}
