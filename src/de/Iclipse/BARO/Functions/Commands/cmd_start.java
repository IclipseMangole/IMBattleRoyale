package de.Iclipse.BARO.Functions.Commands;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Status;
import de.Iclipse.BARO.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class cmd_start {

    @IMCommand(
            name = "start",
            requiresConsole = true,
            usage = "/start <zeit>",
            description = "Startet BARO",
            minArgs = 1,
            maxArgs = 1
    )
    public void execute(CommandSender sender, int zeit){
        if(Data.status.equals(Status.LOBBY)){
            Data.status = Status.STARTING;
            Data.countdown = zeit;
            Bukkit.broadcastMessage(Data.prefix + "§5Der Countdown wurde gestartet!");
        } else {
            sender.sendMessage(Data.prefix + "§cBARO läuft bereits!");
        }
    }
}
