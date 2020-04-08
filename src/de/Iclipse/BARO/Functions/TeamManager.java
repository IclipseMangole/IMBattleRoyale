package de.Iclipse.BARO.Functions;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TeamManager {

    public static void createTeams() {
        new Team(ChatColor.BLACK, blackItem(), new ArrayList<>());
    }

    private static ItemStack blackItem() {
        ItemStack item = new ItemStack(Material.BLACK_CONCRETE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName();
        return item;
    }
}





