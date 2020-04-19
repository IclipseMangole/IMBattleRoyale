package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameFinish implements Listener {

    public static void finish() {
        Bukkit.getOnlinePlayers().forEach(entry -> {
            Data.state = GameState.Finished;
            entry.teleport(Data.spawn(entry.getWorld()));
            de.Iclipse.IMAPI.Data.restart = 15;
            entry.resetPlayerWeather();
            entry.getActivePotionEffects().forEach(effect -> {
                entry.removePotionEffect(effect.getType());
            });
            entry.getInventory().clear();
            BorderManager.removeBorderEffect(entry);
            entry.playSound(entry.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);

        });
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getEntity());
            if (u != null) {
                if (u.getTeam() != null) {
                    if (u.getTeam().getUsers().size() == 1) {
                        Data.teams.remove(u.getTeam());
                        if (Data.teams.size() == 1) {
                            finish();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getPlayer());
            if (u != null) {
                if (u.getTeam() != null) {
                    if (u.getTeam().getUsers().size() == 1) {
                        Data.teams.remove(u.getTeam());
                        if (Data.teams.size() == 1) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                Data.state = GameState.Finished;
                                entry.teleport(Data.spawn(u.getPlayer().getWorld()));
                                de.Iclipse.IMAPI.Data.restart = 15;
                            });
                        }
                    }
                }
            }
        }
    }
}
