package serverbot.commands;

//import serverbot.core.databaseHandler;
import serverbot.core.PermissionChecker;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.moderation.Moderation;
import serverbot.moderation.ModerationManagement;
import serverbot.moderation.ModerationType;
import serverbot.util.SpringContextUtils;
import serverbot.util.GetUser;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CmdExil implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Mythen aus Mittelerde", true).get(0)}, event.getMember()) ||
            PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("YT-Team", true).get(0)}, event.getMember()) ||
            PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Moderator", true).get(0)}, event.getMember())) {
            if (args.length > 0) {
                Member member = GetUser.getMemberFromInput(Arrays.copyOfRange(args, 0, 1), event.getAuthor(), event.getGuild(), event.getChannel());
                String reason = "";
                if (args.length > 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String string : Arrays.copyOfRange(args, 1, args.length)) {
                        stringBuilder.append(string).append(" ");
                    }
                    reason = stringBuilder.toString();
                }
                exileMember(event.getGuild(), member, event.getMember(), -1L, reason);
            } else {
                event.getChannel().sendMessage("Please provide an user.").queue();
            }
        } else {
            PermissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
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
