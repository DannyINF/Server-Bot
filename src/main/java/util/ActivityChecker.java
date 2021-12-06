package util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class ActivityChecker {
    public void activity(JDA jda) throws SQLException {
        for (Guild guild : jda.getGuilds()) {
            core.databaseHandler.database(guild.getId(), "update users set activity = activity - 50, recent_activity = 0");

            core.databaseHandler.database(guild.getId(), "update users set activity = 0 where activity < 0");

            String[] answerSize;
            answerSize = core.databaseHandler.database(STATIC.getGuild().getId(), "select count(id) from users where activity > 0");
            assert answerSize != null;
            int starMembers = Integer.parseInt(answerSize[0]) / 5;

            String[] answer;
            try {
                answer = core.databaseHandler.database(STATIC.getGuild().getId(), "select id from users where activity > 1000 and {fn timestampdiff(SQL_TSI_FRAC_SECOND, last_join, CURRENT_DATE)} / 86400000000000 > 7 order by activity desc offset 0 rows fetch next " + starMembers + " rows only");
                for (Member member : guild.getMembersWithRoles(STATIC.getStar())) {
                    try {
                        guild.removeRoleFromMember(member.getId(), STATIC.getStar()[0]).queue();
                    } catch (Exception ignored) {}
                }

                assert answer != null;
                for (String id : answer) {
                    try {
                        guild.addRoleToMember(id, STATIC.getStar()[0]).queue();
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}

            String[] answerNewcomer;
            answerNewcomer = core.databaseHandler.database(STATIC.getGuild().getId(), "select id from users where {fn timestampdiff(SQL_TSI_FRAC_SECOND, last_join, CURRENT_DATE)} / 86400000000000 < 4 and verifystatus = true");
            assert answerNewcomer != null;
            for (String id : answerNewcomer) {
                try {
                    if (!Objects.requireNonNull(guild.getMemberById(id)).getRoles().contains(STATIC.getNewcomer()[0]))
                        guild.addRoleToMember(id, STATIC.getNewcomer()[0]).queue();
                } catch (Exception ignored) {}
            }

            String[] answerMember;
            answerMember = core.databaseHandler.database(STATIC.getGuild().getId(), "select id from users where {fn timestampdiff(SQL_TSI_FRAC_SECOND, last_join, CURRENT_DATE)} / 86400000000000 > 3 and verifystatus = true");
            assert answerMember != null;
            for (String id : answerMember) {
                try {
                    if (Objects.requireNonNull(guild.getMemberById(id)).getRoles().contains(STATIC.getNewcomer()[0]))
                        guild.removeRoleFromMember(id, STATIC.getNewcomer()[0]).queue();
                    if (!Objects.requireNonNull(guild.getMemberById(id)).getRoles().contains(jda.getGuilds().get(0).getRolesByName("Avari", true).get(0)))
                        guild.addRoleToMember(id, jda.getGuilds().get(0).getRolesByName("Avari", true).get(0)).queue();
                } catch (Exception ignored) {}
            }
        }
    }

    public void kickUnverified() throws SQLException {

        TextChannel modlog = STATIC.getModlog();

        String[] answerWarning;
        answerWarning = core.databaseHandler.database(STATIC.getGuild().getId(), "select id from users where {fn timestampdiff(SQL_TSI_FRAC_SECOND, last_join, CURRENT_DATE)} / 86400000000000 = 1 and verifystatus = false");

        String[] answerKick;
        answerKick = core.databaseHandler.database(STATIC.getGuild().getId(), "select id from users where {fn timestampdiff(SQL_TSI_FRAC_SECOND, last_join, CURRENT_DATE)} / 86400000000000 = 3 and verifystatus = false");

        assert answerKick != null;
        for (String id : answerKick) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.RED);
            embed.setTitle("Kick!");
            embed.setDescription("Du hast dich nicht innerhalb von 3 Tagen auf dem Server **" + STATIC.getGuild().getName() + "** verifiziert und wurdest deshalb gekickt. Wenn du wieder joinen willst, findest du hier eine Einladung: " + STATIC.getInvite(STATIC.getGuild()));
            embed.setThumbnail(STATIC.getGuild().getIconUrl());

            EmbedBuilder embed1 = new EmbedBuilder();
            embed1.setColor(Color.RED);
            embed1.setTitle("Kick!");
            embed1.setDescription(Objects.requireNonNull(STATIC.getGuild().getMemberById(id)).getAsMention() + " wurde aufgrund von fehlender Verifizierung gekickt.");
            assert modlog != null;
            modlog. sendMessageEmbeds(embed1.build()).queue();

            Objects.requireNonNull(STATIC.getGuild().getMemberById(id)).getUser().openPrivateChannel().queue(channel -> {
                channel. sendMessageEmbeds(embed.build()).queue();
                STATIC.getGuild().kick(Objects.requireNonNull(STATIC.getGuild().getMemberById(id)), "fehlende Verifizierung").queue();
            });
        }
        assert answerWarning != null;
        for (String id : answerWarning) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.RED);
            embed.setTitle("Vorsicht!");
            embed.setDescription("Aufgrund deiner fehlenden Verifizierung k\u00f6nnte es zeitnah zum Kick vom Server **" + STATIC.getGuild().getName() + "** kommen!");
            embed.setThumbnail(STATIC.getGuild().getIconUrl());

            EmbedBuilder embed1 = new EmbedBuilder();
            embed1.setColor(Color.RED);
            embed1.setTitle("Verwarnung f\u00fcr fehlende Verfizierung");
            embed1.setDescription(Objects.requireNonNull(STATIC.getGuild().getMemberById(id)).getUser().getAsTag() + " wurde aufgrund einer fehlenden Verifizierung verwarnt.");

            assert modlog != null;
            modlog. sendMessageEmbeds(embed1.build()).queue();

            Objects.requireNonNull(STATIC.getGuild().getMemberById(id)).getUser().openPrivateChannel().queue(channel -> channel. sendMessageEmbeds(embed.build()).queue());
        }
    }
}