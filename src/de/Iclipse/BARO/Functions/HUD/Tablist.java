package de.Iclipse.BARO.Functions.HUD;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class Tablist {
    static String header;
    static String footer;
    static String ranks;

    public static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Team a;
    private Team b;
    private Team c;


    private HashMap<Player, String> rankColor = new HashMap<>();


    public Tablist() {
        header = "§8«§5§lIM§r§fBARO§r§8 »";
        footer = "§7Server: §e" + Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getName();
        ranks = "§4Admin §cMod \n §3User";

        //Serverranks
        this.a = scoreboard.getTeam("1a") == null ? scoreboard.registerNewTeam("1a") : scoreboard.getTeam("1a");
        this.b = scoreboard.getTeam("2b") == null ? scoreboard.registerNewTeam("2b") : scoreboard.getTeam("2b");
        this.c = scoreboard.getTeam("3c") == null ? scoreboard.registerNewTeam("3c") : scoreboard.getTeam("3c");


        this.a.setPrefix("§7[§4Admin§7]§4 ");
        this.b.setPrefix("§7[§cMod§7]§c ");
        this.c.setPrefix("§3 ");
        this.a.setColor(ChatColor.getByChar('4'));
        this.b.setColor(ChatColor.getByChar('c'));
        this.c.setColor(ChatColor.getByChar('3'));


    }


    public void setTablist(Player p) {
        p.setPlayerListHeader(header);
        p.setPlayerListFooter(ranks);
    }


    public void setPlayer(Player p) {
        String team = "";
        if (p.hasPermission("im.color.admin")) {
            team = "1a";
        } else if (p.hasPermission("im.color.mod")) {
            team = "2b";
        } else {
            team = "3c";
        }
        scoreboard.getTeams().forEach(entry -> {
            if (entry.hasEntry(p.getName())) {
                entry.removeEntry(p.getName());
            }
        });
        if (!scoreboard.getTeam(team).hasEntry(p.getName()))
            scoreboard.getTeam(team).addEntry(p.getName());
        if (!scoreboard.getTeam(team).hasEntry(p.getName())) scoreboard.getTeam(team).addEntry(p.getName());
        rankColor.put(p, scoreboard.getTeam(team).getPrefix());

        String name = "";
        name = scoreboard.getTeam(team).getPrefix() + p.getName();
        ChatColor.translateAlternateColorCodes('§', name);

        p.setPlayerListName(name);
        p.setDisplayName(name);
        p.setCustomName(name);
        p.setCustomNameVisible(true);
        p.setScoreboard(scoreboard);
        Bukkit.getScheduler().runTaskTimer(Data.instance, () -> {
            Bukkit.getOnlinePlayers().forEach(pl -> pl.setScoreboard(scoreboard));
        }, 10, 10);
    }

    public void updatePlayer(Player p) {
        String name = "";
        name = User.getUser(p).getTeam().getTeam().getPrefix() + p.getName();
        ChatColor.translateAlternateColorCodes('§', name);

        p.setPlayerListName(name);
        p.setDisplayName(name);
        p.setCustomName(name);
        p.setCustomNameVisible(true);
        p.setScoreboard(scoreboard);
        Bukkit.getScheduler().runTaskTimer(Data.instance, () -> {
            Bukkit.getOnlinePlayers().forEach(pl -> pl.setScoreboard(scoreboard));
        }, 10, 10);
    }

}
