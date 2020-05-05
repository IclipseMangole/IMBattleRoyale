package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonWater implements Listener {
    public static void poison() {
        if (Data.estate == EventState.PoisonWater) {
            Data.users.forEach(u -> {
                if (u.isAlive()) {
                    if (u.getPlayer().getLocation().getBlock().getType().equals(Material.WATER) || u.getPlayer().getLocation().getBlock().getType().equals(Material.KELP_PLANT)) {
                        u.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 1));
                    }
                }
            });
        }
    }
}
