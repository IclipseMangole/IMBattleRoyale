package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

public class FishMutation implements Listener {

    public static void onFish(){
        if(Data.estate == EventState.FishMutation){
            Data.users.forEach(u -> {
                if(u.isAlive()){
                    if(u.getPlayer().getLocation().getBlock().getType().equals(Material.AIR)){
                    }
                }
            });
        }
    }
}
