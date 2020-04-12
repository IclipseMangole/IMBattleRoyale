package de.Iclipse.BARO;


import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.BARO.Functions.Tablist;
import de.Iclipse.BARO.Functions.Team;
import de.Iclipse.BARO.Functions.User;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
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
    public static int minplayers = 3;
    public static int teamsize = 2;
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

}
