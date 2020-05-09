package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Levitation implements Listener {

    @EventHandler
    public void onEventChange(EventChangeEvent e) {
        if (e.getAfter() == EventState.Levitation) {
            Bukkit.getScheduler().runTask(Data.instance, () -> {
                Data.users.forEach(user -> {
                    if (user.isAlive()) {
                        user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 9999999, 1));
                    }
                });
            });
        }
        if (e.getBefore() == EventState.Levitation) {
            Bukkit.getScheduler().runTask(Data.instance, () -> {
                Data.users.forEach(user -> {
                    user.getPlayer().removePotionEffect(PotionEffectType.LEVITATION);
                });
            });
        }
    }
}
