package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameFinish implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (Data.state == GameState.Running) {
            int teamsremaining;
            User u = User.getUser(e.getEntity());
            if (u != null) {
                if (u.getTeam().getUsers().size() == 1) {
                    Data.teams.remove(u.getTeam());
                    if (Data.teams.size() == 1) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            entry.teleport(Data.spawn(u.getPlayer()));
                            de.Iclipse.IMAPI.Data.restart = 15;
                        });
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Data.state == GameState.Running) {
            int teamsremaining;
            User u = User.getUser(e.getPlayer());
            if (u != null) {
                if (u.getTeam().getUsers().size() == 1) {
                    Data.teams.remove(u.getTeam());
                    if (Data.teams.size() == 1) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            entry.sendMessage(de.Iclipse.IMAPI.Data.prefix + "Finished!");
                            entry.teleport(Data.spawn(u.getPlayer()));
                            de.Iclipse.IMAPI.Data.restart = 15;
                        });
                    }
                }
            }
        }
    }
}
