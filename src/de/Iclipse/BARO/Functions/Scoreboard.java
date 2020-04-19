package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Util.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static de.Iclipse.BARO.Data.dsp;

public class Scoreboard {
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
                if (Data.teamsize == 1) {
                    ss.setLine(1, dsp.get("scoreboard.kills", p, User.getUser(p).getKills() + ""));
                } else {
                    final int[] teamkills = {0};
                    User.getUser(p).getTeam().getUsers().forEach(entry -> {
                        teamkills[0] += entry.getKills();
                    });
                    ss.setLine(1, dsp.get("scoreboard.teamkills", p, User.getUser(p).getKills() + "", teamkills[0] + ""));
                }
            } else {
                ss.setLine(1, ChatColor.BLACK + "");
            }
            ss.setLine(2, ChatColor.BLUE + "");
            if (Data.teamsize == 1) {
                ss.setLine(3, dsp.get("scoreboard.alive", p, Data.teams.size() + ""));
            } else {
                final int[] useralive = {0};
                Data.teams.forEach(entry -> {
                    useralive[0] += entry.getUsers().size();
                });
                ss.setLine(3, dsp.get("scoreboard.teamalive", p, useralive + "", Data.teams.size() + ""));
            }
            ss.setLine(4, ChatColor.DARK_AQUA + "");
            if (BorderManager.border.getProgress() > 1) {
                ss.setLine(5, dsp.get("scoreboard.nextzone", p, ((int) ((BorderManager.border.getProgress() - 1) * 200)) + ""));
            } else {
                ss.setLine(5, dsp.get("scoreboard.shrink", p, ((int) (BorderManager.border.getProgress() * 100)) + ""));
            }
            ss.setLine(6, ChatColor.DARK_BLUE + "");
            ss.setLine(7, dsp.get("scoreboard.distance", p, (int) (BorderManager.border.getCurrentRadius() - p.getLocation().distance(BorderManager.border.getCurrentMiddle())) + ""));
            Location loc = BorderManager.border.getCurrentMiddle();
            ss.setLine(8, ChatColor.DARK_GRAY + "");
                /*
                ss.setLine(9, "Middle: " + loc.getBlockX() + "|" + loc.getBlockY() + "|" + loc.getBlockZ());
                ss.setLine(10, "Radius: " + (int) BorderManager.border.getCurrentRadius());
                 */
            if (!boards.containsKey(p)) {
                boards.put(p, ss);
            }
        });
    }
}
