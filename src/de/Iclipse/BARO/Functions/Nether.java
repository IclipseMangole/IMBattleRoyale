package de.Iclipse.BARO.Functions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class Nether implements Listener {
    @EventHandler
    public void onPortal(EntityPortalEvent e) {
        e.setCancelled(true);
    }
}
