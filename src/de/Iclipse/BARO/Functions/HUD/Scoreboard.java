package de.Iclipse.BARO.Functions.HUD;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.ScoreboardSign;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

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
            if (User.getUser(p) != null) {
                User u = User.getUser(p);
                if (u.getTeam() != null) {
                    if (u.hasLivingMates()) {
                        if (matesShowedOnScoreboard(u)) {
                            final int[] i = {0};
                            u.getTeam().getUsers().forEach(entry -> {
                                if (entry.isAlive()) {
                                    if (!entry.equals(u)) {
                                        if (entry.isKnocked()) {
                                            ss.setLine(5 + i[0], u.getTeam().getColor() + entry.getPlayer().getDisplayName() + "§7(§4" + ((double) ((int) entry.getPlayer().getHealth()) / 2) + "❤§7)");
                                            i[0]++;
                                        } else {
                                            ss.setLine(5 + i[0], u.getTeam().getColor() + entry.getPlayer().getDisplayName() + "§7(" + ((double) ((int) entry.getPlayer().getHealth()) / 2) + "§c❤§7)");
                                            i[0]++;
                                        }
                                    }
                                }
                            });
                        } else {
                            for (int i = 5; i < 10; i++) {
                                ss.removeLine(i);
                            }
                            if (BorderManager.border.getProgress() > 1) {
                                ss.setLine(5, dsp.get("scoreboard.nextzone", p, ((int) ((BorderManager.border.getProgress() - 1) * 200)) + ""));
                            } else {
                                ss.setLine(5, dsp.get("scoreboard.shrink", p, ((int) (BorderManager.border.getProgress() * 100)) + ""));
                            }
                        }
                        ss.setLine(10, ChatColor.DARK_BLUE + "");
                    }
                }
            }
            ss.setLine(11, dsp.get("scoreboard.distance", p, (int) (BorderManager.border.getCurrentRadius() - p.getLocation().distance(BorderManager.border.getCurrentMiddle())) + ""));
            ss.setLine(12, ChatColor.DARK_GRAY + "");
            if (!boards.containsKey(p)) {
                boards.put(p, ss);
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


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        boards.get(e.getPlayer()).destroy();
    }

}
