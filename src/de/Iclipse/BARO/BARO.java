package de.Iclipse.BARO;

import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.BARO.Functions.Listener.GameListener;
import de.Iclipse.BARO.Functions.Listener.LobbyListener;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static de.Iclipse.BARO.Data.langDE;
import static de.Iclipse.BARO.Data.langEN;

public class BARO extends JavaPlugin {
    @Override
    public void onLoad() {
        super.onLoad();
        Data.instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        registerListener();
        registerCommands();
        createTables();
        loadMap();
        loadResourceBundles();
        Data.state = GameState.Lobby;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void registerListener() {
        IMAPI.register(new LobbyListener(), this);
        IMAPI.register(new GameListener(), this);
    }

    public void registerCommands() {
    }

    public void createTables() {
    }

    public void loadMap() {
        Bukkit.unloadWorld("world", false);
        File from = new File("/home/Welten/BAROMap");
        File to = new File(Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + "/BAROMap");
        if(to.exists()){
            to.delete();
        }
        try {
            copyFilesInDirectory(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getServer().createWorld(new WorldCreator("BAROMap"));
    }

    public void loadResourceBundles(){
        try {
            HashMap<String, ResourceBundle> langs = new HashMap<>();
            langDE = ResourceBundle.getBundle("i18n.langDE");
            langEN = ResourceBundle.getBundle("i18n.langEN");
            langs.put("DE", langDE);
            langs.put("EN", langEN);
            Data.dsp = new Dispatcher(this,
                    langs);
        } catch(MissingResourceException e){
            e.printStackTrace();
            de.Iclipse.IMAPI.Data.dispatching = false;
        } catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("Reload oder Bundle not found!");
            de.Iclipse.IMAPI.Data.dispatching = false;
        }
    }





    private static void copyFilesInDirectory(File from, File to) throws IOException {
        if (!to.exists()) {
            to.mkdirs();
        }
        for (File file : from.listFiles()) {
            if (file.isDirectory()) {
                copyFilesInDirectory(file, new File(to.getAbsolutePath() + "/" + file.getName()));
            } else {
                File n = new File(to.getAbsolutePath() + "/" + file.getName());
                Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }


}
