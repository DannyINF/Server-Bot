package commands;

import core.permissionChecker;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.getUser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class cmdExil implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Mythen aus Mittelerde", true).get(0)}, event.getMember()) ||
            permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("YT-Team", true).get(0)}, event.getMember()) ||
            permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Moderator", true).get(0)}, event.getMember())) {
            if (args.length > 0) {
                Member member = getUser.getMemberFromInput(args, event.getAuthor(), event.getGuild(), event.getChannel());
                exileMember(event.getGuild(), member);
            } else {
                event.getChannel().sendMessage("Please provide an user.").queue();
            }
        } else {
            permissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }

    }
    public static void exileMember(Guild guild, Member member) throws SQLException {
        String[] answer = core.databaseHandler.database(guild.getId(), "select id from exil");
        
        Role exil = guild.getRolesByName("exil", true).get(0);
        assert member != null;
        assert answer != null;
        boolean isExil = false;
        for (String str : answer) {
            if (str.equals(member.getId()))
                isExil = true;
        }
        if (isExil) {
            String[] rolesFromDB = core.databaseHandler.database(guild.getId(), "select roles from exil where id = '" + member.getId() + "'");
            guild.removeRoleFromMember(member, exil).queue();
            assert rolesFromDB != null;
            for (String id : rolesFromDB[0].split(",")) {
                guild.addRoleToMember(member, Objects.requireNonNull(guild.getRoleById(id))).queue();
            }
            core.databaseHandler.database(guild.getId(), "delete from exil where id = '" + member.getId() + "'");
        } else {
            //TODO: implement duration
            int duration = -1;
            StringBuilder sb = new StringBuilder();
            List<String> voiceroles = new ArrayList<>();
            for (VoiceChannel vc : guild.getVoiceChannels()) {
                voiceroles.add(vc.getName().toLowerCase());
            }
            for (Role role : member.getRoles()) {
                if (!voiceroles.contains(role.getName().toLowerCase())) {
                    sb.append(role.getId());
                    sb.append(",");
                }
                guild.removeRoleFromMember(member, role).queue();
            }
            sb.deleteCharAt(sb.length()-1);

            core.databaseHandler.database(guild.getId(), "insert into exil (id, roles, duration) values ('" + member.getId() + "', '" + sb.toString() + "', " + duration + ")");

            guild.addRoleToMember(member, exil).queue();
            try {
                guild.kickVoiceMember(member).queue();
            } catch (Exception ignored) {}

        }
    }
}
