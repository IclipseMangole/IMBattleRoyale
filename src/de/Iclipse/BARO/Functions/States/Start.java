package de.Iclipse.BARO.Functions.States;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.HUD.BossBar;
import de.Iclipse.BARO.Functions.PlayerManagement.TeamManager;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.Spectator;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.Functions.Servers.State;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Date;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.IMAPI.Functions.PlayerReset.resetPlayer;
import static de.Iclipse.IMAPI.IMAPI.getServerName;


public class Start {
    public static void startGame() {
        Server.setState(getServerName(), State.Running);
        Data.users.forEach(user -> {
            TeamManager.autoFill();
            TeamManager.autoDelete();
            Player p = user.getPlayer();
            resetPlayer(p);
            p.setAllowFlight(true);
            p.setGravity(false);
            Data.spawningPlayers.add(p);
            p.sendTitle(dsp.get("teleport.title", p), dsp.get("teleport.subtitle", p), 10, 80, 10);
        });
        Bukkit.getOnlinePlayers().forEach(entry -> {
            if (!Data.users.contains(User.getUser(entry))) {
                Spectator.setSpectator(entry);
            }
        });
        BossBar.createBars();
        Data.state = GameState.Running;
        Data.start = Date.from(Instant.now());
    }

}
