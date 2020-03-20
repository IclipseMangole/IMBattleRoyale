package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 09.06.2019 at 22:54 o´ clock
 */
public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void onDmg(EntityDamageEvent e){
        if(Data.status.equals(Status.LOBBY) || Data.status.equals(Status.FINISHED)){
            e.setCancelled(true);
        }else{

        }
    }
}
