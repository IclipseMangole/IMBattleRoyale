package de.Iclipse.BARO;


import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.BARO.Functions.Tablist;
import de.Iclipse.BARO.Functions.Team;
import de.Iclipse.BARO.Functions.User;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Data {
    public static Plugin instance;
    public static GameState state;
    public static Tablist tablist;
    public static int defaultcountdown = 100;
    public static int countdown = defaultcountdown;
    public static int teamsize = 1;
    public static int minplayers = teamsize + 1;
    public static Dispatcher dsp;
    public static ResourceBundle langDE;
    public static ResourceBundle langEN;
    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Player> spawningPlayers = new ArrayList<>();
    //Elytra
    public static ArrayList<Player> flyingPlayers = new ArrayList<>();
    //Slow Fall
    public static ArrayList<Player> fallingPlayers = new ArrayList<>();
    public static int timer = 0;

    public static Location spawn(Player p) {
        Location loc = new Location(p.getWorld(), 145.5, 69.5, -447.5, -90.0f, 0.0f);
        return loc;
    }


}
