package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Glowing implements Listener {
    @EventHandler
    public void onEventChange(EventChangeEvent e) {
        if (e.getAfter() == EventState.Glowing) {
            Data.users.forEach(u -> {
                if (u.isAlive()) {
                    u.getPlayer().setGlowing(true);
                }
            });
        } else if (e.getBefore() == EventState.Glowing) {
            Data.users.forEach(u -> {
                if (u.isAlive()) {
                    u.getPlayer().setGlowing(false);
                }
            });
        }
    }
}
