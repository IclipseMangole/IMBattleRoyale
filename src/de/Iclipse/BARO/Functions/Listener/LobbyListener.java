package de.Iclipse.BARO.Functions.Listener;

import de.Iclipse.BARO.Functions.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.Iclipse.BARO.Data.*;

public class LobbyListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(state == GameState.Lobby){
            e.setJoinMessage(null);
            Bukkit.getOnlinePlayers().forEach(entry ->{
                dsp.send(entry, "join.message", p.getDisplayName());
            });
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(state == GameState.Lobby){
            e.setQuitMessage(null);
            Bukkit.getOnlinePlayers().forEach(entry ->{
                dsp.send(entry, "quit.message", p.getDisplayName());
            });
        }
    }

}
