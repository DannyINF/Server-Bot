package serverbot.listeners;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class applicationListener extends ListenerAdapter {
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        String[] answer = null;
        /*try {
            answer = databaseHandler.database(STATIC.getGuild().getId(), "select * from applications where id = '" + event.getAuthor().getId() + "' and step < 8");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        assert answer != null;
        if (answer.length != 0 && answer[1].length() == 1) {
            switch (answer[1]) {
                case "1":
                    String name;
                    if (event.getMessage().getContentRaw().length() > 50) {
                        event.getChannel().sendMessage(">>> Deine Angabe ist zu lang. Bitte beschr\u00e4nke dich auf 50 Zeichen! (**" + event.getMessage().getContentRaw().length() + "**/50)").queue();
                        return;
                    }
                    name = event.getMessage().getContentRaw().replace("'", "\"");
                    event.getChannel().sendMessage(">>> Wie alt bist du? Ein einfache Jahresanzahl ist hier komplett gen\u00fcgend. (max. 20 Zeichen)").queue();
                    /*try {
                        databaseHandler.database(STATIC.getGuild().getId(), "update applications set step = 2, name = '" + name + "' where id = '" + event.getAuthor().getId() + "' and step = 1");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case "2":
                    if (event.getMessage().getContentRaw().length() > 20) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/20)").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Woher kommst du? Wir brauchen hier keine Adresse, aber ein Bundesland o.\u00c4. w\u00e4re angebracht, damit wir in etwa eine Lageeinordnung haben. (max. 50 Zeichen)").queue();
                    String age = event.getMessage().getContentRaw().replace("'", "\"");
                    /*try {
                        databaseHandler.database(STATIC.getGuild().getId(), "update applications set step = 3, age = '" + age + "' where id = '" + event.getAuthor().getId() + "' and step = 2");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case "3":
                    if (event.getMessage().getContentRaw().length() > 50) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/50)").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Welche Sprachkenntnisse besitzt du? Keine Scheu! Du musst kein Muttersprachler \u00fcberall sein. (max. 200 Zeichen)").queue();
                    /*try {
                        databaseHandler.database(STATIC.getGuild().getId(), "update applications set step = 4, location = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where id = '" + event.getAuthor().getId() + "' and step = 3");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case "4":
                    if (event.getMessage().getContentRaw().length() > 200) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/200)").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Erz\u00e4hl uns mehr \u00fcber dich! Gehst du gerade zur Schule, studieren oder arbeiten oder machst du was ganz anderes?\n" +
                            "Was machst du gerne in deiner Freizeit? Vermittle uns einfach ein Bild von dir. (max. 1.000 Zeichen)").queue();
                    /*try {
                        databaseHandler.database(STATIC.getGuild().getId(), "update applications set step = 5, language = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where id = '" + event.getAuthor().getId() + "' and step = 4");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case "5":
                    if (event.getMessage().getContentRaw().length() > 1000) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/1.000)").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Was m\u00f6chtest du am Server \u00e4ndern? Warum willst du Moderator werden? Erz\u00e4hl uns, was dich motiviert! (max. 1.000 Zeichen)").queue();
                    /*try {
                        databaseHandler.database(STATIC.getGuild().getId(), "update applications set step = 6, about_you = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where id = '" + event.getAuthor().getId() + "' and step = 5");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case "6":
                    if (event.getMessage().getContentRaw().length() > 1000) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/1.000)").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Wenn du deine Bewerbung absenden m\u00f6chtest, dann klicke hier auf den Haken!").queue(msg -> msg.addReaction("\u2705").queue());
                    /*try {
                        databaseHandler.database(STATIC.getGuild().getId(), "update applications set step = 7, change = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where id = '" + event.getAuthor().getId() + "' and step = 6");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    break;
            }
        }
    }

    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        String[] answer = null;
        /*try {
            answer = databaseHandler.database(STATIC.getGuild().getId(), "select * from applications where id = '" + event.getUserId() + "' and step = 7");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        /*assert answer != null;
        if (answer.length != 0 && answer[1].length() == 1) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.BLUE);
            embed.setTitle("Bewerbung");
            //embed.setDescription("Der Nutzer **" + Objects.requireNonNull(STATIC.getGuild().getMemberById(answer[0])).getUser().getAsTag() + "** hat sich als **Moderator** beworben.");
            embed.addField("Name:", answer[2], false);
            embed.addField("Alter:", answer[3], false);
            embed.addField("Herkunft:", answer[4], false);
            embed.addField("Sprachkenntnisse:", answer[5], false);
            embed.addField("Pers\u00f6nliche Angaben:", answer[6], false);
            embed.addField("Motivation:", answer[7], false);
            //STATIC.getModlog(). sendMessageEmbeds(embed.build()).queue(msg -> {
              /* try {
                    databaseHandler.database(STATIC.getGuild().getId(), "delete from applications where id = '" + event.getUserId() + "' and step = 7");
                } catch (SQLException e) {
                    e.printStackTrace();
                }*/
            //});
            //STATIC.getApply().sendMessage(">>> Der Nutzer **" + Objects.requireNonNull(STATIC.getGuild().getMemberById(answer[0])).getUser().getAsTag() + "** hat sich als **Moderator** beworben. Gib `/bewerben` oder `/apply` ein, wenn du dich auch als Moderator bewerben m\u00f6chtest.").queue();
            event.getChannel().sendMessage(">>> Dein Bewerbung wurde abgesendet. Vielen Dank!").queue();
        //}
    }
}