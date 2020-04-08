package de.Iclipse.BARO.Functions;

import java.util.UUID;

public class User {
    private UUID uuid;
    private int kills;
    private int deaths;
    private int damageDealt;
    private int damageReceived;
    private int blocksPlaced;
    private int blocksDestroyed;
    private int place;

    public User(UUID uuid) {
        this.uuid = uuid;
        kills = 0;
        deaths = 0;
        damageDealt = 0;
        damageReceived = 0;
        blocksPlaced = 0;
        blocksDestroyed = 0;
        place = 0;
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
}
