package de.Iclipse.BARO.Database;

import de.Iclipse.IMAPI.Database.MySQL;

import java.util.UUID;

public class BAROStats {

    public static void createBAROStatsTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS baro_stats (id MEDIUMINT NOT NULL AUTO_INCREMENT, uuid VARCHAR(64), game INT(10), kills INT(10), deaths INT(10), damageDealt DOUBLE, damageReceived DOUBLE, blocksPlaced INT(6), blocksDestroyed INT(5), itemsCrafted INT(5), lootedChests INT(5), lootedDrops INT(5), place INT(6), playedTime BIGINT, PRIMARY KEY (id))");
    }

    public static void insertStats(UUID uuid, int game, int kills, int deaths, double damageDealt, double damageReceived, int blocksPlaced, int blocksDestroyed, int itemsCrafted, int lootedChests, int lootedDrops, int place, long playedtime) {
        MySQL.update("INSERT INTO baro_stats (uuid, game, kills, deaths, damageDealt, damageReceived, blocksPlaced, blocksDestroyed, itemsCrafted, lootedChests, lootedDrops, place, playedTime) VALUES ('" + uuid + "', " + game + ", " + kills + ", " + deaths + ", " + damageDealt + ", " + damageReceived + ", " + blocksPlaced + ", " + blocksDestroyed + ", " + itemsCrafted + ", " + lootedChests + ", " + lootedDrops + ", " + place + ", " + playedtime + ")");
    }

    public static void deleteStats(UUID uuid) {
        MySQL.update("DELETE FROM baro_stats WHERE uuid = '" + uuid + "'");
    }

    public static void deleteStats(int game) {
        MySQL.update("DELETE FROM baro_stats WHERE game = " + game);
    }

}
