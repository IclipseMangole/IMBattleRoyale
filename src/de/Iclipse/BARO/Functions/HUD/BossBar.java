package de.Iclipse.BARO.Functions.HUD;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.Events.EventState;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import static de.Iclipse.BARO.Data.*;

public class BossBar implements Listener {
    public static void createBars() {
        Data.users.forEach(u -> {
            org.bukkit.boss.BossBar bar = Bukkit.createBossBar(u.getPlayer().getDisplayName(), BarColor.GREEN, BarStyle.SEGMENTED_20);
            bar.setProgress(u.getPlayer().getHealth() / 20);
            Data.playerBossBars.put(u.getPlayer(), bar);
        });
        Data.borderBossBarDE = Bukkit.createBossBar(dsp.get("border.nextZone", dsp.getLanguages().get("DE"), false), BarColor.PURPLE, BarStyle.SOLID);
        Data.borderBossBarEN = Bukkit.createBossBar(dsp.get("border.nextZone", dsp.getLanguages().get("EN"), false), BarColor.PURPLE, BarStyle.SOLID);
        Data.eventBossBarDE = Bukkit.createBossBar(dsp.get("event.comingup", dsp.getLanguages().get("DE"), false), BarColor.RED, BarStyle.SOLID);
        Data.eventBossBarEN = Bukkit.createBossBar(dsp.get("event.comingup", dsp.getLanguages().get("EN"), false), BarColor.RED, BarStyle.SOLID);
    }

