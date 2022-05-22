package serverbot.commands;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;
import java.util.Optional;

public class CmdMute {
    public static void mute(SlashCommandEvent event, GuildChannel guildChannel, Optional<OptionMapping> optionalRole, Optional<OptionMapping> optionalUser) {
        //check if guildChannel is of type voice
        if (!guildChannel.getType().isAudio()) {
            event.reply("Der angegebene Channel muss ein Voicechannel sein!").queue();
            return;
        }

        //check if user in channel are muted
        boolean areMuted = false;
        for (Member member : guildChannel.getMembers()) {
            if (member.getVoiceState().isGuildMuted()) {
                areMuted = true;
                break;
            }
        }

        if (areMuted) {
            event.reply(String.format("Alle Nutzer im Channel **%s** wurden entmuted.", guildChannel.getName())).queue();
            for (Member member : guildChannel.getMembers()) {
                if (member.getVoiceState().isGuildMuted()) {
                    member.mute(false).queue();
                }
            }
        } else {
            event.reply(String.format("Alle Nutzer im Channel **%s** wurden (bis auf Ausnahmen) gemuted.", guildChannel.getName())).queue();
            List<Member> membersInChannel = guildChannel.getMembers();
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get().getAsRole();
                membersInChannel.removeAll(event.getGuild().getMembersWithRoles(role));
            }
            if (optionalUser.isPresent()) {
                User user = optionalUser.get().getAsUser();
                membersInChannel.remove(event.getGuild().getMember(user));
            }
            for (Member member : membersInChannel) {
                member.mute(true).queue();
            }
        }
    }
}