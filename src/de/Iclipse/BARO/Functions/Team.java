package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.BARO.Data.teamsize;
import static de.Iclipse.BARO.Functions.Tablist.scoreboard;

public class Team {

    private int id;
    private org.bukkit.scoreboard.Team team;
    private ChatColor color;
    private ArrayList<User> users;
    Material material;

    public Team(ChatColor color, Material material) {
        this.id = Data.teams.size();
        team = scoreboard.getTeam("Team" + id) == null ? scoreboard.registerNewTeam("Team" + id) : scoreboard.getTeam("Team" + id);
        team.setColor(color);
        team.setPrefix(color + "");
        team.setColor(color);
        this.color = color;
        this.users = new ArrayList<>();
        this.material = material;
        Data.teams.add(this);
    }

    public int getId() {
        return id;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void removeUser(User u) {
        users.remove(u);
    }

    public void addUser(User u) {
        if (u.getTeam() != null) {
            u.getTeam().removeUser(u);
        }
        users.add(u);
        scoreboard.getTeams().forEach(entry -> {
            entry.removeEntry(u.getPlayer().getName());
        });
        team.addEntry(u.getPlayer().getName());
    }

    public boolean isMember(User u) {
        return users.contains(u);
    }

    public org.bukkit.scoreboard.Team getTeam() {
        return team;
    }


    public ItemStack getTeamItem(Player p) {
        ItemStack item = new ItemStack(this.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.getColor() + "Team " + dsp.get("color." + this.getColor().asBungee().getName(), p) + "§7(" + this.getUsers().size() + "|" + Data.teamsize + ")");
        List<String> lores = new ArrayList<>();
        lores.add("");
        for (int i = 0; i < teamsize; i++) {
            String spielername;
            if (this.getUsers().size() > i) {
                spielername = this.getUsers().get(i).getPlayer().getDisplayName();
            } else {
                spielername = "-";
            }
            lores.add("§7» " + spielername);
        }
        lores.add("");
        lores.add(dsp.get("color.click", p, this.getColor() + "Team " + dsp.get("color." + this.getColor().asBungee().getName(), p)));
        meta.setLore(lores);
        this.getUsers().forEach(entry -> {
            if (entry.getPlayer().equals(p)) {
                meta.addEnchant(Enchantment.LUCK, 1, false);
            }
        });
        item.setItemMeta(meta);
        return item;
    }

}
