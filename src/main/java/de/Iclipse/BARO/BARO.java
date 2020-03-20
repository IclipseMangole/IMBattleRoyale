package de.Iclipse.BARO;

import com.google.common.base.Joiner;
import de.Iclipse.BARO.Functions.Commands.cmd_start;
import de.Iclipse.BARO.Functions.Listener;
import de.Iclipse.BARO.Functions.Scheduler;
import de.Iclipse.BARO.Functions.Tablist;
import de.Iclipse.BARO.MySQL.MySQL;
import de.Iclipse.BARO.Util.Command.BukkitCommand;
import de.Iclipse.BARO.Util.Command.IMCommand;
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
        MySQL.connect();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        registerListener();
        registerCommands();
        createTables();
        Data.tablist = new Tablist();
        Scheduler.startScheduler();
        Data.teamsize = 1;
        Data.teams = new ArrayList<>();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Scheduler.stopScheduler();
    }

    public void registerListener(){
        Bukkit.getPluginManager().registerEvents(new Listener(), this);
    }

    public void registerCommands(){
        register(new cmd_start(), this);
    }

    /*
    public void registerCompleter(){
            TabCompleter completer = new Completer();
            System.out.println(completer.toString());
            PluginCommand command = this.getCommand(cmd.name());
            System.out.println(command.toString());
            this.getCommand(cmd.name())
                    .setTabCompleter(completer);
    }
    */

    public void createTables(){
    }






    public static void register(Class functionClass, JavaPlugin plugin) {
        try {
            register(functionClass, functionClass.newInstance(), plugin);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Registriert Listener und Commands aus einer Klasse für ein Plugin
     *
     * @param function Object der Klasse welche registriert werden soll
     * @param plugin   Plugin für welches die Klasse registriert wird
     */
    public static void register(Object function, JavaPlugin plugin) {
        register(function.getClass(), function, plugin);
    }

    /**
     * Registriert Listener und Commands aus einer Klasse für ein Plugin
     *
     * @param functionClass Klasse welche registriert werden soll
     * @param function      Object der Klasse welche registriert werden soll
     * @param plugin        Plugin für welches die Klasse registriert wird
     */
    public static void register(Class functionClass, Object function, JavaPlugin plugin) {
        Method[] methods = functionClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(IMCommand.class))
                registerCommand(function, method, plugin);
        }

        if (function instanceof org.bukkit.event.Listener) {
            Bukkit.getPluginManager().registerEvents((org.bukkit.event.Listener) function, plugin);
        }
    }

    private static Map<String, Command> commandMap = new HashMap<>();
    private static List<Object[]> unavailableSubcommands = new ArrayList<>();

    private static void registerCommand(Object function, Method method, JavaPlugin plugin) {
        IMCommand cmd = method.getAnnotation(IMCommand.class);

        if (cmd.parent().length == 0) {
            BukkitCommand tBukkitCommand = new BukkitCommand(plugin, function, method, cmd);
            tBukkitCommand.register();
            commandMap.put(tBukkitCommand.getName(), tBukkitCommand);



            for (Object[] unavailableSubcommand : unavailableSubcommands) {
                Method oldMethod = (Method) unavailableSubcommand[1];
                IMCommand old = oldMethod.getAnnotation(IMCommand.class);
                if (old.parent()[0].equalsIgnoreCase(cmd.name()))
                    registerCommand(unavailableSubcommand[0], oldMethod, plugin);
            }

        } else {
            Command pluginCommand = commandMap.get(cmd.parent()[0]);
            if (pluginCommand == null) {
                unavailableSubcommands.add(new Object[]{function, method});
                Joiner.on(" ").join(cmd.parent() + " " + cmd.name(), cmd.parent()[0]);
            } else {
                if (pluginCommand instanceof BukkitCommand) {
                    ((BukkitCommand) pluginCommand).getProcessor().addSubCommand(cmd, function, method);
                } else {
                    Joiner.on(" ").join(cmd.parent() + " " + cmd.name(), cmd.parent()[0]);
                }
            }
        }
    }
}
