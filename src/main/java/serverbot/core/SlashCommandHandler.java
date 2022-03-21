package serverbot.core;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import serverbot.commands.*;

import java.util.Optional;

public class SlashCommandHandler extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;

        try {
            switch (event.getName()) {
                case "2x":
                    Cmd2x.TwoX(event);
                    break;
                case "ban":
                    User user = event.getOption("ban_user").getAsUser();
                    int del_days = (int) event.getOption("ban_del_days").getAsLong();
                    String reason = event.getOption("ban_reason").getAsString();
                    CmdBan.ban(event, user, del_days, reason);
                    break;
                case "botinfo":
                    CmdBotinfo.botinfo(event);
                    break;
                case "kick":
                    CmdKick.kick(event);
                    break;
                case "credits":
                    switch (event.getSubcommandName()) {
                        case "get":
                            CmdCredits.get(event, event.getOption("credits_user").getAsMember());
                            break;
                        case "give":
                            CmdCredits.give(event, event.getOption("credits_give_amount").getAsLong(), event.getOption("credits_give_user").getAsUser());
                            break;
                        case "gift":
                            CmdCredits.gift(event, event.getOption("credits_gift_amount").getAsLong(), event.getOption("credits_gift_user").getAsUser());
                            break;
                    }
                    break;
                case "exil":
                    CmdExil.exile(event, event.getOption("exil_user").getAsMember(), event.getOption("exil_reason"));
                    break;
                case "stats":
                    CmdStats.stats(event, event.getOption("stats_user").getAsMember());
                    break;
                    /*
                    TODO: implement music
                case "music":
                    action(event);
                    */
                default:
                    event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
            }
        } catch (Exception ignored) {
        }

    }
}