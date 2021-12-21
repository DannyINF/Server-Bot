package serverbot.util;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;

public class LevelChecker {
    public static long checker(Member member, Guild guild) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        Statistics statistics = statisticsManagement.findByUserIdAndServerId(member.getId(), guild.getId()).get();
        long xp = statistics.getXp();
        long level;

        if (xp <= 50000) {
            level = (int) (Math.sqrt(3 * xp + 10000) - 100) / 6;
        } else {
            level = (xp + 25000) / 1500;
        }

        String role;

        if (level > 549)
            role = "Vanyar";
        else if (level > 349)
            role = "Noldor";
        else if (level > 199)
            role = "Falmari";
        else if (level > 99)
            role = "Sindar";
        else if (level > 49)
            role = "Nandor";
        else
            role = "Avari";

        if (!member.getRoles().contains(guild.getRolesByName(role, true).get(0))) {
            String[] role_names = {"Vanyar", "Noldor", "Falmari", "Sindar", "Nandor", "Avari"};
            for (String role_name : role_names) {

                if (member.getRoles().contains(guild.getRolesByName(role_name, true).get(0))) {
                    try {
                        guild.removeRoleFromMember(member, guild
                                .getRolesByName(role_name, true).get(0)).queue();
                    } catch (Exception ignored) {
                    }
                }

                try {
                    guild.addRoleToMember(member, guild
                            .getRolesByName(role, true).get(0)).queue();
                } catch (Exception ignored) {
                }

            }
        }
        return level;
    }

    public static Long nextLevel(Long xp) {
        long xpToLevel;
        long level;
        long xp2;
        if (xp <= 50000L) {
            level = (int) (Math.sqrt(3 * xp + 10000) - 100) / 6;
        } else {
            level = (xp + 25000) / 1500;
        }

        level++;

        if (level <= 50L) {
            xp2 = ((((level * 6) + 100) * ((level * 6) + 100)) - 10000) / 3;
        } else {
            xp2 = level * 1500 - 25000;
        }

        xpToLevel = xp2 - xp;

        return xpToLevel;
    }

    public static String[] nextRank(Long xp) {
        long xpToRank;
        long level;
        long xp2;
        String rank;

        if (xp <= 50000L) {
            level = (long) (Math.ceil((Math.sqrt((3 * xp) + 10000) - 100) / 6));
        } else {
            level = (xp + 25000) / 1500;
        }


        if (level < 50) {
            rank = "Avari";
            level = 50;
        } else if (level < 100) {
            rank = "Nandor";
            level = 100;
        } else if (level < 200) {
            rank = "Sindar";
            level = 200;
        } else if (level < 350) {
            rank = "Falmari";
            level = 350;
        } else if (level < 550) {
            rank = "Noldor";
            level = 550;
        } else {
            rank = "H\u00f6chster Rang erreicht!";
            level = -1;
        }

        if (level < 0L)
            xpToRank = -1;
        else {
            if (level == 50)
                xp2 = 50000L;
            else
                xp2 = level * 1500 - 25000;
            xpToRank = xp2 - xp;
        }

        return new String[]{String.valueOf(xpToRank), rank};
    }
}
