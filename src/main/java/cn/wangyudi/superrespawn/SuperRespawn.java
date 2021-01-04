package cn.wangyudi.superrespawn;

import cn.wangyudi.superrespawn.commands.BukkitCommandsExecutor;
import cn.wangyudi.superrespawn.listeners.RespawnListener;
import cn.wangyudi.superrespawn.sql.Dber;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public final class SuperRespawn extends JavaPlugin {
    private static Connection databaseConnection;
    private static String databaseFilename;
    private static HashMap<String, Location> respawnLocations;
    private static HashMap<String, Integer> respawnWeights;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        databaseFilename = getDataFolder() + "/data.db";
        if (!new File(databaseFilename).exists()) {
            saveResource("data.db", false);
        }

        try {
            databaseConnection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilename);
            String sql;
            Statement stmt = databaseConnection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS srspawn_locations" +
                    "(spawn_name VARCHAR(255) PRIMARY KEY," +
                    "spawn_world VARCHAR(255)," +
                    "spawn_x DOUBLE," +
                    "spawn_y DOUBLE," +
                    "spawn_z DOUBLE," +
                    "spawn_pitch SINGLE," +
                    "spawn_yaw SINGLE)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS srspawn_weights" +
                    "(spawn_name VARCHAR(255) PRIMARY KEY," +
                    "spawn_weight INT)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS srspawn_prompts" +
                    "(spawn_name VARCHAR(255) PRIMARY KEY," +
                    "prompt_title TEXT," +
                    "prompt_subtitle TEXT," +
                    "prompt_chat TEXT," +
                    "prompt_actionbar TEXT)";
            stmt.executeUpdate(sql);
            stmt.close();
            Dber dber = new Dber(this,databaseConnection);
            respawnLocations = dber.getRespawnLocations();
            respawnWeights = dber.getRespawnWeights();

        } catch (SQLException e) {
            getLogger().info("§c" + e.getMessage());
            getLogger().info("§c出现致命的数据问题,本插件将会关闭服务器保证数据安全!");
            getServer().shutdown();
        }

        getServer().getPluginCommand("superrespawn").setExecutor(new BukkitCommandsExecutor(this, databaseConnection, respawnLocations, respawnWeights));
        getServer().getPluginManager().registerEvents(new RespawnListener(this, respawnLocations, respawnWeights), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            databaseConnection.close();
        } catch (SQLException e) {
            getLogger().info(e.getMessage());
        }
    }
}
