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

    private org.bukkit.scoreboard.Team team;
    private ChatColor color;
    private ArrayList<User> users;
    Material material;

    public Team(ChatColor color, Material material) {
        team = scoreboard.getTeam("Team" + Data.teams.size()) == null ? scoreboard.registerNewTeam("Team" + Data.teams.size()) : scoreboard.getTeam("Team" + Data.teams.size());
        team.setColor(color);
        this.color = color;
        this.users = new ArrayList<>();
        this.material = material;
        Data.teams.add(this);
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
        users.add(u);
        u.getTeam().removeUser(u);
    }

    public boolean isMember(User u) {
        return users.contains(u);
    }


    public ItemStack getTeamItem(Player p) {
        ItemStack item = new ItemStack(this.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.getColor() + "Team " + dsp.get("color." + this.getColor().asBungee().getName(), p) + "(" + this.getUsers() + "|" + Data.teamsize + ")");
        List<String> lores = new ArrayList<>();
        lores.add("");
        for (int i = 0; i < teamsize; i++) {
            lores.add("§7» " + (this.getUsers().get(i) == null ? "-" : this.getUsers().get(i).getPlayer().getDisplayName()));
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
