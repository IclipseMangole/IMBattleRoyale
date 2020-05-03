package de.Iclipse.BARO.Functions.States;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.HUD.BossBar;
import de.Iclipse.BARO.Functions.PlayerManagement.TeamManager;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.Spectator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Date;

import static de.Iclipse.BARO.Data.dsp;


public class Start {
    public static void startGame() {
        Data.users.forEach(user -> {
            TeamManager.autoFill();
            TeamManager.autoDelete();
            Player p = user.getPlayer();
            p.setLevel(0);
            p.setExp(0);
            p.setInvulnerable(false);
            p.getInventory().clear();
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setFireTicks(0);
            p.setCanPickupItems(true);
            p.setCollidable(true);
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            p.setGravity(false);
            p.setAllowFlight(true);
            Data.spawningPlayers.add(p);
            p.sendTitle(dsp.get("teleport.title", p), dsp.get("teleport.subtitle", p), 10, 80, 10);
        });
        Bukkit.getOnlinePlayers().forEach(entry -> {
            System.out.println(entry.getName());
            if (!Data.users.contains(User.getUser(entry))) {
                Spectator.setSpectator(entry);
            }
        });
        BossBar.createBars();
        Data.state = GameState.Running;
        Data.start = Date.from(Instant.now());
    }

}
