package de.Iclipse.BARO;

import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void registerListener() {
    }

    public void registerCommands() {
    }

    public void createTables() {
    }

    public void loadMap() {
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
