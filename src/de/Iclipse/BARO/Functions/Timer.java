package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Util.Actionbar;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;

import static de.Iclipse.BARO.Data.dsp;

public class Timer {
    public static void timer() {
        Data.timer++;
        DecimalFormat format = new DecimalFormat("00");
        int t = Data.timer;
        int hours = (int) t / (60 * 60);
        t = t % (60 * 60 * 20);
        int minutes = (int) t / 60;
        t = t % (60 * 20);
        int seconds = t;
        Bukkit.getOnlinePlayers().forEach(entry -> {
            if (!Data.spawningPlayers.contains(entry)) {
                Actionbar.send(entry, "§7§l" + dsp.get("timer.timer", entry) + ": §2§l" + format.format(hours) + ":" + format.format(minutes) + ":" + format.format(seconds));
            }
        });
    }
}
