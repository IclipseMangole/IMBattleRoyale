package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import static de.Iclipse.BARO.Data.dsp;


public class GameStart implements Listener {
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
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            p.setGravity(false);
            p.setAllowFlight(true);
            Data.spawningPlayers.add(p);
            p.sendTitle(dsp.get("teleport.title", p), dsp.get("teleport.subtitle", p), 10, 80, 10);
        });
        Data.state = GameState.Running;
    }

}
