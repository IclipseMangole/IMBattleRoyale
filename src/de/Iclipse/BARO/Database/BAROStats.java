package de.Iclipse.BARO.Database;

import de.Iclipse.IMAPI.Database.MySQL;
import de.Iclipse.IMAPI.IMAPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    public static int getPosition(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT uuid, (SUM(baro_stats.kills) / SUM(baro_stats.deaths)) AS kd FROM baro_stats GROUP BY uuid ORDER BY kd DESC");
            } else {
                rs = MySQL.querry("SELECT uuid, (SUM(baro_stats.kills) / SUM(baro_stats.deaths)) AS kd FROM baro_stats, baro_games WHERE baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "' GROUP BY uuid ORDER BY kd DESC");
            }
            int i = 1;
            while (rs.next()) {
                if (UUID.fromString(rs.getString("uuid")).equals(uuid)) {
                    return i;
                }
                i++;
            }
            return i + 1; //If no position with uuid
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getKills(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.kills) AS kills FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.kills) AS kills FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("kills");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDeaths(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.deaths) AS deaths FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.deaths) AS deaths FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("deaths");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static double getKD(UUID uuid, boolean all) {
        NumberFormat formatter = new DecimalFormat("#0.0#");
        int deaths = getDeaths(uuid, all) == 0 ? 1 : getDeaths(uuid, all);
        return Double.parseDouble(formatter.format(getKills(uuid, all) / deaths));
    }

    public static int getPlayedGames(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT COUNT(uuid) AS gamesPlayed FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT COUNT(uuid) AS gamesPlayed FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("gamesPlayed");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getWonGames(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT COUNT(uuid) AS gamesPlayed FROM baro_stats WHERE uuid = '" + uuid + "' AND place = " + 1);
            } else {
                rs = MySQL.querry("SELECT COUNT(uuid) AS gamesPlayed FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND place = " + 1 + " AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("gamesPlayed");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getWinProbability(UUID uuid, boolean all) {
        NumberFormat formatter = new DecimalFormat("0.00#");
        int playedGames = getPlayedGames(uuid, all) == 0 ? 1 : getPlayedGames(uuid, all);
        return formatter.format(getWonGames(uuid, all) / playedGames * 100);
    }

    public static String getAveragePlace(UUID uuid, boolean all) {
        NumberFormat formatter = new DecimalFormat("#0.0#");
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.place) AS places FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.place) AS places FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                double playedGames = getPlayedGames(uuid, all);
                return formatter.format((double) rs.getInt("places") / playedGames);
            } else {
                return 0 + "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDamageDealt(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.damageDealt) AS damageDealt FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.damageDealt) AS damageDealt FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("damageDealt");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDamageReceived(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.damageReceived) AS damageReceived FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.damageReceived) AS damageReceived FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("damageReceived");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getBlocksPlaced(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.blocksPlaced) AS blocksPlaced FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.blocksPlaced) AS blocksPlaced FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("blocksPlaced");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getBlocksDestroyed(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.blocksDestroyed) AS blocksDestroyed FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.blocksDestroyed) AS blocksDestroyed FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("blocksDestroyed");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getItemsCrafted(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.itemsCrafted) AS itemsCrafted FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.itemsCrafted) AS itemsCrafted FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("itemsCrafted");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getLootedChests(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.lootedChests) AS lootedChests FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.lootedChests) AS lootedChests FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("lootedChests");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getLootedDrops(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.lootedDrops) AS lootedDrops FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.lootedDrops) AS lootedDrops FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return rs.getInt("lootedDrops");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getAverageLifespan(UUID uuid, boolean all) {
        try {
            ResultSet rs;
            if (all) {
                rs = MySQL.querry("SELECT SUM(baro_stats.playedTime) AS playedTime FROM baro_stats WHERE uuid = '" + uuid + "'");
            } else {
                rs = MySQL.querry("SELECT SUM(baro_stats.playedTime) AS playedTime FROM baro_stats, baro_games WHERE uuid = '" + uuid + "' AND baro_stats.game = baro_games.id AND baro_games.finish > '" + IMAPI.monthBefore() + "'");
            }
            if (rs.next()) {
                return (int) Math.round(((double) rs.getInt("playedTime") / (double) getPlayedGames(uuid, all)) / 60);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
