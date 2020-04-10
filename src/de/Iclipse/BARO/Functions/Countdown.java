package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Functions.Vanish;
import org.bukkit.Bukkit;

import static de.Iclipse.BARO.Data.*;


public class Countdown {

    public static void countdown() {
        if ((Bukkit.getOnlinePlayers().size() - Vanish.getVanishsOnServer().size()) >= Data.minplayers) {
            if (Data.countdown >= 0) {
                if (Data.countdown >= 30 * 60 && Data.countdown <= 90 * 60) {
                    if (Data.countdown % (30 * 60) == 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                    }
                } else if (Data.countdown >= 5 * 60) {
                    if (Data.countdown % (5 * 60) == 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                    }
                } else if (Data.countdown >= 1 * 60) {
                    if (Data.countdown % (1 * 60) == 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                    }
                } else if (Data.countdown > 10) {
                    if (Data.countdown % (15 * 60) == 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.seconds", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                    }
                } else if (Data.countdown > 0) {
                    Bukkit.getOnlinePlayers().forEach(entry -> {
                        dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.seconds", entry));
                    });
                    dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                } else {
                    Bukkit.getOnlinePlayers().forEach(entry -> {
                        dsp.get("countdown.countdown", entry);
                    });
                    dsp.send(Bukkit.getConsoleSender(), "countdown.countdown");

                }

                Data.countdown--;
            }
        } else {
            if (countdown < defaultcountdown) {
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    dsp.send(entry, "countdown.reset");
                });
            }
        }
    }
}
