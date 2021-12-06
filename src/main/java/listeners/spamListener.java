package listeners;

import com.google.api.client.util.DateTime;
import core.messageActions;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class spamListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        int limit = 100;
        List<Message> list = new ArrayList<>();
        Consumer<Message> consumer = (message) -> {
            if (message.getAuthor().equals(event.getAuthor()) &&
                    (event.getMessage().getTimeCreated().toEpochSecond() - message.getTimeCreated().toEpochSecond()) <= 86400) {
                list.add(message);
            }
        };
        if (limit < 1)
            return;
        MessagePaginationAction action = event.getChannel().getIterableHistory();
        AtomicInteger counter = new AtomicInteger(limit);
        action.forEachAsync( (message)->
        {
            consumer.accept(message);
            // if false the iteration is terminated; else it continues
            return counter.decrementAndGet() == 0;
        }).thenRun(
                () -> {
                    int i = 0;
                    int j = 0;
                    int int_counter = 0;
                    int maxcounter = 0;
                    int size = 0;
                    String string1 = event.getMessage().getContentRaw().toLowerCase();
                    i++;
                    if (string1.length() > 0) {
                        while (i < list.size() && size < 5) {
                            if (!string1.equals(list.get(j).getContentRaw().toLowerCase()))
                                int_counter = 0;
                            else
                                int_counter++;
                            size++;
                            if (int_counter > maxcounter) {
                                maxcounter = int_counter;
                            }
                            i++;
                            j++;
                            if (i == list.size()) {
                                size = 5;
                            }
                        }
                        String topic = "";
                        try {
                            topic = event.getChannel().getTopic();
                        } catch (Exception ignored) {}
                        if (!event.getAuthor().isBot() && !topic.contains("{spam}")) {
                            if (maxcounter > 3) {
                                new commands.cmdBan().action(new String[]{event.getAuthor().getAsMention(), "1",
                                        messageActions.getLocalizedString("spam_reason", "user", event.getAuthor().getId()).replace("[MESSAGE]", string1)}, event);
                            } else if (maxcounter > 2) {
                                MessageBuilder builder = new MessageBuilder();
                                builder.setContent(messageActions.getLocalizedString("spam_warn2", "user", event.getAuthor().getId())
                                        .replace("[USER]", event.getAuthor().getAsMention()));
                                messageActions.selfDestroyMSG(builder.build(), 15000, event);
                            } else if (maxcounter > 1) {
                                MessageBuilder builder = new MessageBuilder();
                                builder.setContent(messageActions.getLocalizedString("spam_warn1", "user", event.getAuthor().getId())
                                        .replace("[USER]", event.getAuthor().getAsMention()));
                                messageActions.selfDestroyMSG(builder.build(), 15000, event);
                            }
                        }
                    }
                }
        );
    }
}
