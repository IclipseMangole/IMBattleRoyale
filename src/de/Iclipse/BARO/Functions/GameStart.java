package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;

public class GameStart {
    public void startGame() {
        Data.state = GameState.Running;
        Bukkit.getOnlinePlayers().forEach(entry -> {
            Data.spawningPlayers.add(entry);
        });
    }
}
