package de.Iclipse.BARO;


import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.plugin.Plugin;

import java.util.ResourceBundle;

public class Data {
    public static Plugin instance;
    public static GameState state;
    public static int defaultcountdown = 200;
    public static int minplayers = 3;
    public static Dispatcher dsp;
    public static ResourceBundle langDE;
    public static ResourceBundle langEN;
}
