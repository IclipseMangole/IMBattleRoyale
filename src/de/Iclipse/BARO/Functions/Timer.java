package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;

public class Timer {
    public static void timer() {
        Data.duration++;
        Data.timer++;
        DecimalFormat format = new DecimalFormat("00");
        long t = Data.timer;
        int hours = (int) t / (60 * 60 * 20);
        t = t % (60 * 60 * 20);
        int minutes = (int) t / (60 * 20);
        t = t % (60 * 20);
        int seconds = (int) t / (20);
        t = t % (20);
        int milliseconds = (int) t * 5;
        Bukkit.getOnlinePlayers().forEach(entry -> {
            ActionbarManager.sendActionBar(entry, "§7§lChallenge: §2§l" + format.format(hours) + ":" + format.format(minutes) + ":" + format.format(seconds) + ":" + format.format(milliseconds));
        });
    }else

    {
        Bukkit.getOnlinePlayers().forEach(entry -> {
            if (Data.timer == 0) {
                ActionbarManager.sendActionBar(entry, "§6§lTimer läuft nicht!");
            } else {
                DecimalFormat format = new DecimalFormat("00");
                long t = Data.timer;
                int hours = (int) t / (60 * 60 * 20);
                t = t % (60 * 60 * 20);
                int minutes = (int) t / (60 * 20);
                t = t % (60 * 20);
                int seconds = (int) t / (20);
                t = t % (20);
                int milliseconds = (int) t * 5;
                ActionbarManager.sendActionBar(entry, "§6§lPausiert: " + format.format(hours) + ":" + format.format(minutes) + ":" + format.format(seconds) + ":" + format.format(milliseconds));
            }
        });
    }
}
