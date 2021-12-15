package serverbot.listeners;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class leaveListener extends ListenerAdapter {


    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        TextChannel welcome = event.getGuild().getDefaultChannel();

        assert welcome != null;
        welcome.sendMessage("Nam\u00E1ri\u00EB " + Objects.requireNonNull(event.getMember()).getAsMention() + " (" + event.getMember().getUser().getAsTag() + ")! Nai autuvaly\u00eb rain\u00eb!").queue();
        /*try {
            STATIC.getRules().retrieveMessageById(747072404328677396L).queue(msg -> msg.removeReaction("\uD83C\uDDEC\uD83C\uDDE7", event.getUser()).queue());
        } catch (Exception ignored) {}
        try {
            STATIC.getRegeln().retrieveMessageById(745992745289777255L).queue(msg -> msg.removeReaction("\uD83C\uDDE9\uD83C\uDDEA", event.getUser()).queue());
        } catch (Exception ignored) {}*/
    }
}
