package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Jumppad implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location below = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY() - 1, e.getTo().getBlockZ());
        if (below.getBlock().getType().equals(Material.SLIME_BLOCK)) {
            Data.jumppads.put(e.getPlayer(), e.getPlayer().getInventory().getChestplate());

        }
    }
}
