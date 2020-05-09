package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Lostness implements Listener {
    @EventHandler
    public void onEvent(EventChangeEvent e) {
        if (e.getAfter() == EventState.Lostness) {
            Bukkit.getScheduler().runTask(Data.instance, () -> {
                Data.users.forEach(u -> {
                    if (u.isAlive()) {
                        u.getPlayer().setHealthScale(20.0); //TODO Hungerbalken usw.
                        u.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 1));
                    }
                });
            });
        } else if (e.getBefore() == EventState.Lostness) {
            Data.users.forEach(u -> {
                if (u.isAlive()) {
                    u.getPlayer().setHealthScale(u.getPlayer().getHealth());
                }
            });
        }
    }
}
