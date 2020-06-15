package de.Iclipse.BARO.Commands;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

public class cmd_stats {
    @IMCommand(
            name = "stats",
            usage = "stats.usage",
            description = "stats.description",
            maxArgs = 1,
            minArgs = 0,
            permissions = "im.cmd.stats"
    )
    public void execute(CommandSender sender, String name) {
        if (name == null) {

        } else {

        }
    }

    @IMCommand(
            name = "statsall",
            usage = "stats.usage",
            description = "stats.description",
            maxArgs = 1,
            minArgs = 0,
            permissions = "im.cmd.stats"
    )
    public void executeAll(CommandSender sender, String name) {

    }

}
