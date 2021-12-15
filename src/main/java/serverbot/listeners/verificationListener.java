package serverbot.listeners;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class verificationListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        /*if (Objects.equals(STATIC.getRules(), event.getChannel()) || Objects.equals(STATIC.getRegeln(), event.getChannel())) {
            String lang;
            String country;
            if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDEC\uD83C\uDDE7")) {
                lang = "en";
                country = "gb";
                if (!event.getMember().getRoles().contains(STATIC.getExil()[0])) {
                    try {
                        event.getGuild().addRoleToMember(event.getMember(), STATIC.getEnglish()[0]).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDE9\uD83C\uDDEA")) {
                lang = "de";
                country = "de";
                if (!event.getMember().getRoles().contains(STATIC.getExil()[0])) {
                    try {
                        event.getGuild().addRoleToMember(event.getMember(), STATIC.getDeutsch()[0]).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return;
            }
            if (!event.getMember().getRoles().contains(STATIC.getExil()[0])) {
                try {
                    event.getGuild().addRoleToMember(event.getMember(), STATIC.getVerified()[0]).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                databaseHandler.database(STATIC.getGuild().getId(), "update users set verifystatus = TRUE, language = '" + lang + "', country = '" + country + "' where id = '" + event.getUserId() + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        /*if (Objects.equals(STATIC.getRules(), event.getChannel()) || Objects.equals(STATIC.getRegeln(), event.getChannel())) {
            String lang;
            String country;
            if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDEC\uD83C\uDDE7")) {
                lang = "de";
                country = "de";
                if (!Objects.requireNonNull(event.getMember()).getRoles().contains(STATIC.getExil()[0])) {
                    try {
                        event.getGuild().removeRoleFromMember(event.getMember(), STATIC.getEnglish()[0]).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDE9\uD83C\uDDEA")) {
                lang = "en";
                country = "gb";
                if (!Objects.requireNonNull(event.getMember()).getRoles().contains(STATIC.getExil()[0])) {
                    try {
                        event.getGuild().removeRoleFromMember(event.getMember(), STATIC.getDeutsch()[0]).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return;
            }
            if (!event.getMember().getRoles().contains(STATIC.getExil()[0])) {
                try {
                    event.getGuild().removeRoleFromMember(event.getMember(), STATIC.getVerified()[0]).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                databaseHandler.database(STATIC.getGuild().getId(), "update users set verifystatus = TRUE, language = '" + lang + "', country = '" + country + "' where id = '" + event.getUserId() + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
}
