package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Confusion implements Listener {
    @EventHandler
    public void onEvent(EventChangeEvent e) {
        if (e.getAfter() == EventState.Confusion) {
            Bukkit.getScheduler().runTask(Data.instance, () -> {
                Data.users.forEach(entry -> {
                    if (entry.isAlive()) {
                        entry.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 999999, 1));
                    }
                });
            });
        } else if (e.getBefore() == EventState.Confusion) {
            Bukkit.getScheduler().runTask(Data.instance, () -> {
                Data.users.forEach(entry -> {
                    if (entry.isAlive()) {
                        entry.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
                    }
                });
            });
        }
    }
}
