package serverbot.util;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import serverbot.server.ServerManagement;
import serverbot.statistics.StatisticsManagement;

public class GiveXP {
    public static void giveXPToMember(Member member, Guild guild, long amount) {
        if (amount != 0) {
            ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);
            StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);

            long xp;
            double userBoost = 1;
            double serverBoost = serverManagement.findById(guild.getId()).get().getXpMultiplier();

            Role nitro = null;

            for (Role r : guild.getRolesByName("Nitro Booster", true)) {
                if (r.isManaged()) {
                    nitro = r;
                }
            }

            if (member.getRoles().contains(nitro)) {
                userBoost += 0.1;
            }

            xp = (long) (serverBoost * userBoost * amount);

            statisticsManagement.addXpToUser(member.getId(), guild.getId(), xp);
        }
    }
}