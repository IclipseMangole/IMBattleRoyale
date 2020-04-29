package de.Iclipse.BARO.Commands;

import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.entity.Player;

import static de.Iclipse.BARO.Data.dsp;

public class cmd_maxPlayerBars {
    @IMCommand(
            name = "maxPlayerBars",
            aliases = {"maxbars", "mpb", "mp"},
            maxArgs = 1,
            minArgs = 0,
            permissions = "im.cmd.maxbars",
            noConsole = true,
            description = "maxPlayerBars.description",
            usage = "maxPlayerBars.usage"
    )
    public void maxPlayerBars(Player p, Integer mpb) {
        if (mpb == null) {
            dsp.send(p, "maxPlayerBars.get", UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars") + "");
        } else {
            if (mpb >= 0) {
                if (mpb < 65) {
                    if (UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars") != mpb) {
                        UserSettings.setInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars", mpb);
                        dsp.send(p, "maxPlayerBars.changed");
                    } else {
                        dsp.send(p, "maxPlayerBars.already");
                    }
                } else {
                    dsp.send(p, "maxPlayerBars.above");
                }
            } else {
                dsp.send(p, "maxPlayerBars.below");
            }
        }
    }
}
