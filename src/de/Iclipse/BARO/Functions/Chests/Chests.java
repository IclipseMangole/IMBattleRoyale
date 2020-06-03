package de.Iclipse.BARO.Functions.Chests;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class Chests implements Listener {
    public static ArrayList<Location> notLootedChests = new ArrayList<>();

    @EventHandler
    public void onChestOpen(InventoryOpenEvent e) {
        if (Data.state == GameState.Running) {
            if (notLootedChests.contains(e.getInventory().getLocation())) {
                if (User.getUser((Player) e.getPlayer()) != null) {
                    User u = User.getUser((Player) e.getPlayer());
                    if (u.isAlive()) {
                        notLootedChests.remove(e.getInventory().getLocation());
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
            if (notLootedChests.contains(e.getBlock().getLocation())) {
                notLootedChests.remove(e.getBlock().getLocation());
            }
        }
    }


    public static void loadChests() {
        Random random = new Random();
        while (Data.chests.size() > Data.chestAmount) {
            Location chest = Data.chests.get(random.nextInt(Data.chests.size()));
            chest.getBlock().setType(Material.AIR);
            Data.chests.remove(chest);
        }
        Data.chests.forEach(chest -> {
            notLootedChests.add(chest);
            loadInventory(chest);
        });
    }


    public static void loadInventory(Location loc) {
        Inventory inv = ((Chest) loc.getBlock()).getBlockInventory();
        inv.clear();
        Random random = new Random();
        int itemsAmount = random.nextInt(3) + 5;
        for (int i = 0; i < itemsAmount; i++) {
            int slot = random.nextInt(27);
            while (inv.getItem(slot) != null) {
                slot = random.nextInt(27);
            }
            Item item = Item.getChestItems().get(random.nextInt(Item.getChestItems().size()));
            ItemStack itemStack = item.getItem();
            if (item.getChestMinAmount() != item.getChestMaxAmount()) {
                itemStack.setAmount(random.nextInt(item.getChestMaxAmount() - item.getChestMinAmount()) + item.getChestMinAmount());
            } else {
                itemStack.setAmount(item.getChestMaxAmount());
            }
            inv.setItem(slot, itemStack);
            }
    }


}
