package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Levitation {

    public static void onLevitate() {
        if (Data.estate == EventState.Levitation) {
            Data.users.forEach(user -> {
                if(user.isAlive()){
                    user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20,1));
                }
            });
        }else{
            Data.users.forEach(user -> {
                user.getPlayer().removePotionEffect(PotionEffectType.LEVITATION);
            });
        }
    }
}
