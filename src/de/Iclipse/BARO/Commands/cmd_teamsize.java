package de.Iclipse.BARO.Commands;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.TeamManager;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

import static de.Iclipse.BARO.Data.dsp;

public class cmd_teamsize {
    @IMCommand(
            name = "teamsize",
            minArgs = 1,
            maxArgs = 1,
            permissions = "im.cmd.teamsize",
            usage = "teamsize.usage",
            description = "teamsize.description"
    )
    public void teamsize(CommandSender sender, int teamsize) {
        if (Data.teamsize != teamsize) {
            dsp.send(sender, "teamsize.set", "" + teamsize);
            Data.teamsize = teamsize;
            Data.minplayers = teamsize + 1;
            Data.countdown = Data.defaultCountdown;
            Data.teams = new ArrayList<>();
            TeamManager.createTeams();
            Bukkit.getOnlinePlayers().forEach(entry -> {
                dsp.send(entry, "countdown.reset");
                de.Iclipse.IMAPI.Data.tablist.setTablist(entry);
                entry.setExp(0.0f);
                entry.setLevel(0);
            });
            dsp.send(Bukkit.getConsoleSender(), "countdown.reset");
        } else {
            dsp.send(sender, "teamsize.already", "" + teamsize);
        }
    }
}
