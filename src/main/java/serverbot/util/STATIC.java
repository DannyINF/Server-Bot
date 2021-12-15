package serverbot.util;

import lombok.Getter;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.util.*;

@Getter
public class STATIC {

    public static final String VERSION = "v1.1.1";

    public static final String PREFIX = "/";

    public static final String BOT_ID = "393375474056953856";

    public static final String SEASON = "0";

    public static boolean is2x = false;

    public static boolean toggle2x() {
        is2x = !is2x;
        return is2x;
    }

    // CHANNELS

    private static TextChannel MODLOG;

    private static TextChannel SPAM;

    // ROLES

    private static Role[] VERIFIED;

    private static Role[] EXIL;

    private static int announcement = 0;

    private static Role[] DEUTSCH;

    private static Role[] NEWCOMER;

    // CHANNELS

    private static TextChannel REGELN;

    private static TextChannel APPLY;

    // GUILD

    private static Guild GUILD;


    private static final List<Pair<String, Integer>> commandSpammer = new ArrayList<>();

    public static int addCommandSpammer(String id) {
        int index = 0;
        try {
            for (Pair pair : commandSpammer) {
                if (pair.getLeft().equals(id)) {
                    int uses = (int) pair.getRight() + 1;
                    if (uses >= 3)
                        uses = 0;
                    commandSpammer.remove(index);
                    commandSpammer.add(Pair.of(id, uses));
                    return uses;
                }
                index++;
            }
        } catch (Exception ignored) {}

        commandSpammer.add(Pair.of(id, 1));
        return 1;
    }

    public static String getInvite(Guild guild) {
        String url = null;
        for (Invite inv : guild.retrieveInvites().complete())
            if (!inv.isTemporary() && Objects.equals(inv.getInviter(), Objects.requireNonNull(guild.getOwner()).getUser()))
                url = inv.getUrl();

        if (url == null)
            url = guild.retrieveInvites().complete().get(0).getUrl();
        return url;
    }
}