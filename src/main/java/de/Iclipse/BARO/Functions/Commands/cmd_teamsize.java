package de.Iclipse.BARO.Functions.Commands;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Status;
import de.Iclipse.BARO.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

public class cmd_teamsize {
    @IMCommand(
            name = "teams",
            maxArgs = 1,
            minArgs = 0,
            usage = "/teams (zahl)",
            permissions = "im.cmd.teams"
    )
    public void execute(CommandSender sender, Boolean teams){
        if(teams != null){
            if(Data.status.equals(Status.LOBBY)){

            }else{
                sender.sendMessage(Data.prefix + "Das Spiel läuft bereits!");
            }
        }else{
            sender.sendMessage(Data.prefix + "Teamgröße: §h" + Data.teamsize);
        }
    }
}
