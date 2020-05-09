package de.Iclipse.BARO.Functions.HUD;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.Events.EventState;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.ScoreboardSign;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

import static de.Iclipse.BARO.Data.dsp;

public class Scoreboard implements Listener {
    public static HashMap<Player, ScoreboardSign> boards = new HashMap<>();


    public static void scoreboard() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            ScoreboardSign ss;
            if (!boards.containsKey(p)) {
                ss = new ScoreboardSign(p, "§8« §5§lIM§r§fBARO§r§8 »");
                ss.create();
            } else {
                ss = boards.get(p);
            }
            ss.setLine(0, ChatColor.AQUA + "");
            if (User.getUser(p) != null) {
                if (User.getUser(p).getTeam() != null) {
                    if (Data.teamsize == 1 || User.getUser(p).getTeam().getUsers().size() == 1) {
                        ss.setLine(1, dsp.get("scoreboard.kills", p, User.getUser(p).getKills() + ""));
                    } else {
                        final int[] teamkills = {0};
                        User.getUser(p).getTeam().getUsers().forEach(entry -> {
                            teamkills[0] += entry.getKills();
                        });
                        ss.setLine(1, dsp.get("scoreboard.teamkills", p, User.getUser(p).getKills() + "", teamkills[0] + ""));
                    }
                }
            }
            ss.setLine(2, ChatColor.BLUE + "");
            if (Data.teamsize == 1) {
                ss.setLine(3, dsp.get("scoreboard.alive", p, Data.teams.size() + ""));
            } else {
                final int[] useralive = {0};
                Data.teams.forEach(entry -> {
                    useralive[0] += entry.getUsers().size();
                });
                ss.setLine(3, dsp.get("scoreboard.teamalive", p, useralive[0] + "", Data.teams.size() + ""));
            }
            ss.setLine(4, ChatColor.DARK_AQUA + "");

            showMates(ss, p);
            showEvent(ss, p);//Mates: 5-8 + 9: Free, //Events: 10 + 11: Free //Zone: 12 + 13: Free, Distance: 14
            showZone(ss, p);

            String distance = (int) (BorderManager.border.getCurrentRadius() - p.getLocation().distance(BorderManager.border.getCurrentMiddle())) + "";
            if (User.getUser(p) != null) {
                if (User.getUser(p).isAlive()) {
                    if (Data.estate == EventState.Confusion || Data.estate == EventState.Lostness) {
                        distance = "§e§k???";
                    }
                }
            }
            ss.setLine(14, dsp.get("scoreboard.distance", p, distance));
            if (!boards.containsKey(p)) {
                boards.put(p, ss);
            }
        });
    }

    public static void showMates(ScoreboardSign ss, Player p) {
        if (matesShowedOnScoreboard(p)) {
            User u = User.getUser(p);
            final int[] i = {0};
            u.getTeam().getUsers().forEach(entry -> {
                if (entry.isAlive()) {
                    if (!entry.equals(u)) {
                        if (i[0] < 3) {
                            if (entry.isKnocked()) {
                                ss.setLine(5 + i[0], u.getTeam().getColor() + entry.getPlayer().getDisplayName() + "§7(§4" + ((double) ((int) entry.getPlayer().getHealth()) / 2) + "❤§7)");
                                i[0]++;
                            } else {
                                ss.setLine(5 + i[0], u.getTeam().getColor() + entry.getPlayer().getDisplayName() + "§7(" + ((double) ((int) entry.getPlayer().getHealth()) / 2) + "§c❤§7)");
                                i[0]++;
                            }
                        }
                    }
                }
            });
            ss.setLine(9, ChatColor.GOLD + "");
        } else {
            for (int i = 5; i < 10; i++) {
                ss.removeLine(i);
            }
        }
    }

    public static void showZone(ScoreboardSign ss, Player p) {
        if (UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone") && !matesShowedOnScoreboard(p) || !UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone")) {
            if (BorderManager.border.getProgress() > 1) {
                ss.setLine(10, dsp.get("scoreboard.nextzone", p, ((int) ((BorderManager.border.getProgress() - 1) * 200)) + ""));
            } else {
                ss.setLine(10, dsp.get("scoreboard.shrink", p, ((int) (BorderManager.border.getProgress() * 100)) + ""));
            }
            ss.setLine(11, ChatColor.DARK_BLUE + "");
            return;
        }
        ss.removeLine(10);
        ss.removeLine(11);
    }

    public static void showEvent(ScoreboardSign ss, Player p) {
        if (!UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone") && !matesShowedOnScoreboard(p) || UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "baro_barSettingZone")) {
            if ((Data.nextEventTime - Data.timer) <= 60) {
                if (Data.nextEvent != EventState.None) {
                    ss.setLine(12, dsp.get("event.comingup.scoreboard", p, dsp.get("event." + Data.nextEvent.getName(), p), Math.round(((double) (Data.nextEventTime - Data.timer) / 60) * 100) + ""));
                } else {
                    ss.setLine(12, dsp.get("event.finished.scoreboard", p, dsp.get("event." + Data.estate.getName(), p), Math.round(100 - ((double) (Data.nextEventTime - Data.timer) / 60) * 100) + ""));
                }
                ss.setLine(13, ChatColor.DARK_RED + "");
                return;
            }
        }
        ss.removeLine(12);
        ss.removeLine(13);
    }


    public static boolean matesShowedOnScoreboard(Player p) {
        if (User.getUser(p) != null) {
            User u = User.getUser(p);
            if (u.hasLivingMates()) {
                return u.getLivingTeammatesAmount() > UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars");
            }
        }
        return false;
    }


}
