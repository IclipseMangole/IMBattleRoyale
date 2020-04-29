package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.States.Start;
import de.Iclipse.IMAPI.Functions.Vanish;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import static de.Iclipse.BARO.Data.*;


public class Countdown {

    public static void countdown(int seconds) {
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
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            entry.playSound(entry.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                        });
                    }
                } else if (Data.countdown > 5) {
                    if (Data.countdown % 15 == 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.seconds", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        if (countdown == 15) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                entry.playSound(entry.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                            });
                        }
                    } else if (countdown == 10) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.seconds", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            entry.playSound(entry.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                        });

                    }
                } else if (Data.countdown > 0) {
                    Bukkit.getOnlinePlayers().forEach(entry -> {
                        dsp.send(entry, "countdown.message", "" + Data.countdown, dsp.get("unit.seconds", entry));
                    });
                    dsp.send(Bukkit.getConsoleSender(), "countdown.message", "" + Data.countdown, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                    Bukkit.getOnlinePlayers().forEach(entry -> {
                        entry.playSound(entry.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                    });
                } else {
                    Bukkit.getOnlinePlayers().forEach(entry -> {
                        dsp.get("countdown.finished", entry);
                    });
                    dsp.send(Bukkit.getConsoleSender(), "countdown.finished");
                    Bukkit.getOnlinePlayers().forEach(entry -> {
                        entry.playSound(entry.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1, 1);
                    });
                    Start.startGame();
                }

                Bukkit.getOnlinePlayers().forEach(entry -> {
                    entry.setLevel(countdown);
                    if (defaultcountdown <= 100) {
                        entry.setExp((float) countdown / (float) defaultcountdown);
                    } else {
                        if (countdown > 100) {
                            entry.setExp(1);
                        } else {
                            entry.setExp((float) countdown / (float) 100);
                        }
                    }
                });
                Data.countdown--;
            }
        } else {
            if (countdown < defaultcountdown) {
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    dsp.send(entry, "countdown.reset");
                    entry.setLevel(0);
                    entry.setExp(0.0f);
                });
                countdown = defaultcountdown;
            } else {
                if (seconds % 20 == 0) {
                    Bukkit.getOnlinePlayers().forEach(entry -> {
                        dsp.send(entry, "countdown.missing", "" + (Data.minplayers - (Bukkit.getOnlinePlayers().size() - Vanish.getVanishsOnServer().size())));
                    });
                }
            }
        }
    }
}
