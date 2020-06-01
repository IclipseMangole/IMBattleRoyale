package de.Iclipse.BARO.Commands;

import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Functions.Vanish;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static de.Iclipse.BARO.Data.*;

public class cmd_start {
    @IMCommand(
            name = "start",
            maxArgs = 0,
            minArgs = 0,
            usage = "start.usage",
            description = "start.description",
            permissions = "im.cmd.start"
    )
    public void start(CommandSender sender) {
        if (state == GameState.Lobby) {
            if ((Bukkit.getOnlinePlayers().size() - Vanish.getVanishsOnServer().size()) >= minplayers) {
                if (countdown > skipCountdown) {
                    countdown = skipCountdown;
                } else {
                    dsp.send(sender, "start.already");
                }
            } else {
                dsp.send(sender, "start.missing", "" + (minplayers - (Bukkit.getOnlinePlayers().size() - Vanish.getVanishsOnServer().size())));
            }
        } else {
            dsp.send(sender, "start.running");
        }
    }
}
