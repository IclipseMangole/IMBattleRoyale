package de.Iclipse.BARO;

import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BARO extends JavaPlugin{
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
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void registerListener(){
    }

    public void registerCommands(){
    }

    public void createTables(){
    }

    public void loadMap(){
        File existing = new File("/home/IMNetzwerk/Welten/BAROMap");
        File toCreate = new File(Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath().getBytes() + "/world");
        try {
            copyFilesInDirectory(existing, toCreate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getServer().createWorld(new WorldCreator("BAROMap"));
    }

    private static void copyFilesInDirectory(File from, File to) throws IOException {
        if(!to.exists()) {
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
