package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.entity.Player;

public class User {
    private Player player;
    private int kills;
    private int deaths;
    private int damageDealt;
    private int damageReceived;
    private int blocksPlaced;
    private int blocksDestroyed;
    private int place;
    private long finished;

    public User(Player player) {
        this.player = player;
        kills = 0;
        deaths = 0;
        damageDealt = 0;
        damageReceived = 0;
        blocksPlaced = 0;
        blocksDestroyed = 0;
        place = 0;
        finished = 0;
    }

    public static User getUser(Player player) {
        for (User user : Data.users) {
            if (user.getPlayer().equals(player)) {
                return user;
            }
        }
        return null;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public int getDamageReceived() {
        return damageReceived;
    }

    public void setDamageReceived(int damageReceived) {
        this.damageReceived = damageReceived;
    }

    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public int getBlocksDestroyed() {
        return blocksDestroyed;
    }

    public void setBlocksDestroyed(int blocksDestroyed) {
        this.blocksDestroyed = blocksDestroyed;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public Player getPlayer() {
        return player;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public Team getTeam() {
        for (Team team : Data.teams) {
            if (team.isMember(this)) {
                return team;
            }
        }
        return null;
    }

    public boolean isInATeam() {
        for (Team team : Data.teams) {
            if (team.isMember(this)) {
                return true;
            }
        }
        return false;
    }
}
