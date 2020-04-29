package de.Iclipse.BARO.Functions.PlayerManagement;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Database.BAROStats;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class User {
    private Player player;
    private int kills;
    private int deaths;
    private double damageDealt;
    private double damageReceived;
    private int blocksPlaced;
    private int blocksDestroyed;
    private int itemsCrafted;
    private int lootedChests;
    private int lootedDrops;
    private int place;
    private long finished;
    private boolean knocked;
    private Player knockedBy;

    public User(Player player) {
        this.player = player;
        kills = 0;
        deaths = 0;
        damageDealt = 0;
        damageReceived = 0;
        blocksPlaced = 0;
        blocksDestroyed = 0;
        itemsCrafted = 0;
        lootedChests = 0;
        lootedDrops = 0;
        place = 0;
        finished = 0;
        knocked = false;
        Data.users.add(this);
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
        System.out.println("setKills");
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public double getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(double damageDealt) {
        this.damageDealt = damageDealt;
    }

    public double getDamageReceived() {
        return damageReceived;
    }

    public void setDamageReceived(double damageReceived) {
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

    public int getItemsCrafted() {
        return itemsCrafted;
    }

    public void setItemsCrafted(int itemsCrafted) {
        this.itemsCrafted = itemsCrafted;
    }

    public int getLootedChests() {
        return lootedChests;
    }

    public void setLootedChests(int lootedChests) {
        this.lootedChests = lootedChests;
    }

    public int getLootedDrops() {
        return lootedDrops;
    }

    public void setLootedDrops(int lootedDrops) {
        this.lootedDrops = lootedDrops;
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

    public boolean isKnocked() {
        return knocked;
    }

    public void setKnocked(boolean knocked, Player knockedBy) {
        this.knocked = knocked;
        this.knockedBy = knockedBy;
    }

    public Player getKnockedBy() {
        return knockedBy;
    }


    public boolean isAlive() {
        return finished == 0;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public Team getTeam() {
        ArrayList<Team> teams = Data.teams;
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
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

    public void save(int gameId) {
        BAROStats.insertStats(UUIDFetcher.getUUID(player.getName()), gameId, kills, deaths, damageDealt, damageReceived, blocksPlaced, blocksDestroyed, itemsCrafted, lootedChests, lootedDrops, place, finished);
    }

}
