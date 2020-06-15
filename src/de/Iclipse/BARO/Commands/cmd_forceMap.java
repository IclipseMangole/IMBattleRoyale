package de.Iclipse.BARO.Commands;

import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

import static de.Iclipse.BARO.Data.dsp;

public class cmd_forceMap {
    @IMCommand(
            name = "forceMap",
            usage = "forceMap.usage",
            description = "forceMap.description",
            minArgs = 1,
            maxArgs = 1,
            permissions = "im.cmd.forceMap"
    )
    public void execute(CommandSender sender, String map) {
        boolean successfull = Data.mapLoader.loadMap(map);
        if (successfull) {
            dsp.send(sender, "forceMap.successfull");
        } else {
            dsp.send(sender, "forceMap.failure");
        }
    }
}