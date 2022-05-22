package serverbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.commands.CmdExil;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.report.Report;
import serverbot.report.ReportManagement;
import serverbot.report.RulingType;
import serverbot.role.RoleManagement;
import serverbot.role.RoleType;
import serverbot.util.SpringContextUtils;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class ModReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        if (channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).toList().contains(channelManagement.findByChannelId(event.getChannel().getId()).get())) {
            if (event.getMember().getUser().isBot())
                return;
            int reactionCount;
            RulingType rulingType;
            switch (event.getReactionEmote().getEmoji()) {
                case "\u21A9":
                    reactionCount = 1;
                    rulingType = RulingType.TROLL;
                    break;
                case "\u2705":
                    reactionCount = 2;
                    rulingType = RulingType.CLOSED;
                    break;
                case "\uD83C\uDFAD":
                    reactionCount = 1;
                    rulingType = RulingType.DISCUSSION;
                    break;
                case "\u2B55":
                    reactionCount = 1;
                    rulingType = RulingType.EXIL;
                    break;
                case "\u26D4":
                    reactionCount = 2;
                    rulingType = RulingType.KICK;
                    break;
                case "\uD83D\uDD28":
                    reactionCount = 3;
                    rulingType = RulingType.BAN;
                    break;
                case "\uD83D\uDD04":
                    reactionCount = 3;
                    rulingType = RulingType.REOPENED;
                    break;
                default:
                    return;
            }
            event.getChannel().retrieveMessageById(event.getMessageId()).queue(msg -> msg.retrieveReactionUsers(event.getReactionEmote().getEmoji()).queue(users -> {
                System.out.println(users.size());
                if (users.size() > reactionCount)
                    executeAction(event, rulingType, msg, users);
            }));

        }
    }

    private static void executeAction(MessageReactionAddEvent event, RulingType rulingType, Message msg, List<User> users) {
        ReportManagement reportManagement = SpringContextUtils.getBean(ReportManagement.class);
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        RoleManagement roleManagement = SpringContextUtils.getBean(RoleManagement.class);
        MemberManagement memberManagement = SpringContextUtils.getBean(MemberManagement.class);
        Report report = reportManagement.findByMessageId(msg.getId()).get();
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
        Member victim = Objects.requireNonNull(event.getGuild().getMemberById(report.getUserId()));
        Member offender = Objects.requireNonNull(event.getGuild().getMemberById(report.getOffenderId()));
        long victim_coins = 0L;
        switch (rulingType) {
            case TROLL:
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.CYAN);
                embed.setTitle("Report bearbeitet: TROLL");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        report.getCause() + "` wurde geschlossen. \nDer Nutzer **" + victim.getUser().getAsTag() + "**" +
                        " wurde verwarnt!\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                EmbedBuilder pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.red);
                pm_victim.setTitle("Verwarnung f\u00fcr Trolling");
                pm_victim.setDescription("Dein Report \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" + report.getCause() + "` wurde als Trolling eingestuft.\n" +
                        "Hiermit wird dir eine Verwarnung ausgesprochen.\n" +
                        "Unterlasse in Zukunft das Erstellen von Trollreports, da sonst zu h\u00e4rteren Strafen gegriffen wird!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                report.setTrollCredits(victim_coins);

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case CLOSED:
                embed = new EmbedBuilder();
                embed.setColor(Color.GREEN);
                embed.setTitle("Report bearbeitet: GESCHLOSSEN");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        report.getCause() + "` wurde geschlossen.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.GREEN);
                pm_victim.setTitle("Dein Report wurde geschlossen!");
                pm_victim.setDescription("Dein Report \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" + report.getCause() + "` wurde geschlossen.");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case DISCUSSION:
                event.getReaction().removeReaction(event.getUser()).queue();
                assert jump != null;
                event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()).sendMessage("@here").queue();
                embed = new EmbedBuilder();
                embed.setColor(Color.YELLOW);
                embed.setTitle("Report bearbeiten: " + event.getMember().getUser().getAsTag());
                embed.setDescription("Seht euch mal diesen [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        report.getCause() + "` an!");
                event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()). sendMessageEmbeds(embed.build()).queue();
                break;
            case EXIL:
                if (offender.getRoles().contains(event.getGuild().getRoleById(roleManagement.findByServerIdAndRoleType(event.getGuild().getId(), RoleType.EXIL).stream().findFirst().get().getRoleId()))) {
                    event.getReaction().removeReaction(event.getUser()).queue();
                    break;
                }
                embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.setTitle("Report bearbeitet: EXIL");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        report.getCause() + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde in das Exil verschoben.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                EmbedBuilder pm_offender = new EmbedBuilder();
                pm_offender.setColor(Color.ORANGE);
                pm_offender.setTitle("Du wurdest in das Exil verschoben!");
                pm_offender.setDescription("Aufgrund von `" + report.getCause() + "` wurdest du auf dem **" + event.getGuild().getName() + "** in das Exil verschoben.\n" +
                        "Im Exilchannel kannst du nun alles Weitere mit den Moderatoren besprechen.");
                offender.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_offender.build()).queue()
                );

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.ORANGE);
                pm_victim.setTitle(offender.getUser().getAsTag() + " wurde in das Exil verschoben!");
                pm_victim.setDescription("Aufgrund deines Reports mit dem Grund `" + report.getCause() + "` wurde **" + offender.getUser().getAsTag() + "** in das Exil verschoben!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                CmdExil.exileMember(event.getGuild(), offender, event.getGuild().getSelfMember(), 0L, "Report");

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case KICK:
                embed = new EmbedBuilder();
                embed.setColor(Color.RED);
                embed.setTitle("Report bearbeitet: KICK");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        report.getCause() + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde vom Server gekickt.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                pm_offender = new EmbedBuilder();
                pm_offender.setColor(Color.red);
                pm_offender.setTitle("Du wurdest gekickt!");
                pm_offender.setDescription("Aufgrund von `" + report.getCause() + "` wurdest du vom **" + event.getGuild().getName() + "** gekickt.\n" +
                        "Du kannst weiterhin dem Server beitreten.");
                offender.getUser().openPrivateChannel().queue(channel -> {
                    channel.sendMessageEmbeds(pm_offender.build()).queue();
                    offender.kick(report.getCause()).queue();
                });

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.red);
                pm_victim.setTitle(offender.getUser().getAsTag() + " wurde gekickt!");
                pm_victim.setDescription("Aufgrund deines Reports mit dem Grund `" + report.getCause() + "` wurde **" + offender.getUser().getAsTag() + "** vom **Mythen aus Mittelerde Discord** gekickt!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case BAN:
                embed = new EmbedBuilder();
                embed.setColor(Color.BLACK);
                embed.setTitle("Report bearbeitet: BANN");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        report.getCause() + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde vom Server gebannt.\n" +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                pm_offender = new EmbedBuilder();
                pm_offender.setColor(Color.red);
                pm_offender.setTitle("Du wurdest gebannt!");
                pm_offender.setDescription("Aufgrund von `" + report.getCause() + "` wurdest du vom **Mythen aus Mittelerde Discord** gebannt.\n" +
                        "Kontaktiere einen Administrator, falls du mehr \u00fcber deine Banstrafe erfahren m\u00f6chtest.");
                offender.getUser().openPrivateChannel().queue(channel -> {
                    channel.sendMessageEmbeds(pm_offender.build()).queue();
                    offender.ban(1, report.getCause()).queue();
                });

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.red);
                pm_victim.setTitle(offender.getUser().getAsTag() + " wurde gebannt!");
                pm_victim.setDescription("Aufgrund deines Reports mit dem Grund `" + report.getCause() + "` wurde **" + offender.getUser().getAsTag() + "** vom **Mythen aus Mittelerde Discord** gebannt!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(pm_victim.build()).queue()
                );

                msg.clearReactions().queue();
                msg.addReaction("\uD83D\uDD04").queue();
                break;
            case REOPENED:
                String revert_ruling = "Die Strafe konnte nicht r\u00fcckg\u00e4ngig gemacht werden.\n";
                String revert_ruling_offender = "Deine Strafe konnte nicht r\u00fcckg\u00e4ngig gemacht werden.\n";
                String revert_ruling_victim = "Deine Strafe konnte nicht r\u00fcckg\u00e4ngig gemacht werden.\n";
                boolean success = false;
                switch (report.getRulingType()) {
                    case TROLL:
                        //TODO: give back coins
                    case CLOSED:
                        revert_ruling = "";
                        revert_ruling_offender = "";
                        revert_ruling_victim = "";
                        success = true;
                        break;
                    case EXIL:
                        if (memberManagement.findById(new MemberId(event.getGuild().getId(), offender.getId())).get().isExiled()) {
                            revert_ruling = "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde aus dem Exil entlassen.\n";
                            revert_ruling_offender = "Du wurdest aus dem Exil entlassen.\n";
                            CmdExil.exileMember(event.getGuild(), offender, event.getGuild().getSelfMember(), 0L, "Revert");
                        } else {
                            revert_ruling = "";
                            revert_ruling_offender = "";
                        }
                        revert_ruling_victim = "";
                        success = true;
                        break;
                    case KICK:
                        if (event.getGuild().getMembers().contains(offender)) {
                            revert_ruling = "";
                            revert_ruling_offender = "";
                        } else {
                            revert_ruling = "Es wurde versucht, dem Nutzer **" + offender.getUser().getAsTag() + "** eine Einladung zum Server zu schicken.\n";
                            revert_ruling_offender = "Du kannst \u00fcber folgende Einladung dem Server beitreten: "; //TODO: add invite
                        }
                        revert_ruling_victim = "";
                        success = true;
                        break;
                    case BAN:
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
                            revert_ruling_offender = "Du" + ban_addition + " kannst \u00fcber folgende Einladung dem Server beitreten: "; //TODO: link invite
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
                        report.getCause() + "` wurde wieder ge\u00f6ffnet.\n" +
                        revert_ruling +
                        "Zust\u00e4ndige Moderatoren: " + stimmen.toString());
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();

                if (!report.getRulingType().equals(RulingType.TROLL)) {
                    pm_offender = new EmbedBuilder();
                    pm_offender.setColor(Color.BLUE);
                    pm_offender.setTitle("Ein Report \u00fcber dich wurde wieder er\u00f6ffnet!");
                    pm_offender.setDescription("Der Report \u00fcber dich mit dem Grund `" + report.getCause() + "` wurde auf dem **" + event.getGuild().getName() + "** wieder er\u00f6ffnet.\n" +
                            revert_ruling_offender);
                    try {
                        offender.getUser().openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(pm_offender.build()).queue());
                    } catch (Exception ignored) {}
                }

                pm_victim = new EmbedBuilder();
                pm_victim.setColor(Color.BLUE);
                pm_victim.setTitle("Dein Report wurde wieder er\u00f6ffnet!");
                pm_victim.setDescription("Dein Report \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" + report.getCause() + "` wurde auf dem **" + event.getGuild().getName() + "** wieder er\u00f6ffnet.\n" +
                        revert_ruling_victim);
                try {
                    victim.getUser().openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(pm_victim.build()).queue());
                } catch (Exception ignored) {}
                break;
        }
        report.setRulingType(rulingType);
        reportManagement.save(report);
    }
}
