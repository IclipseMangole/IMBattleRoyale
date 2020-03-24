package de.Iclipse.BARO;

import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
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


}
