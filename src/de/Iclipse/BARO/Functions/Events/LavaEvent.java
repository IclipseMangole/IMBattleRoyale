package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
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
                        if (!magmaBlocks.containsKey(under.getLocation())) {
                            Object[] array = {under.getType(), Data.timer};
                            magmaBlocks.put(under.getLocation(), array);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEventChange(EventChangeEvent e) {
        if (e.getBefore() == EventState.LavaEvent) {
            magmaBlocks.forEach((loc, array) -> {
                loc.getBlock().setType((Material) array[0]);
            });
            magmaBlocks = new HashMap<>();
        }
    }

    public static void lava() {
        if (Data.estate == EventState.LavaEvent) {
            ArrayList<Location> toRemove = new ArrayList<>();
            magmaBlocks.forEach((loc, array) -> {
                if (Data.timer == (int) array[1] + 7) {
                    toRemove.add(loc);
                    loc.getBlock().setType((Material) array[0]);
                } else if (Data.timer == (int) array[1] + 2) {
                    loc.getBlock().setType(Material.MAGMA_BLOCK);
                }
            });
            toRemove.forEach(entry -> magmaBlocks.remove(entry));
        }
    }
}
