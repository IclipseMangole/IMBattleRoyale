package de.Iclipse.BARO.MySQL;

import de.Iclipse.BARO.Data;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 08.06.2019 at 21:09 o´ clock
 */
public class MySQL {

    public static File getMySQLFile() {
        return new File("plugins/" + Data.instance.getDescription().getName(), "mysql.yml");
    }

    public static FileConfiguration getMySQLFileConfiguration() {
        return YamlConfiguration.loadConfiguration(getMySQLFile());
    }

    public static void setStandardMySQL() {
        FileConfiguration cfg = getMySQLFileConfiguration();
        cfg.options().copyDefaults(true);
        cfg.addDefault("host", "localhost");
        cfg.addDefault("database", "IclipseMangole");
        cfg.addDefault("user", "root");
        cfg.addDefault("password", "dshchangE762");

        cfg.addDefault("prefix", "&5" + Data.instance.getDescription().getName() + " &3MySQL &8&7");
        try {
            cfg.save(getMySQLFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readMySQL() {
        FileConfiguration cfg = getMySQLFileConfiguration();
        HOST = cfg.getString("host");
        DATABASE = cfg.getString("database");
        USER = cfg.getString("user");
        PASSWORD = cfg.getString("password");
        prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix")) + " ";
    }


    private static String HOST;
    private static String DATABASE;
    private static String USER;
    private static String PASSWORD;
    private static String prefix;

    public static Connection conn;

    public MySQL(String database) {
        this.DATABASE = database;
        connect();
    }

    public static void connect() {
        setStandardMySQL();
        readMySQL();
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoReconnect=false", USER, PASSWORD);
            System.out.println(prefix + "Verbunden!");
        } catch (SQLException e) {
            System.out.println(prefix + "Keine Verbindung! Fehler: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println(prefix + "erfolgreich getrennt!");
            }

        } catch (SQLException e) {
            System.out.println(prefix + "Keine Verbindung! Fehler: " + e.getMessage());
        }
    }

    public static void update(String querry) {
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(querry);
            st.close();
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
    }

    public static ResultSet querry(String querry) {
        ResultSet rs = null;

        Statement st;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(querry);
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
        return rs;
    }

}
