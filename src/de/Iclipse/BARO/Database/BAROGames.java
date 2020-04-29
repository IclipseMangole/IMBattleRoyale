package de.Iclipse.BARO.Database;

import de.Iclipse.IMAPI.Database.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BAROGames {
    public static void createBAROGamesTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS baro_games (id MEDIUMINT NOT NULL AUTO_INCREMENT, start DATETIME, finish DATETIME, teamsize INT(5), PRIMARY KEY (id))");
    }

    public static void createGame(Date start, Date finish, int teamsize) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MySQL.update("INSERT INTO baro_games (start, finish, teamsize) VALUES ('" + sdf.format(start) + "', '" + sdf.format(finish) + "', " + teamsize + ")");
    }

    public static void deleteGame(int id) {
        MySQL.update("DELETE FROM baro_games WHERE id = " + id);
        MySQL.update("DELETE FROM baro_stats WHERE game = " + id);
    }

    public static int getId(Date start, Date finish) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ResultSet rs = MySQL.querry("SELECT id FROM baro_games WHERE start = '" + sdf.format(start) + "' AND finish = '" + sdf.format(finish) + "'");
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
