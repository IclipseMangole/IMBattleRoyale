package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.IMAPI.Util.Actionbar;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import static de.Iclipse.BARO.Data.dsp;

public class Timer {
    public static void timer() {
        Data.timer++;
        DecimalFormat format = new DecimalFormat("00");
        int t = Data.timer;
        int hours = t / (60 * 60);
        t = t % (60 * 60);
        int minutes = t / 60;
        t = t % 60;
        int seconds = t;
        Bukkit.getOnlinePlayers().forEach(entry -> {
            if (User.getUser(entry) != null) {
                User u = User.getUser(entry);
                if (!Data.spawningPlayers.contains(entry) && !BorderManager.outOfBorder.contains(entry)) {
                    boolean contains = false;
                    for (Map.Entry<ArrayList<User>, Integer> e : Data.reviving.entrySet()) {
                        ArrayList<User> list = e.getKey();
                        if (list.contains(u)) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        Actionbar.send(entry, "§7§l" + dsp.get("timer.timer", entry) + ": §2§l" + format.format(hours) + ":" + format.format(minutes) + ":" + format.format(seconds));
                    }
                }
            }
        });
    }
}
