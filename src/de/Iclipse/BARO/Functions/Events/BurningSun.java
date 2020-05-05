package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.event.Listener;

public class BurningSun implements Listener {

    public static void burn() {
        if (Data.estate == EventState.BurningSun) {
            Data.users.forEach(u -> {
                if (u.isAlive()) {
                    if (u.getPlayer().getLocation().getY() > u.getPlayer().getWorld().getHighestBlockAt(u.getPlayer().getLocation()).getY()) {
                        if (u.getPlayer().getFireTicks() < 20) {
                            u.getPlayer().setFireTicks(20);
                        }
                    }
                }
            });
        }
    }
}
