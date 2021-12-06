package util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.SQLException;

public class giveXP {
    public static void giveXPToMember(Member member, Guild guild, long amount) throws SQLException {
        if (amount != 0) {
            long xp;
            double userboost = 1; // Double-XP Event
            double channelboost = 1;
            double serverboost = 1;

            if (STATIC.is2x)
                userboost = 2;

            Role nitro = null;

            for (Role r : guild.getRolesByName("Nitro Booster", true)) {
                if (r.isManaged()) {
                    nitro = r;
                }
            }

            if (member.getRoles().contains(nitro)) {
                userboost = 1.5;
            }

            xp = (long) (serverboost * channelboost * userboost * amount);

            core.databaseHandler.database(guild.getId(), "update users set xp = xp + " + xp + " where id = '" + member.getId() + "'");
        }
    }
}