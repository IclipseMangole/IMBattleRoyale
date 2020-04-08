package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static de.Iclipse.BARO.Functions.Tablist.scoreboard;

public class Team {

    private org.bukkit.scoreboard.Team team;
    private ChatColor color;
    private ItemStack item;
    private ArrayList<User> users;

    public Team(ChatColor color, ItemStack item, ArrayList<User> users) {
        team = scoreboard.getTeam("Team" + Data.teams.size()) == null ? scoreboard.registerNewTeam("Team" + Data.teams.size()) : scoreboard.getTeam("Team" + Data.teams.size());
        this.color = color;
        this.item = item;
        this.users = users;
        Data.teams.add(this);
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

}
