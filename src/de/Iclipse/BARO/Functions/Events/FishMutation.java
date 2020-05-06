package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.potion.PotionEffectType;

public class FishMutation implements Listener {

    @EventHandler
    public void onFish(EventChangeEvent event){
        if(event.getAfter() == EventState.FishMutation){
            Data.users.forEach(user -> {
                if(user.isAlive()){
                    Data.fishmutation.add(user);
                }
            });
        }else if(event.getBefore() == EventState.FishMutation){
            Data.fishmutation.clear();
        }
    }

    @EventHandler
    public void onBreath(EntityAirChangeEvent event){
        if(event.getEntity() instanceof Player){
            if(Data.estate == EventState.FishMutation){
                event.setCancelled(true);
            }
        }
    }
}
