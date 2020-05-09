package de.Iclipse.BARO.Functions.PlayerManagement;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Chests.Chests;
import de.Iclipse.BARO.Functions.Chests.LootDrops;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class UserStats implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onDeathKill(PlayerDeathEvent e) {
        System.out.println("Death");
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getEntity()) != null) {
                User u = User.getUser(e.getEntity());
                if (u.isAlive()) {
                    int deathsNew = u.getDeaths() + 1;
                    u.setDeaths(deathsNew);

                    Player killer = null;
                    if (u.isKnocked()) {
                        killer = u.getKnockedBy();
                    } else {
                        if (System.currentTimeMillis() < Data.lastDamager.get(u.getPlayer()).getLastDamageTime() + 10000) {
                            killer = Data.lastDamager.get(u.getPlayer()).getLastDamager();
                        }
                    }

                    if (killer != null) {
                        User k = User.getUser(killer);
                        System.out.println(k.getKills());
                        int killsNew = k.getKills() + 1;
                        k.setKills(killsNew);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (User.getUser((Player) e.getEntity()) != null) {
                User u = User.getUser((Player) e.getEntity());
                if (u.isAlive()) {
                    u.setDamageReceived(u.getDamageReceived() + ((double) ((int) e.getDamage()) / 2));
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                if (User.getUser((Player) e.getDamager()) != null) {
                    User u = User.getUser((Player) e.getDamager());
                    if (u.isAlive()) {
                        u.setDamageDealt(u.getDamageDealt() + ((double) ((int) e.getDamage()) / 2));
                    }
                }
            }
        }
    }


    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                User u = User.getUser(e.getPlayer());
                if (u.isAlive()) {
                    u.setBlocksDestroyed(u.getBlocksDestroyed() + 1);
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser((Player) e.getPlayer()) != null) {
                User u = User.getUser((Player) e.getPlayer());
                if (u.isAlive()) {
                    u.setBlocksPlaced(u.getBlocksPlaced() + 1);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser((Player) e.getWhoClicked()) != null) {
                User u = User.getUser((Player) e.getWhoClicked());
                if (u.isAlive()) {
                    u.setItemsCrafted(u.getItemsCrafted() + 1);
                }
            }
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (Data.state == GameState.Running) {
            if (Chests.notLootedChests.containsKey(e.getInventory().getLocation())) {
                if (User.getUser((Player) e.getPlayer()) != null) {
                    User u = User.getUser((Player) e.getPlayer());
                    if (u.isAlive()) {
                        u.setLootedChests(u.getLootedChests() + 1);
                    }
                }
            } else if (LootDrops.drops.containsKey(e.getInventory().getLocation())) {
                if (User.getUser((Player) e.getPlayer()) != null) {
                    User u = User.getUser((Player) e.getPlayer());
                    if (u.isAlive()) {
                        u.setLootedDrops(u.getLootedDrops() + 1);
                    }
                }
            }
        }
    }

}
