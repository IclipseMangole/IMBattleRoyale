package de.Iclipse.BARO.Commands;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.BARO.Functions.Scheduler;
import de.Iclipse.IMAPI.Util.Actionbar;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class cmd_pause {
    @IMCommand(
            name = "pause",
            permissions = "im.cmd.pause",
            minArgs = 0,
            maxArgs = 0
    )
    public void pause(CommandSender sender) {
        if (Data.state != GameState.Pause) {
            Data.state = GameState.Pause;
            Scheduler.stopScheduler();
            Bukkit.getOnlinePlayers().forEach(entry -> {
                Actionbar.send(entry, "Spiel wird pausiert!");
            });
        } else {
            Data.state = GameState.Running;
            Scheduler.startScheduler();
            Bukkit.getOnlinePlayers().forEach(entry -> {
                Actionbar.send(entry, "Spiel wird fortgesetzt!");
            });
        }
    }
}
