package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class LavaEvent implements Listener {
    //Material, Integer in Object Array
    private static HashMap<Location, Object[]> magmaBlocks = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Data.estate == EventState.LavaEvent) {
            if (e.getPlayer().isOnGround()) {
                if (User.getUser(e.getPlayer()) != null) {
                    if (User.getUser(e.getPlayer()).isAlive()) {
                        Block under = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY() - 1, e.getTo().getBlockZ()).getBlock();
                        Material material = under.getType();
                        Bukkit.getScheduler().runTaskLater(Data.instance, () -> {
                            under.setType(Material.MAGMA_BLOCK);
                            Bukkit.getScheduler().runTaskLater(Data.instance, () -> under.setType(material), 100);
                        }, 50);
                    }
                }
            }
        }
    }

}
