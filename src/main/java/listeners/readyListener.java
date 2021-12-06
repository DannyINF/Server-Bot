package listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

public class readyListener extends ListenerAdapter {

    private long members;

    public void onReady(@NotNull ReadyEvent event) {

        StringBuilder out = new StringBuilder("\nThis bot is running on following servers: \n");

        for (Guild g : event.getJDA().getGuilds()) {
            if(!g.getId().equals("492379705748750337")) {
                g.leave().queue();
            } else {
                members += g.getMembers().size();
                out.append(g.getName()).append(" (").append(g.getId()).append(")  ").append("Nutzeranzahl: ").append(g.getMembers().size()).append("\n");
            }
        }

        System.out.println(out);
        System.out.println("\nInsgesamte Nutzeranzahl: " + members);
    }
}