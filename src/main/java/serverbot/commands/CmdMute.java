package serverbot.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;
import java.util.Optional;

public class CmdMute {
    public static void mute(SlashCommandInteractionEvent event, AudioChannel audioChannel, Optional<OptionMapping> optionalRole, Optional<OptionMapping> optionalUser) {
        //check if guildChannel is of type voice
        if (!audioChannel.getType().isAudio()) {
            event.reply("Der angegebene Channel muss ein Voicechannel sein!").queue();
            return;
        }

        //check if user in channel are muted
        boolean areMuted = false;
        for (Member member : audioChannel.getMembers()) {
            if (member.getVoiceState().isGuildMuted()) {
                areMuted = true;
                break;
            }
        }

        if (areMuted) {
            event.reply(String.format("Alle Nutzer im Channel **%s** wurden entmuted.", audioChannel.getName())).queue();
            for (Member member : audioChannel.getMembers()) {
                if (member.getVoiceState().isGuildMuted()) {
                    member.mute(false).queue();
                }
            }
        } else {
            event.reply(String.format("Alle Nutzer im Channel **%s** wurden (bis auf Ausnahmen) gemuted.", audioChannel.getName())).queue();
            List<Member> membersInChannel = audioChannel.getMembers();
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