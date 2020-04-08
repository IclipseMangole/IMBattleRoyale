package de.Iclipse.BARO;


import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.BARO.Functions.Tablist;
import de.Iclipse.BARO.Functions.Team;
import de.Iclipse.BARO.Functions.User;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Data {
    public static Plugin instance;
    public static GameState state;
    public static Tablist tablist;
    public static int defaultcountdown = 200;
    public static int minplayers = 3;
    public static int teamsize = 2;
    public static Dispatcher dsp;
    public static ResourceBundle langDE;
    public static ResourceBundle langEN;
    public static ArrayList<Team> teams;
    public static ArrayList<User> users;

}
