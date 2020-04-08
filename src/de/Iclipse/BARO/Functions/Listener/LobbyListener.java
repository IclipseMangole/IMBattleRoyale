package de.Iclipse.BARO.Functions.Listener;

import de.Iclipse.BARO.Functions.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.BARO.Data.state;

public class LobbyListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(state == GameState.Lobby || state == GameState.Finished) {
            e.setJoinMessage(null);
            Bukkit.getOnlinePlayers().forEach(entry -> {
                dsp.send(entry, "join.message", p.getDisplayName());
            });
            p.teleport(new Location(p.getWorld(), 145.5, 69.5, -447.5, -90.0f, 0.0f));
            switch (new Random().nextInt(2)) {
                case 0:
                    p.playSound(p.getLocation(), Sound.MUSIC_DISC_WAIT, 1, 1);
                    break;
                case 1:
                    p.playSound(p.getLocation(), Sound.MUSIC_DISC_FAR, 1, 1);
                    break;
            }

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
