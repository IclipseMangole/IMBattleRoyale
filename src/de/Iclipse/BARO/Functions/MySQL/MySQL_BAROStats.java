package de.Iclipse.BARO.Functions.MySQL;

import de.Iclipse.IMAPI.Functions.MySQL.MySQL;

import java.util.UUID;

public class MySQL_BAROStats {

    public static void createBAROStatsTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS baro_stats (id MEDIUMINT NOT NULL AUTO_INCREMENT, uuid VARCHAR(64), game INT(10), kills INT(10), deaths INT(10), damageDealt INT(5), damageReceived INT(5), blocksPlaced INT(6), blocksDestroyed(5), place INT(6), playedtime BIGINT, PRIMARY KEY (id)))");
    }

    public void insertStats(UUID uuid, int game, int kills, int deaths, int damageDealt, int damageReceived, int blocksPlaced, int blocksDestroyed, int place, long playedtime) {
        MySQL.update("INSERT INTO baro_stats (uuid, game, kills, deaths, damageDealt, damageReceived, blocksPlaced, blocksDestroyed, place, playedtime) VALUES ('" + uuid + "', " + game + ", " + kills + ", " + deaths + ", " + damageDealt + ", " + damageReceived + ", " + blocksPlaced + ", " + blocksDestroyed + ", " + place + ", " + playedtime + ")");
    }

    public void deleteStats(UUID uuid) {
        MySQL.update("DELETE FROM baro_stats WHERE uuid = '" + uuid + "'");
    }

    public void deleteStats(int game) {
        MySQL.update("DELETE FROM baro_stats WHERE game = " + game);
    }

}
