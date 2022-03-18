package serverbot.listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class ModReactionListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getChannel().getId().equals("640662809343950860")) {
            if (event.getMember().getUser().isBot())
                return;
            int reactionCount;
            String mode;
            switch (event.getReactionEmote().getEmoji()) {
                case "\u21A9":
                    reactionCount = 1;
                    mode = "troll";
                    break;
                case "\u2705":
                    reactionCount = 2;
                    mode = "nothing";
                    break;
                case "\uD83C\uDFAD":
                    reactionCount = 1;
                    mode = "discussion";
                    break;
                case "\u2B55":
                    reactionCount = 1;
                    mode = "exil";
                    break;
                case "\u26D4":
                    reactionCount = 2;
                    mode = "kick";
                    break;
                case "\uD83D\uDD28":
                    reactionCount = 3;
                    mode = "ban";
                    break;
                case "\uD83D\uDD04":
                    reactionCount = 3;
                    mode = "revert";
                    break;
                default:
                    return;
            }
            event.getChannel().retrieveMessageById(event.getMessageId()).queue(msg -> msg.retrieveReactionUsers(event.getReactionEmote().getEmoji()).queue(users -> {
                System.out.println(users.size());
                if (users.size() > reactionCount)
                    executeAction(event, mode, msg, users);
            }));

        }
    }

    private static void executeAction(GuildMessageReactionAddEvent event, String mode, Message msg, List<User> users) {
        /*String[] answer = null;
        try {
            answer = databaseHandler.database(event.getGuild().getId(), "select * from reports where report_id = '" + msg.getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        URL jump = null;
        try {
            jump = new URL("https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessageId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder stimmen = new StringBuilder();
        for (User user : users) {
            if (!user.isBot()) {
                stimmen.append(user.getAsTag());
                stimmen.append(", ");
            }
        }
        stimmen.deleteCharAt(stimmen.length()-2);
        Member victim = Objects.requireNonNull(event.getGuild().getMemberById(answer[1]));
        Member offender = Objects.requireNonNull(event.getGuild().getMemberById(answer[2]));
        long victim_coins = 0L;
        switch (mode) {
            case "troll":
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.CYAN);
                embed.setTitle("Report bearbeitet: TROLL");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen. \nDer Nutzer **" + victim.getUser().getAsTag() + "**" +
                        " wurde verwarnt!\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                EmbedBuilder pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.red);
                pm_victim.setTitle("Verwarnung f\u00fcr Trolling");
                pm_victim.setDescription("Dein Report \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" + answer[4] + "` wurde als Trolling eingestuft.\n" +
                        "Hiermit wird dir eine Verwarnung ausgesprochen.\n" +
                        "Unterlasse in Zukunft das Erstellen von Trollreports, da sonst zu h\u00e4rteren Strafen gegriffen wird!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                mode = mode + "#" + victim_coins;

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case "nothing":
                embed = new EmbedBuilder();
                embed.setColor(Color.GREEN);
                embed.setTitle("Report bearbeitet: GESCHLOSSEN");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.GREEN);
                pm_victim.setTitle("Dein Report wurde geschlossen!");
                pm_victim.setDescription("Dein Report \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" + answer[4] + "` wurde geschlossen.");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case "discussion":
                event.getReaction().removeReaction(event.getUser()).queue();
                assert jump != null;
                //ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
                event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()).sendMessage(
                        //event.getGuild().getRolesByName("YT-Team", true).get(0).getAsMention()).queue();
                embed = new EmbedBuilder();
                embed.setColor(Color.YELLOW);
                embed.setTitle("Report bearbeiten: " + event.getMember().getUser().getAsTag());
                embed.setDescription("Seht euch mal diesen [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` an!");
                //ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
                event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()). sendMessageEmbeds(embed.build()).queue();
                break;
            case "exil":
                if (offender.getRoles().contains(STATIC.getExil()[0])) {
                    event.getReaction().removeReaction(event.getUser()).queue();
                    break;
                }
                embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.setTitle("Report bearbeitet: EXIL");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde in das Exil verschoben.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                EmbedBuilder pm_offender = new EmbedBuilder();
                pm_offender.setColor(Color.ORANGE);
                pm_offender.setTitle("Du wurdest in das Exil verschoben!");
                pm_offender.setDescription("Aufgrund von `" + answer[4] + "` wurdest du auf dem **Mythen aus Mittelerde Discord** in das Exil verschoben.\n" +
                        "Im Exilchannel kannst du nun alles Weitere mit den Moderatoren besprechen.");
                offender.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_offender.build()).queue()
                );

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.ORANGE);
                pm_victim.setTitle(offender.getUser().getAsTag() + " wurde in das Exil verschoben!");
                pm_victim.setDescription("Aufgrund deines Reports mit dem Grund `" + answer[4] + "` wurde **" + offender.getUser().getAsTag() + "** in das Exil verschoben!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                try {
                    cmdExil.exileMember(event.getGuild(), offender);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case "kick":
                embed = new EmbedBuilder();
                embed.setColor(Color.RED);
                embed.setTitle("Report bearbeitet: KICK");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde vom Server gekickt.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                pm_offender = new EmbedBuilder();
                pm_offender.setColor(Color.red);
                pm_offender.setTitle("Du wurdest gekickt!");
                pm_offender.setDescription("Aufgrund von `" + answer[4] + "` wurdest du vom **Mythen aus Mittelerde Discord** gekickt.\n" +
                        "Du kannst weiterhin dem Server beitreten.");
                String[] finalAnswer1 = answer;
                offender.getUser().openPrivateChannel().queue(channel -> {
                    channel.sendMessageEmbeds(pm_offender.build()).queue();
                    offender.kick(finalAnswer1[4]).queue();
                });

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.red);
                pm_victim.setTitle(offender.getUser().getAsTag() + " wurde gekickt!");
                pm_victim.setDescription("Aufgrund deines Reports mit dem Grund `" + answer[4] + "` wurde **" + offender.getUser().getAsTag() + "** vom **Mythen aus Mittelerde Discord** gekickt!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case "ban":
                embed = new EmbedBuilder();
                embed.setColor(Color.BLACK);
                embed.setTitle("Report bearbeitet: BANN");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde vom Server gebannt.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                pm_offender = new EmbedBuilder();
                pm_offender.setColor(Color.red);
                pm_offender.setTitle("Du wurdest gebannt!");
                pm_offender.setDescription("Aufgrund von `" + answer[4] + "` wurdest du vom **Mythen aus Mittelerde Discord** gebannt.\n" +
                        "Kontaktiere einen Administrator, falls du mehr \u00fcber deine Banstrafe erfahren m\u00f6chtest.");
                String[] finalAnswer = answer;
                offender.getUser().openPrivateChannel().queue(channel -> {
                    channel.sendMessageEmbeds(pm_offender.build()).queue();
                    offender.ban(1, finalAnswer[4]).queue();
                });

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.red);
                pm_victim.setTitle(offender.getUser().getAsTag() + " wurde gebannt!");
                pm_victim.setDescription("Aufgrund deines Reports mit dem Grund `" + answer[4] + "` wurde **" + offender.getUser().getAsTag() + "** vom **Mythen aus Mittelerde Discord** gebannt!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case "revert":
                String revert_ruling = "Die Strafe konnte nicht r\u00fcckg\u00e4ngig gemacht werden.\n";
                String revert_ruling_offender = "Deine Strafe konnte nicht r\u00fcckg\u00e4ngig gemacht werden.\n";
                String revert_ruling_victim = "Deine Strafe konnte nicht r\u00fcckg\u00e4ngig gemacht werden.\n";
                boolean success = false;
                switch (answer[6].split("#")[0]) {
                    case "troll":
                    case "nothing":
                        revert_ruling = "";
                        revert_ruling_offender = "";
                        revert_ruling_victim = "";
                        success = true;
                        break;
                    case "exil":
                        String[] exil_ids = null;
                        try {
                            exil_ids = databaseHandler.database(event.getGuild().getId(), "select id from exil");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        boolean isExiled = false;
                        assert exil_ids != null;
                        for (String id : exil_ids) {
                            if (id.equals(offender.getId())) {
                                isExiled = true;
                                break;
                            }
                        }
                        if (isExiled) {
                            revert_ruling = "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde aus dem Exil entlassen.\n";
                            revert_ruling_offender = "Du wurdest aus dem Exil entlassen.\n";
                            try {
                                cmdExil.exileMember(event.getGuild(), offender);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            revert_ruling = "";
                            revert_ruling_offender = "";
                        }
                        revert_ruling_victim = "";
                        success = true;
                        break;
                    case "kick":
                        if (event.getGuild().getMembers().contains(offender)) {
                            revert_ruling = "";
                            revert_ruling_offender = "";
                        } else {
                            revert_ruling = "Es wurde versucht, dem Nutzer **" + offender.getUser().getAsTag() + "** eine Einladung zum Server zu schicken.\n";
                            revert_ruling_offender = "Du kannst \u00fcber folgende Einladung dem Server beitreten: " + STATIC.getInvite(event.getGuild());
                        }
                        revert_ruling_victim = "";
                        success = true;
                        break;
                    case "ban":
                        if (event.getGuild().getMembers().contains(offender)) {
                            revert_ruling = "";
                            revert_ruling_offender = "";
                        } else {
                            String ban_addition = "";
                            boolean isBanned = false;
                            for (Guild.Ban ban : event.getGuild().retrieveBanList().complete())
                                if (ban.getUser().equals(offender.getUser())) {
                                    isBanned = true;
                                    break;
                                }
                            if (isBanned) {
                                ban_addition = " wurdest entbannt und";
                                event.getGuild().unban(offender.getUser()).queue();
                            }
                            revert_ruling = "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde entbannt und es wurde versucht, ihm eine Einladung zum Server zu schicken.\n";
                            revert_ruling_offender = "Du" + ban_addition + " kannst \u00fcber folgende Einladung dem Server beitreten: " + STATIC.getInvite(event.getGuild());
                        }
                        revert_ruling_victim = "";
                        success = true;
                        break;
                }
                if (!success)
                    break;
                msg.clearReactions().queue();
                msg.addReaction("\u21A9").queue();
                msg.addReaction("\u2705").queue();
                msg.addReaction("\uD83C\uDFAD").queue();
                msg.addReaction("\u2B55").queue();
                msg.addReaction("\u26D4").queue();
                msg.addReaction("\uD83D\uDD28").queue();
                embed = new EmbedBuilder();
                embed.setColor(Color.BLUE);
                embed.setTitle("Report bearbeitet: ER\u00d6FFNET");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde wieder ge\u00f6ffnet.\n" +
                        revert_ruling +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                if (!answer[6].split("#")[0].equals("troll")) {
                    pm_offender = new EmbedBuilder();
                    pm_offender.setColor(Color.BLUE);
                    pm_offender.setTitle("Ein Report \u00fcber dich wurde wieder er\u00f6ffnet!");
                    pm_offender.setDescription("Der Report \u00fcber dich mit dem Grund `" + answer[4] + "` wurde auf dem **Mythen aus Mittelerde Discord** wieder er\u00f6ffnet.\n" +
                            revert_ruling_offender);
                    try {
                        offender.getUser().openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(pm_offender.build()).queue());
                    } catch (Exception ignored) {}
                }

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.BLUE);
                pm_victim.setTitle("Dein Report wurde wieder er\u00f6ffnet!");
                pm_victim.setDescription("Dein Report \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" + answer[4] + "` wurde auf dem **Mythen aus Mittelerde Discord** wieder er\u00f6ffnet.\n" +
                        revert_ruling_victim);
                try {
                    victim.getUser().openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(pm_victim.build()).queue());
                } catch (Exception ignored) {}
                break;
        }
        try {
            databaseHandler.database(event.getGuild().getId(), "update reports set ruling = '" + mode + "' where report_id = '" + msg.getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}
