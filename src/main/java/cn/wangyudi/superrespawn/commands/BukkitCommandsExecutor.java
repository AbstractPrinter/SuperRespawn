package cn.wangyudi.superrespawn.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BukkitCommandsExecutor implements TabExecutor {
    private final JavaPlugin plugin;
    private final Connection databaseConnection;
    private final HashMap<String, Location> respawnLocations;
    private final HashMap<String, Integer> respawnWeights;

    public BukkitCommandsExecutor(JavaPlugin plugin, Connection databaseConnection, HashMap<String, Location> respawnLocations, HashMap<String, Integer> respawnWeights) {
        this.plugin = plugin;
        this.databaseConnection = databaseConnection;
        this.respawnLocations = respawnLocations;
        this.respawnWeights = respawnWeights;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(String.format("Type: /%s help", label));
            return true;
        }

        if (sender.hasPermission(String.format("superrespawn.%s", args[0]))) {
            if (args[0].equalsIgnoreCase("help") && args.length == 1) {
                sender.sendMessage("§6§o§lSuperReSpawn");
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "help", "显示帮助"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "mode <default|nearest|weight>", "设置重生模式"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "add <name>", "以自身为坐标添加一个重生点"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "del <name>", "删除一个重生点"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "set <name>", "设置某重生点"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "info", "获取插件信息"));
                return true;
            }
            if (args[0].equalsIgnoreCase("mode") && args.length == 2) {
                if (args[1].equalsIgnoreCase("default") || args[1].equalsIgnoreCase("nearest") || args[1].equalsIgnoreCase("weight")) {
                    plugin.getConfig().set("mode", args[1]);
                    sender.sendMessage("§a§oSucceed");
                } else {
                    sender.sendMessage("§c§oNo Mode");
                }
                return true;
            }
        } else {
            sender.sendMessage(String.format("§c§oNo Permission: superrespawn.%s", args[0]));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subCommandList = new ArrayList<>();
            subCommandList.add("help");
            subCommandList.add("mode");
            subCommandList.add("add");
            subCommandList.add("del");
            subCommandList.add("set");
            subCommandList.add("list");
            subCommandList.add("info");
            subCommandList.add("reload");
            return subCommandList;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("mode")) {
                List<String> subCommandList = new ArrayList<>();
                subCommandList.add("default");
                subCommandList.add("nearest");
                subCommandList.add("weight");
                return subCommandList;
            }
        }
        return null;
    }
}
