package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Glowing implements Listener {
    @EventHandler
    public void onEventChange(EventChangeEvent e) {
        if (e.getAfter() == EventState.Glowing) {
            Data.users.forEach(u -> {
                if (u.isAlive()) {
                    Bukkit.getScheduler().runTask(Data.instance, () -> u.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10000, 1)));
                }
            });
        } else if (e.getBefore() == EventState.Glowing) {
            Data.users.forEach(u -> {
                if (u.isAlive()) {
                    Bukkit.getScheduler().runTask(Data.instance, () -> u.getPlayer().removePotionEffect(PotionEffectType.GLOWING));
                }
            });
        }
    }
}
