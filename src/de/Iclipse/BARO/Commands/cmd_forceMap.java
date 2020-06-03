package de.Iclipse.BARO.Commands;

import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

public class cmd_forceMap {
    @IMCommand(
            name = "forceMap",
            usage = "/forcemap",
            description = "Dorced eine Map",
            minArgs = 1,
            maxArgs = 1,
            permissions = "im.cmd.forceMap"
    )
    public void execute(CommandSender sender, String map) {
        boolean successfull = Data.mapLoader.loadMap(map, true);
        if (successfull) {
            sender.sendMessage("§7Die Map wurde geforced");
        } else {
            sender.sendMessage("§4Die Map existiert nicht!");
        }
    }
}
