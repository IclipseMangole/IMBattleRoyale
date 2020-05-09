package de.Iclipse.BARO.Functions.Chests;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Chests implements Listener {
    public static HashMap<Location, HashMap<Integer, ItemStack>> notLootedChests = new HashMap<>();

    @EventHandler
    public void onChestOpen(InventoryOpenEvent e) {
        if (Data.state == GameState.Running) {
            if (notLootedChests.containsKey(e.getInventory().getLocation())) {
                if (User.getUser((Player) e.getPlayer()) != null) {
                    User u = User.getUser((Player) e.getPlayer());
                    if (u.isAlive()) {
                        notLootedChests.get(e.getInventory().getLocation()).forEach((index, item) -> {
                            e.getInventory().setItem(index, item);
                        });
                        notLootedChests.remove(e.getInventory().getLocation());
                        return;
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onChestDestroy(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.CHEST)) {
            if (notLootedChests.containsKey(e.getBlock().getLocation())) {
                notLootedChests.get(e.getBlock().getLocation()).forEach((index, item) -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), item));
            }
        }
    }


    public static void loadChests() {
        ArrayList<Location> chests = loadChestList();
        Random random = new Random();
        while (chests.size() > Data.chests) {
            Location chest = chests.get(random.nextInt(chests.size()));
            chest.getBlock().setType(Material.AIR);
            chests.remove(chest);
        }
        chests.forEach(chest -> notLootedChests.put(chest, loadInventory()));
    }

    public static ArrayList<Location> loadChestList() {
        ArrayList<Location> chestlist = new ArrayList<>();
        chestlist.add(new Location(Bukkit.getWorld("world"), -136, 79, 131));
        chestlist.add(new Location(Bukkit.getWorld("world"), -137, 79, 129));
        chestlist.add(new Location(Bukkit.getWorld("world"), 95, 80, 67));
        chestlist.add(new Location(Bukkit.getWorld("world"), 90, 80, 71));
        chestlist.add(new Location(Bukkit.getWorld("world"), 154, 79, 91));
        chestlist.add(new Location(Bukkit.getWorld("world"), 353, 65, 83));
        chestlist.add(new Location(Bukkit.getWorld("world"), 351, 65, 60));
        chestlist.add(new Location(Bukkit.getWorld("world"), 244, 71, 204));
        chestlist.add(new Location(Bukkit.getWorld("world"), 244, 76, 195));
        chestlist.add(new Location(Bukkit.getWorld("world"), 155, 69, 241));
        chestlist.add(new Location(Bukkit.getWorld("world"), 134, 81, 238));
        chestlist.add(new Location(Bukkit.getWorld("world"), 124, 67, 233));
        chestlist.add(new Location(Bukkit.getWorld("world"), -79, 65, 352));
        chestlist.add(new Location(Bukkit.getWorld("world"), -60, 72, 295));
        chestlist.add(new Location(Bukkit.getWorld("world"), -33, 64, 306));
        chestlist.add(new Location(Bukkit.getWorld("world"), -242, 92, 231));
        chestlist.add(new Location(Bukkit.getWorld("world"), -241, 80, 231));
        chestlist.add(new Location(Bukkit.getWorld("world"), -244, 74, 229));
        chestlist.add(new Location(Bukkit.getWorld("world"), -306, 65, 106));
        chestlist.add(new Location(Bukkit.getWorld("world"), -302, 64, 110));
        chestlist.add(new Location(Bukkit.getWorld("world"), -360, 63, -74));
        chestlist.add(new Location(Bukkit.getWorld("world"), -364, 72, -71));
        chestlist.add(new Location(Bukkit.getWorld("world"), -349, 63, -67));
        chestlist.add(new Location(Bukkit.getWorld("world"), -175, 63, -266));
        chestlist.add(new Location(Bukkit.getWorld("world"), -190, 66, -259));
        chestlist.add(new Location(Bukkit.getWorld("world"), -154, 83, -83));
        chestlist.add(new Location(Bukkit.getWorld("world"), -158, 83, -86));
        chestlist.add(new Location(Bukkit.getWorld("world"), -168, 82, -90));
        chestlist.add(new Location(Bukkit.getWorld("world"), 337, 65, 34));
        chestlist.add(new Location(Bukkit.getWorld("world"), 296, 69, -65));
        chestlist.add(new Location(Bukkit.getWorld("world"), 298, 65, -63));
        chestlist.add(new Location(Bukkit.getWorld("world"), 211, 74, -145));
        chestlist.add(new Location(Bukkit.getWorld("world"), 212, 71, -145));
        chestlist.add(new Location(Bukkit.getWorld("world"), 226, 70, -151));
        chestlist.add(new Location(Bukkit.getWorld("world"), 199, 70, -166));
        chestlist.add(new Location(Bukkit.getWorld("world"), 199, 74, -157));
        chestlist.add(new Location(Bukkit.getWorld("world"), 216, 70, -165));
        chestlist.add(new Location(Bukkit.getWorld("world"), 211, 73, -165));
        chestlist.add(new Location(Bukkit.getWorld("world"), 178, 64, -342));
        chestlist.add(new Location(Bukkit.getWorld("world"), 178, 62, -339));
        chestlist.add(new Location(Bukkit.getWorld("world"), 193, 53, -371));
        chestlist.add(new Location(Bukkit.getWorld("world"), -5, 63, -35));
        chestlist.add(new Location(Bukkit.getWorld("world"), -10, 70, -41));
        chestlist.add(new Location(Bukkit.getWorld("world"), -56, 81, -108));
        chestlist.add(new Location(Bukkit.getWorld("world"), -51, 92, -124));
        chestlist.add(new Location(Bukkit.getWorld("world"), -58, 77, -120));
        chestlist.add(new Location(Bukkit.getWorld("world"), -306, 78, -124));
        chestlist.add(new Location(Bukkit.getWorld("world"), 16, 64, 331));
        chestlist.add(new Location(Bukkit.getWorld("world"), 234, 70, 112));
        chestlist.add(new Location(Bukkit.getWorld("world"), 297, 64, 157));
        chestlist.add(new Location(Bukkit.getWorld("world"), 86, 99, -32));
        chestlist.add(new Location(Bukkit.getWorld("world"), 89, 88, -33));
        chestlist.add(new Location(Bukkit.getWorld("world"), -17, 80, 47));
        chestlist.add(new Location(Bukkit.getWorld("world"), -22, 80, 49));
        chestlist.add(new Location(Bukkit.getWorld("world"), 14, 125, 176));
        chestlist.add(new Location(Bukkit.getWorld("world"), 13, 96, 197));
        chestlist.add(new Location(Bukkit.getWorld("world"), 1, 109, 165));
        chestlist.add(new Location(Bukkit.getWorld("world"), 29, 88, 181));
        chestlist.add(new Location(Bukkit.getWorld("world"), -211, 81, 45));
        chestlist.add(new Location(Bukkit.getWorld("world"), -205, 79, 50));
        return chestlist;
    }

    public static HashMap<Integer, ItemStack> loadInventory() {
        HashMap<Integer, ItemStack> inv = new HashMap<>();
        Random random = new Random();
        int itemsAmount = random.nextInt(3) + 5;
        for (int i = 0; i < itemsAmount; i++) {
            int slot = random.nextInt(27);
            while (inv.containsKey(slot)) {
                slot = random.nextInt(27);
            }
            Item item = Item.getChestItems().get(random.nextInt(Item.getChestItems().size()));
            ItemStack itemStack = item.getItem();
                if (item.getChestMinAmount() != item.getChestMaxAmount()) {
                    itemStack.setAmount(random.nextInt(item.getChestMaxAmount() - item.getChestMinAmount()) + item.getChestMinAmount());
                } else {
                    itemStack.setAmount(item.getChestMaxAmount());
                }
                inv.put(slot, itemStack);
            }
        return inv;
    }


}
