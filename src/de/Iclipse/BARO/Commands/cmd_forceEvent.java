package de.Iclipse.BARO.Commands;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Events.EventState;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

public class cmd_forceEvent {
    @IMCommand(
            name = "forceEvent",
            permissions = "im.cmd.forceEvent",
            usage = "forceEvent.usage",
            description = "forceEvent.description",
            maxArgs = 1,
            minArgs = 1
    )
    public void execute(CommandSender sender, String event) {
        if (Data.estate == EventState.None) {
            final boolean[] contains = {false};
            Data.allEvents.forEach(entry -> {
                if (entry.getName().equals(event)) {
                    Data.nextEvent = entry;
                    Data.nextEventTime = Data.timer + 30;
                    sender.sendMessage(de.Iclipse.IMAPI.Data.prefix + "Das nächste Event wurde geändert!");
                    contains[0] = true;
                }
            });
            if (!contains[0]) {
                sender.sendMessage(de.Iclipse.IMAPI.Data.prefix + "§cDas Event lief bereits oder es existiert nicht!");
            }
        } else {
            sender.sendMessage(de.Iclipse.IMAPI.Data.prefix + "§cEs läuft bereits ein Event");
        }
    }
}
