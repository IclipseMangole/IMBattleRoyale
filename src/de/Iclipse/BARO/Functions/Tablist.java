package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 09.06.2019 at 11:47 o´ clock
 */
public class Tablist {
    static String header;
    static String footer;

    public final Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();


    private HashMap<Player, String> rankColor = new HashMap<>();


    public Tablist() {
        header = "§8«  §l§5Minecraft BARO§8  »";
        footer = "§7Hosted by: §bIclipse §8& §7MangoleHD";
    }


    public void setTablist(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        p.setPlayerListHeader(header);
        p.setPlayerListFooter(footer);
    }


    public void setPlayer(Player p) {
        String team = "";
        team = "";
        if (!sb.getTeam(team).hasPlayer(Bukkit.getOfflinePlayer(p.getUniqueId())))
            sb.getTeam(team).addPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
        if (!sb.getTeam(team).hasEntry(p.getName())) sb.getTeam(team).addEntry(p.getName());
        rankColor.put(p, sb.getTeam(team).getPrefix());

        String name = "";
        name = String.valueOf(sb.getTeam(team).getPrefix()) + p.getName();
        ChatColor.translateAlternateColorCodes('§', name);

        p.setPlayerListName(name);
        p.setDisplayName(name);
        p.setCustomName(name);
        p.setCustomNameVisible(true);
        p.setScoreboard(sb);
        Bukkit.getScheduler().runTaskTimer(Data.instance, () -> {
            Bukkit.getOnlinePlayers().forEach(pl -> pl.setScoreboard(sb));
        }, 1, 1);
    }



}
