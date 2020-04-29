package de.Iclipse.BARO.Functions.PlayerManagement;


import de.Iclipse.BARO.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class TeamManager {

    public static void createTeams() {
        new Team(ChatColor.BLACK, Material.BLACK_CONCRETE);
        new Team(ChatColor.DARK_GRAY, Material.GRAY_CONCRETE);
        new Team(ChatColor.WHITE, Material.WHITE_CONCRETE);
        new Team(ChatColor.AQUA, Material.CYAN_CONCRETE_POWDER);
        new Team(ChatColor.DARK_AQUA, Material.CYAN_CONCRETE);
        new Team(ChatColor.BLUE, Material.LIGHT_BLUE_CONCRETE);
        new Team(ChatColor.DARK_BLUE, Material.BLUE_CONCRETE);
        new Team(ChatColor.DARK_PURPLE, Material.PURPLE_CONCRETE);
        new Team(ChatColor.LIGHT_PURPLE, Material.MAGENTA_CONCRETE);
        new Team(ChatColor.RED, Material.RED_CONCRETE_POWDER);
        new Team(ChatColor.DARK_RED, Material.RED_CONCRETE);
        new Team(ChatColor.GOLD, Material.ORANGE_CONCRETE);
        new Team(ChatColor.YELLOW, Material.YELLOW_CONCRETE);
        new Team(ChatColor.GREEN, Material.LIME_CONCRETE);
        new Team(ChatColor.DARK_GREEN, Material.GREEN_CONCRETE);
        new Team(ChatColor.GRAY, Material.LIGHT_GRAY_CONCRETE);
    }

    public static void autoFill() {
        final int[] biggestTeam = {0};
        Data.teams.forEach(entry -> {
            if (entry.getUsers().size() > biggestTeam[0]) {
                biggestTeam[0] = entry.getUsers().size();
            }
        });
        Data.users.forEach(user -> {
            if (!user.isInATeam()) {
                if (biggestTeam[0] != 0) {
                    Data.teams.forEach(team -> {
                        if (!user.isInATeam()) {
                            if (team.getUsers().size() < biggestTeam[0]) {
                                team.addUser(user);
                            }
                        }
                    });
                } else {
                    Data.teams.get(0).addUser(user);
                    biggestTeam[0]++;
                }
            }
        });
    }

    public static void autoDelete() {
        if (Data.teams.size() > 0) {
            ArrayList<Team> toDelete = new ArrayList<>();
            for (Team entry : Data.teams) {
                if (entry.getUsers().size() == 0) {
                    toDelete.add(entry);
                }
            }
            toDelete.forEach(entry -> {
                Data.teams.remove(entry);
            });
        }
    }


}





