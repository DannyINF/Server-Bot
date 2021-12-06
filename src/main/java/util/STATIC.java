package util;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.util.*;

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

    public static void setModlog(TextChannel channel) {
        MODLOG = channel;
    }

    public static TextChannel getModlog() {
        return MODLOG;
    }

    private static TextChannel SPAM;

    public static void setSpam(TextChannel channel) {
        SPAM = channel;
    }

    public static TextChannel getSpam() {
        return SPAM;
    }

    // ROLES

    private static Role[] VERIFIED;

    public static void setVerified(Role[] role) {
        VERIFIED = role;
    }

    public static Role[] getVerified() {
        return VERIFIED;
    }

    private static Role[] EXIL;

    public static void setExil(Role[] role) {
        EXIL = role;
    }

    public static Role[] getExil() {
        return EXIL;
    }

    private static int announcement = 0;

    public static void changeAnnouncement(int gain) {
        announcement += gain;
    }

    public static int getAnnouncement() {
        return announcement;
    }

    private static Role[] DEUTSCH;

    public static void setDeutsch(Role[] role) {
        DEUTSCH = role;
    }

    public static Role[] getDeutsch() {
        return DEUTSCH;
    }

    private static Role[] ENGLISH;

    public static void setEnglish(Role[] role) {
        ENGLISH = role;
    }

    public static Role[] getEnglish() {
        return ENGLISH;
    }

    private static Role[] STAR;

    public static void setStar(Role[] role) {
        STAR = role;
    }

    public static Role[] getStar() {
        return STAR;
    }

    private static Role[] NEWCOMER;

    public static void setNewcomer(Role[] role) {
        NEWCOMER = role;
    }

    public static Role[] getNewcomer() {
        return NEWCOMER;
    }

    // CHANNELS

    private static TextChannel RULES;

    public static void setRules(TextChannel channel) {
        RULES = channel;
    }

    public static TextChannel getRules() {
        return RULES;
    }

    private static TextChannel REGELN;

    public static void setRegeln(TextChannel channel) {
        REGELN = channel;
    }

    public static TextChannel getRegeln() {
        return REGELN;
    }

    private static TextChannel APPLY;

    public static void setApply(TextChannel channel) {
        APPLY = channel;
    }

    public static TextChannel getApply() {
        return APPLY;
    }

    // GUILD

    private static Guild GUILD;

    public static void setGuild(Guild guild) {
        GUILD = guild;
    }

    public static Guild getGuild() {
        return GUILD;
    }



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