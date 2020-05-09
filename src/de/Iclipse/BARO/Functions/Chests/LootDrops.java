package de.Iclipse.BARO.Functions.Chests;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static de.Iclipse.BARO.Data.dsp;

public class LootDrops implements Listener {
    public static HashMap<Location, Boolean> drops = new HashMap<>();

    public static void lootDropMovement() {
        Iterator it = drops.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry item = (Map.Entry) it.next();
            boolean looted = (boolean) item.getValue();
            Location loc = (Location) item.getKey();
            if (!looted) {
                if (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType() == Material.AIR) {
                    loc.getBlock().setType(Material.AIR);
                    loc.subtract(0, 1, 0);
                    loc.getBlock().setType(Material.CHEST);
                }
            } else {
                it.remove();
            }
        }
    }

    public static void newDrops() {
        Random random = new Random();
        if (random.nextInt(150) == 0) {
            spawnDrop();
        }
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent e) {
        if (drops.containsKey(e.getInventory().getLocation())) {
            if (!drops.get(e.getInventory().getLocation())) {
                if (User.getUser((Player) e.getPlayer()) != null) {
                    User u = User.getUser((Player) e.getPlayer());
                    u.setLootedDrops(u.getLootedDrops() + 1);
                }
                e.getInventory().clear();
                loadInventory().forEach((slot, item) -> {
                    e.getInventory().setItem(slot, item);
                });
                unsendBeacon(e.getInventory().getLocation());
                sendParticlesAround(e.getInventory().getLocation());
                Bukkit.getOnlinePlayers().forEach(p -> dsp.send(p, "drop.looted"));
                drops.remove(e.getInventory().getLocation());
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    entry.playSound(entry.getLocation(), Sound.BLOCK_BELL_RESONATE, 1, 1.0f);
                });
            }
        }
    }


    public static void spawnDrop() {
        Location loc = randomLocationInZone();
        Bukkit.getOnlinePlayers().forEach(o -> {
            dsp.send(o, "drop.spawned");
        });
        sendBeacon(loc);
        drops.put(loc, false);
        Bukkit.getOnlinePlayers().forEach(entry -> {
            entry.playSound(entry.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1.0f);
        });
    }

    public static void sendBeacon(Location loc) {
        Location change = new Location(loc.getWorld(), loc.getBlockX(), loc.getWorld().getHighestBlockYAt(loc), loc.getBlockZ());
        while (change.getBlockY() > 3) {
            sendBlockChange(change, Bukkit.createBlockData(Material.GLASS));
            change.subtract(0, 1, 0);
        }
        sendBlockChange(change, Bukkit.createBlockData(Material.BLUE_STAINED_GLASS));
        change.subtract(0, 1, 0);
        sendBlockChange(change, Bukkit.createBlockData(Material.BEACON));
        change.subtract(1, 1, 1);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                sendBlockChange(change, Bukkit.createBlockData(Material.IRON_BLOCK));
                change.add(0, 0, 1);
            }
            change.add(1, 0, -3);
        }
    }

    public static void unsendBeacon(Location loc) {
        Location change = new Location(loc.getWorld(), loc.getBlockX(), 3, loc.getBlockZ());
        sendBlockChange(change, Bukkit.createBlockData(Material.BLACK_STAINED_GLASS));
        Bukkit.getScheduler().runTaskLater(Data.instance, new Runnable() {
            @Override
            public void run() {
                change.subtract(1, 2, 1);
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        resetBlockChange(change);
                        change.add(0, 0, 1);
                    }
                    change.add(1, 0, -3);
                }
                change.setY(1);
                change.setX(loc.getBlockX());
                change.setY(loc.getBlockZ());
                while (change.getBlockY() < loc.getBlockY()) {
                    resetBlockChange(change);
                    change.add(0, 1, 0);
                }
            }
        }, 10);
    }

    public static void sendParticlesAround(Location loc) {
        Random random = new Random();
        loc.subtract(1, 1, 1);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    if (random.nextInt(3) == 0) {
                        loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 1);
                    }
                    loc.add(0, 0, 1);
                }
                loc.add(0, 1, -3);
            }
            loc.add(1, -3, 0);
        }
    }

    public static void sendBlockChange(Location loc, BlockData data) {
        //System.out.println(loc + ", " + data.getMaterial().toString());
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }
        Bukkit.getOnlinePlayers().forEach(p -> p.sendBlockChange(loc, data));
    }

    public static void resetBlockChange(Location loc) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendBlockChange(loc, loc.getBlock().getBlockData()));
    }

    public static Location randomLocationInZone() {
        Random random = new Random();
        int radius = random.nextInt((int) BorderManager.border.getCurrentRadius());
        System.out.println(radius);
        int angle = random.nextInt(360);
        System.out.println(angle);
        int x = (int) (Math.sin(angle) * radius);
        int z = (int) (Math.cos(angle) * radius);
        Location loc = new Location(Bukkit.getWorld("world"), x, Bukkit.getWorld("world").getHighestBlockYAt(x, z) + 50, z);
        if (!drops.containsKey(loc)) {
            return loc;
        } else {
            return randomLocationInZone();
        }
    }


    public static HashMap<Integer, ItemStack> loadInventory() {
        HashMap<Integer, ItemStack> inv = new HashMap<>();
        Random random = new Random();
        int itemsAmount = random.nextInt(2) + 3;
        for (int i = 0; i < itemsAmount; i++) {
            int slot = random.nextInt(27);
            while (inv.containsKey(slot)) {
                slot = random.nextInt();
            }
            Item item = Item.getDropItems().get(random.nextInt(Item.getDropItems().size()));
            ItemStack itemStack = item.getItem();
            if (item.getDropMinAmount() != item.getDropMaxAmount()) {
                itemStack.setAmount(random.nextInt(item.getDropMaxAmount() - item.getDropMinAmount()) + item.getDropMinAmount());
            } else {
                itemStack.setAmount(item.getDropMaxAmount());
            }
            inv.put(slot, itemStack);
        }
        return inv;
    }

}
