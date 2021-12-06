package core;

import java.sql.*;
import java.util.ArrayList;

public class databaseHandler {

    /**
     * Databases:
     *
     * === without data types ===
     *
     * [SERVERID]
     * - users(id, intro, profile, words, msg, chars, voicetime, verifystatus, activity, recent_activity, first_join, last_join, language, country, sex, xp, level)
     * - reports(report_id, victim_id, offender_id, channel, cause, info, ruling)
     * - applications(id, step, name, age, location, language, about_you, change)
     * - modlog(id, datelog, description, action)
     * - exil(id, roles, duration)
     *
     * === with data types ===
     * [SERVERID]
     * - users(id varchar(20), intro int, profile clob(2000), words bigint, msg bigint, chars bigint, voicetime bigint, verifystatus boolean,
     *   activity bigint, recent_activity bigint, first_join date, last_join date, language varchar(3), country varchar(3), sex varchar(1), xp bigint, level bigint)
     * - reports(report_id varchar(20), victim_id varchar(20), offender_id varchar(20), channel varchar(100), cause varchar(200), info clob(2000), ruling varchar(20))
     * - applications(id varchar(20), step int, name varchar(50), age varchar(20), location varchar(50), language varchar(200), about_you clob(1000), change clob(1000))
     * - modlog(id varchar(20), datelog date, description clob(2000), action varchar(100))
     * - exil(id varchar(20), roles clob(2000), duration int)
     */
    private static Connection conn;

    public static String[] database(String database, String statement_string) throws SQLException {
        databaseHandler app = new databaseHandler();

        app.connectionToDerby(database);
        Statement statement = conn.createStatement();
        if (statement_string.split(" ")[0].toLowerCase().equals("select")) {
            ResultSet resultSet = statement.executeQuery(statement_string);
            ArrayList<String> list = new ArrayList<>();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    list.add(resultSet.getString(i));
                }
            }
            String[] result = new String[list.size()];
            list.toArray(result);

            return result;
        }

        statement.execute(statement_string);
        return null;
    }

    private void connectionToDerby(String database) throws SQLException {
        // -------------------------------------------
        // URL format is
        // jdbc:derby:<local directory to save data>
        // -------------------------------------------
        String dbUrl = "jdbc:derby:Data/" + database + ";create=true";
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        conn = DriverManager.getConnection(dbUrl);
    }
}
