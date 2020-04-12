package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Util.Actionbar;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Random;

import static de.Iclipse.BARO.Data.*;

public class PlayerSpawns implements Listener {

    public static void teleport() {
        //Middle X: 0 Z: 0 Radius: 450
        if (Data.timer % 10 == 0 && Data.timer < 80) {
            Data.spawningPlayers.forEach(entry -> {
                teleportRandom(entry);
            });
        } else {
            Data.spawningPlayers.forEach(entry -> {
                Actionbar.send(entry, dsp.get("teleport.time", entry, Data.timer % 10 + ""));
            });
        }
    }

    public static void teleportRandom(Player p) {
        //Middle X: 0 Z: 0 Radius: 450
        Location loc = new Location(p.getWorld(), new Random().nextInt(900) - 450.0, 175.0, new Random().nextInt(900) - 450.0);
        //Tests if new Location is near to old Location to spread Spawnpoints
        if (p.getLocation().distance(loc) > 100) {
            p.teleport(loc);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
            Actionbar.send(p, dsp.get("teleport.teleport", p));
        } else {
            teleportRandom(p);
        }
    }


    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (Data.spawningPlayers.contains(p)) {
            flyingPlayers.add(p);
            Data.spawningPlayers.remove(p);
            dsp.send(p, "teleport.left");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (flyingPlayers.contains(p)) {
            if (p.getLocation().getBlockY() < 115) {
                fallingPlayers.add(p);
                flyingPlayers.remove(p);
            }
        }
        if (fallingPlayers.contains(p)) {
            if (p.isOnGround()) {
                fallingPlayers.remove(p);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (spawningPlayers.contains(p) || flyingPlayers.contains(p) || fallingPlayers.contains(p)) {
                e.setCancelled(true);
            }
        }
    }
}
