package de.Iclipse.BARO.Functions.PlayerManagement;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public class UserStats implements Listener {

    @EventHandler
    public void onDeathKill(PlayerDeathEvent e) {
        System.out.println("Death");
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getEntity());
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

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            User u = User.getUser((Player) e.getEntity());
            u.setDamageReceived(u.getDamageReceived() + ((double) ((int) e.getDamage()) / 2));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                User d = User.getUser((Player) e.getDamager());
                d.setDamageDealt(d.getDamageDealt() + ((double) ((int) e.getDamage()) / 2));
            }
        }
    }


    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getPlayer());
            u.setBlocksDestroyed(u.getBlocksDestroyed() + 1);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getPlayer());
            u.setBlocksPlaced(u.getBlocksPlaced() + 1);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser((Player) e.getWhoClicked());
            u.setItemsCrafted(u.getItemsCrafted() + 1);
        }
    }

}
