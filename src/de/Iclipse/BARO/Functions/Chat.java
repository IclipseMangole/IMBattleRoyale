package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static de.Iclipse.BARO.Data.dsp;

public class Chat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (Data.state == GameState.Running) {
            if (User.getUser(p) != null) {
                User u = User.getUser(p);
                if (u.isInATeam()) {
                    if (e.getMessage().startsWith("@a")) {
                        sendAllchat(u, e.getMessage().replaceFirst("@a", ""));
                    } else {
                        sendTeamchat(u, e.getMessage());
                    }
                } else {
                    sendSpecchat(p, e.getMessage());
                }
            } else {
                sendSpecchat(p, e.getMessage());
            }
            e.setCancelled(true);
        }
    }

    public static void sendTeamchat(User u, String message) {
        u.getTeam().getUsers().forEach(o -> {
            dsp.send(o.getPlayer(), "chat.team", u.getTeam().getColor() + dsp.get("color." + u.getTeam().getColor().asBungee().getName(), u.getPlayer()), u.getPlayer().getDisplayName(), message);
        });
    }

    public static void sendAllchat(User u, String message) {
        Bukkit.getOnlinePlayers().forEach(o -> {
            dsp.send(o.getPlayer(), "chat.global", u.getPlayer().getDisplayName(), message);
        });
    }

    public static void sendSpecchat(Player p, String message) {
        Data.spectators.forEach(s -> {
            dsp.send(s, "chat.spec", p.getDisplayName(), message);
        });
    }


}
