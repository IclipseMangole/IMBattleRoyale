package de.Iclipse.BARO.Functions.Chests;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
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
    public static HashMap<Location, Boolean> chests = new HashMap<>();
    public static HashMap<Location, HashMap<Integer, ItemStack>> chestInventorys = new HashMap<>();

    @EventHandler
    public void onChestOpen(InventoryOpenEvent e) {
        if (chests.containsKey(e.getInventory().getLocation())) {
            if (!chests.get(e.getInventory().getLocation())) {
                if (User.getUser((Player) e.getPlayer()) != null) {
                    User u = User.getUser((Player) e.getPlayer());
                    u.setLootedChests(u.getLootedChests() + 1);
                }
                e.getInventory().clear();
                chestInventorys.get(e.getInventory().getLocation()).forEach((slot, item) -> {
                    e.getInventory().setItem(slot, item);
                });
                chests.replace(e.getInventory().getLocation(), true);
            }
        }
    }

    @EventHandler
    public void onChestDestroy(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.CHEST)) {
            if (chests.containsKey(e.getBlock().getLocation())) {
                e.setCancelled(true);
            }
        }
    }


    public static void loadChests() {
        HashMap<Location, BlockFace> list = loadChestList();
        //HashMap<Location, BlockFace> list = new HashMap<>();
        if (list.size() > 0) {
            ArrayList<Location> locs = new ArrayList<>();
            list.forEach((chest, facing) -> {
                locs.add(chest);
                if (chest.getWorld().getBlockAt(chest).getType().equals(Material.CHEST)) {
                    chest.getWorld().getBlockAt(chest).setType(Material.AIR);
                } else {
                    System.out.println("No chest at: " + chest);
                }
            });
            Random random = new Random();
            for (int i = 0; i < Data.chests && list.size() > 0; i++) {
                Location chest = locs.get(random.nextInt(locs.size() - 1));
                chests.put(chest, false);
                chest.getBlock().setType(Material.CHEST);
                if (chest.getBlock().getState() instanceof Directional) {
                    ((Directional) chest.getBlock().getState()).setFacing(list.get(chest));
                }
                list.remove(chest);
            }
            loadInventorys();
        } else {
            System.out.println("No Chests");
        }
    }

    public static HashMap<Location, BlockFace> loadChestList() {
        HashMap<Location, BlockFace> chestlist = new HashMap<>();
        chestlist.put(new Location(Bukkit.getWorld("world"), -136, 79, 131), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -137, 79, 129), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), 95, 80, 67), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), 90, 80, 71), BlockFace.NORTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), 154, 79, 91), BlockFace.EAST);
        chestlist.put(new Location(Bukkit.getWorld("world"), 353, 65, 83), BlockFace.NORTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), 351, 65, 60), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), 244, 71, 204), BlockFace.EAST);
        chestlist.put(new Location(Bukkit.getWorld("world"), 244, 76, 195), BlockFace.EAST);
        chestlist.put(new Location(Bukkit.getWorld("world"), 155, 69, 241), BlockFace.WEST);
        chestlist.put(new Location(Bukkit.getWorld("world"), 134, 81, 238), BlockFace.WEST);
        chestlist.put(new Location(Bukkit.getWorld("world"), 124, 67, 233), BlockFace.WEST);
        chestlist.put(new Location(Bukkit.getWorld("world"), -79, 65, 352), BlockFace.WEST);
        chestlist.put(new Location(Bukkit.getWorld("world"), -60, 72, 295), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -33, 64, 306), BlockFace.WEST);
        chestlist.put(new Location(Bukkit.getWorld("world"), -242, 92, 231), BlockFace.EAST);
        chestlist.put(new Location(Bukkit.getWorld("world"), -241, 80, 231), BlockFace.EAST);
        chestlist.put(new Location(Bukkit.getWorld("world"), -244, 74, 229), BlockFace.NORTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -306, 65, 106), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -302, 64, 110), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -360, 63, -74), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -364, 72, -71), BlockFace.EAST);
        chestlist.put(new Location(Bukkit.getWorld("world"), -349, 63, -67), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -175, 63, -266), BlockFace.NORTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -190, 66, -259), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -154, 83, -83), BlockFace.WEST);
        chestlist.put(new Location(Bukkit.getWorld("world"), -158, 83, -86), BlockFace.SOUTH);
        chestlist.put(new Location(Bukkit.getWorld("world"), -168, 82, -90), BlockFace.NORTH);

        return chestlist;
    }

    public static void loadInventorys() {
        chests.forEach((loc, looted) -> {
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
            chestInventorys.put(loc, inv);
        });
    }


}