    public static void bossbar() {
        updateBars();
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (User.getUser(p) != null) {
                User u = User.getUser(p);
                if (u.getTeam() != null) {
                    if (hasLivingMates(u)) {
                        if (!matesShowedOnScoreboard(u)) {
                            u.getTeam().getUsers().forEach(entry -> {
                                if (entry.isAlive()) {
                                    if (!entry.equals(u)) {
                                        org.bukkit.boss.BossBar bar = Data.playerBossBars.get(entry.getPlayer());
                                        if (!bar.getPlayers().contains(u.getPlayer())) {
                                            bar.addPlayer(u.getPlayer());
                                            if (Data.borderBossBarEN.getPlayers().contains(u.getPlayer())) {
                                                Data.borderBossBarEN.removePlayer(u.getPlayer());
                                            }
                                            if (Data.borderBossBarDE.getPlayers().contains(u.getPlayer())) {
                                                Data.borderBossBarDE.removePlayer(u.getPlayer());
                                            }
                                        }
                                    }
                                } else {
                                    if (Data.playerBossBars.get(entry.getPlayer()) != null) {
                                        if (Data.playerBossBars.get(entry.getPlayer()).getPlayers().contains(u.getPlayer())) {
                                            Data.playerBossBars.get(entry.getPlayer()).removePlayer(u.getPlayer());
                                        }
                                    }
                                }
                            });
                            return;
                        }
                    }
                }
            }
            Data.playerBossBars.forEach((player, bar) -> {
                if (bar.getPlayers().contains(p)) {
                    bar.removePlayer(p);
                }
            });
            if (!UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone")) {
                if (nextEventTime - timer <= 60) {
                    if (Data.borderBossBarEN.getPlayers().contains(p)) {
                        Data.borderBossBarEN.removePlayer(p);
                    }
                    if (Data.borderBossBarDE.getPlayers().contains(p)) {
                        Data.borderBossBarDE.removePlayer(p);
                    }
                    if (de.Iclipse.IMAPI.Database.User.getLanguage(UUIDFetcher.getUUID(p.getName())).equalsIgnoreCase("DE")) {
                        Data.eventBossBarDE.addPlayer(p);
                        if (Data.eventBossBarEN.getPlayers().contains(p)) {
                            Data.eventBossBarEN.removePlayer(p);
                        }
                    } else {
                        Data.borderBossBarEN.addPlayer(p);
                        if (Data.borderBossBarDE.getPlayers().contains(p)) {
                            Data.borderBossBarDE.removePlayer(p);
                        }
                    }
                    return;
                }
            }


            if (eventBossBarDE.getPlayers().contains(p)) {
                eventBossBarDE.removePlayer(p);
            }
            if (eventBossBarEN.getPlayers().contains(p)) {
                eventBossBarEN.removePlayer(p);
            }
            if (de.Iclipse.IMAPI.Database.User.getLanguage(UUIDFetcher.getUUID(p.getName())).equalsIgnoreCase("DE")) {
                Data.borderBossBarDE.addPlayer(p);
                if (Data.borderBossBarEN.getPlayers().contains(p)) {
                    Data.borderBossBarEN.removePlayer(p);
                }
            } else {
                Data.borderBossBarEN.addPlayer(p);
                if (Data.borderBossBarDE.getPlayers().contains(p)) {
                    Data.borderBossBarDE.removePlayer(p);
                }
            }

        });
    }

    public static void updateBars() {
        /*
        Data.playerBossBars.forEach((p, bar) ->{
            if(User.getUser(p) != null) {
                User u = User.getUser(p);
                if(!u.isKnocked()) {
                    if(!bar.getColor().equals(BarColor.GREEN)){
                        bar.setColor(BarColor.GREEN);
                    }
                    bar.setProgress(p.getHealth() / 20);
                }else{
                    if(!bar.getColor().equals(BarColor.RED)){
                        bar.setColor(BarColor.RED);
                    }
                    bar.setProgress(p.getHealth() / 20);
                }
            }
        });
         */
        if (Data.nextEventTime - Data.timer <= 60) {
            if (Data.estate == EventState.None) {
                eventBossBarDE.setTitle(dsp.get("event.comingup.bossbar", dsp.getLanguages().get("DE"), false, dsp.get("event." + nextEvent.getName(), dsp.getLanguages().get("DE"))));
                eventBossBarEN.setTitle(dsp.get("event.comingup.bossbar", dsp.getLanguages().get("EN"), false, dsp.get("event." + nextEvent.getName(), dsp.getLanguages().get("EN"))));
                eventBossBarDE.setProgress(1.0 - ((double) (nextEventTime - timer) / 60));
                eventBossBarEN.setProgress(1.0 - ((double) (nextEventTime - timer) / 60));
            } else {
                eventBossBarDE.setTitle(dsp.get("event.finished.bossbar", dsp.getLanguages().get("DE"), false, dsp.get("event." + estate.getName(), dsp.getLanguages().get("DE"))));
                eventBossBarEN.setTitle(dsp.get("event.finished.bossbar", dsp.getLanguages().get("EN"), false, dsp.get("event." + estate.getName(), dsp.getLanguages().get("DE"))));
                eventBossBarDE.setProgress((double) (nextEventTime - timer) / 60);
                eventBossBarEN.setProgress((double) (nextEventTime - timer) / 60);
            }
        }

        if (BorderManager.border.getProgress() >= 1) {
            if (!Data.borderBossBarDE.getTitle().equals(dsp.get("border.nextZone", dsp.getLanguages().get("DE"), false))) {
                Data.borderBossBarDE.setTitle(dsp.get("border.nextZone", dsp.getLanguages().get("DE"), false));
                Data.borderBossBarEN.setTitle(dsp.get("border.nextZone", dsp.getLanguages().get("EN"), false));
                Data.borderBossBarDE.setColor(BarColor.YELLOW);
                Data.borderBossBarEN.setColor(BarColor.YELLOW);
            }
            Data.borderBossBarDE.setProgress(1.0 - ((double) ((int) ((BorderManager.border.getProgress() - 1) * 200)) / 100.0));
            Data.borderBossBarEN.setProgress(1.0 - ((double) ((int) ((BorderManager.border.getProgress() - 1) * 200)) / 100.0));
        } else {
            if (!Data.borderBossBarDE.getTitle().equals(dsp.get("border.shrink", dsp.getLanguages().get("DE"), false))) {
                Data.borderBossBarDE.setTitle(dsp.get("border.shrink", dsp.getLanguages().get("DE"), false));
                Data.borderBossBarEN.setTitle(dsp.get("border.shrink", dsp.getLanguages().get("EN"), false));
                Data.borderBossBarDE.setColor(BarColor.PURPLE);
                Data.borderBossBarEN.setColor(BarColor.PURPLE);
            }
            Data.borderBossBarDE.setProgress(BorderManager.border.getProgress());
            Data.borderBossBarEN.setProgress(BorderManager.border.getProgress());
        }
    }


    @EventHandler
    public void onRegain(EntityRegainHealthEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getEntity() instanceof Player) {
                if (User.getUser((Player) e.getEntity()) != null) {
                    User u = User.getUser((Player) e.getEntity());
                    if (Data.playerBossBars.containsKey(e.getEntity())) {
                        org.bukkit.boss.BossBar bar = Data.playerBossBars.get(e.getEntity());
                        if (!u.isKnocked()) {
                            if (!bar.getColor().equals(BarColor.GREEN)) {
                                bar.setColor(BarColor.GREEN);
                            }
                            bar.setProgress(((Player) e.getEntity()).getHealth() / 20);
                        } else {
                            if (!bar.getColor().equals(BarColor.RED)) {
                                bar.setColor(BarColor.RED);
                            }
                            bar.setProgress(((Player) e.getEntity()).getHealth() / 20);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getEntity() instanceof Player) {
                if (User.getUser((Player) e.getEntity()) != null) {
                    User u = User.getUser((Player) e.getEntity());
                    if (Data.playerBossBars.containsKey(e.getEntity())) {
                        org.bukkit.boss.BossBar bar = Data.playerBossBars.get(e.getEntity());
                        if (!u.isKnocked()) {
                            if (!bar.getColor().equals(BarColor.GREEN)) {
                                bar.setColor(BarColor.GREEN);
                            }
                            bar.setProgress(((Player) e.getEntity()).getHealth() / 20);
                        } else {
                            if (!bar.getColor().equals(BarColor.RED)) {
                                bar.setColor(BarColor.RED);
                            }
                            bar.setProgress(((Player) e.getEntity()).getHealth() / 20);
                        }
                    }
                }
            }
        }
    }


    public static void clearBars(Player p) {
        if (Data.borderBossBarEN.getPlayers().contains(p)) {
            Data.borderBossBarEN.removePlayer(p);
        }
        if (Data.borderBossBarDE.getPlayers().contains(p)) {
            Data.borderBossBarDE.removePlayer(p);
        }
        Data.playerBossBars.forEach((player, bar) -> {
            if (bar.getPlayers().contains(p)) {
                bar.removePlayer(p);
            }
        });
    }

    public static boolean matesShowedOnScoreboard(User u) {
        if (!u.isAlive()) {
            return u.getTeam().getAlive() > UserSettings.getInt(UUIDFetcher.getUUID(u.getPlayer().getName()), "baro_maxPlayerBars");
        } else {
            return u.getTeam().getAlive() - 1 > UserSettings.getInt(UUIDFetcher.getUUID(u.getPlayer().getName()), "baro_maxPlayerBars");
        }
    }

    public static boolean hasLivingMates(User u) {
        if (!u.isAlive()) {
            return u.getTeam().getAlive() > 0;
        } else {
            return u.getTeam().getAlive() > 1;
        }
    }
}
