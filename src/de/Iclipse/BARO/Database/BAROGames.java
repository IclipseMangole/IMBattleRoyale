package de.Iclipse.BARO.Database;

import de.Iclipse.IMAPI.Database.MySQL;

import java.time.LocalDateTime;

public class BAROGames {
    public static void createBAROGamesTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS baro_games (id MEDIUMINT NOT NULL AUTO_INCREMENT, start DATETIME, finish DATETIME, teamsize INT(5), PRIMARY KEY (id))");
    }

    public static void createGame(LocalDateTime start, LocalDateTime finish, int teamsize) {
        MySQL.update("INSERT INTO baro_games (start, finish, teamsize) VALUES ('" + start + "', '" + finish + "', " + teamsize + ")");
    }

    public static void deleteGame(int id) {
        MySQL.update("DELETE FROM baro_games WHERE id = " + id);
        MySQL.update("DELETE FROM baro_stats WHERE game = " + id);
    }

}
