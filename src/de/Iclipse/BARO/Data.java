package de.Iclipse.BARO;

import de.Iclipse.BARO.Functions.Status;
import de.Iclipse.BARO.Functions.Tablist;
import de.Iclipse.BARO.Functions.Team;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    public static String symbol = "§8 » §7";
    public static String prefix = "§5BARO " + symbol;
    public static Tablist tablist;
    public static Plugin instance;
    public static Status status;
    public static int countdown;
    public static int teamsize;
    public static ArrayList<Team> teams;
}
