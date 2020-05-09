package de.Iclipse.BARO.Commands;

import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.entity.Player;

import static de.Iclipse.BARO.Data.dsp;

public class cmd_barSettingZone {
    @IMCommand(
            name = "barSettingZone",
            aliases = {"bsz"},
            maxArgs = 0,
            usage = "barSettingZone.usage",
            description = "barSettingZone.description",
            permissions = "im.cmd.barSettingZone"
    )
    public void execute(Player p) {
        if (UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone")) {
            dsp.send(p, "barSettingZone.disabled");
        } else {
            dsp.send(p, "barSettingZone.enabled");
        }
        UserSettings.setBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone", !UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone"));
    }
}
