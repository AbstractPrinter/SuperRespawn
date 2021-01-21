package cn.wangyudi.superrespawn.commands;

import cn.wangyudi.superrespawn.sql.Dber;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AbstractPrinter
 */
public class BukkitCommandsExecutor implements TabExecutor {
    private final JavaPlugin plugin;
    private final Connection databaseConnection;
    private HashMap<String, Location> respawnLocations;
    private HashMap<String, Integer> respawnWeights;
    private HashMap<String, String[]> respawnPrompts;

    public BukkitCommandsExecutor(JavaPlugin plugin, Connection databaseConnection, HashMap<String, Location> respawnLocations, HashMap<String, Integer> respawnWeights, HashMap<String, String[]> respawnPrompts) {
        this.plugin = plugin;
        this.databaseConnection = databaseConnection;
        this.respawnLocations = respawnLocations;
        this.respawnWeights = respawnWeights;
        this.respawnPrompts = respawnPrompts;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(String.format("Type: /%s help", label));
            return true;
        }

        if (sender.hasPermission(String.format("superrespawn.command.%s", args[0]))) {
            if ("help".equalsIgnoreCase(args[0]) && args.length == 1) {
                sender.sendMessage("§6§o§lSuperReSpawn");
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "help", "显示帮助"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "mode <default|nearest|weight>", "设置重生模式"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "add <name>", "以自身为坐标添加一个重生点"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "del <name>", "删除一个重生点"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "set <name>", "设置某重生点"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "list", "列出所有重生点"));
                sender.sendMessage(String.format("§6/%s %s: §f%s", label, "reload", "重载插件配置/数据文件"));
                return true;
            }
            if ("mode".equalsIgnoreCase(args[0]) && args.length == 2) {
                if ("default".equalsIgnoreCase(args[1]) || "nearest".equalsIgnoreCase(args[1]) || "weight".equalsIgnoreCase(args[1])) {
                    plugin.getConfig().set("mode", args[1]);
                    sender.sendMessage("§a§oSucceed");
                } else {
                    sender.sendMessage("§c§oNo Mode");
                }
                return true;
            }
            if ("add".equalsIgnoreCase(args[0]) && args.length == 2) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Dber dber = new Dber(plugin, databaseConnection);
                    if (respawnLocations.containsKey(args[1])) {
                        dber.updateLoc(args[1], player.getLocation());
                        respawnLocations.put(args[1], player.getLocation());
                        player.sendMessage("§a§oUpdate Succeed");
                    } else {
                        dber.insertLoc(args[1], player.getLocation())
                                .insertWeight(args[1], 0)
                                .insertPrompt(args[1]);
                        respawnLocations.put(args[1], player.getLocation());
                        respawnWeights.put(args[1], 0);
                        respawnPrompts.put(args[1], new String[]{"未设置","未设置","未设置"});
                        player.sendMessage("§a§oInsert Succeed");
                    }
                } else {
                    sender.sendMessage("§c§oOnly the player can execute this command");
                }
                return true;
            }
            if ("del".equalsIgnoreCase(args[0]) && args.length == 2) {
                if (respawnLocations.containsKey(args[1])) {
                    Dber dber = new Dber(plugin, databaseConnection);
                    dber.delLoc(args[1])
                            .delWeight(args[1])
                            .delPrompt(args[1]);
                    respawnLocations.remove(args[1]);
                    respawnWeights.remove(args[1]);
                    respawnPrompts.remove(args[1]);
                    sender.sendMessage("§a§oDelete Succeed");
                } else {
                    sender.sendMessage("§c§oThe respawn not found");
                }
                return true;
            }
            if ("set".equalsIgnoreCase(args[0]) && args.length == 2) {
                if (respawnLocations.containsKey(args[1])) {
                    sender.sendMessage(String.format("§6/%s %s %s %s: §f%s", label, "set", args[1], "weight <Integer>", "设置权重"));
                    sender.sendMessage(String.format("§6/%s %s %s %s: §f%s", label, "set", args[1], "chat <message>", "设置消息框提示"));
                    sender.sendMessage(String.format("§6/%s %s %s %s: §f%s", label, "set", args[1], "title <message>", "设置TITLE提示"));
                    sender.sendMessage(String.format("§6/%s %s %s %s: §f%s", label, "set", args[1], "subtitle <message>", "设置SUBTITLE提示"));
                } else {
                    sender.sendMessage("§c§oThe respawn not found");
                }
                return true;
            }
            if ("set".equalsIgnoreCase(args[0]) && args.length == 4) {
                if (respawnLocations.containsKey(args[1])) {
                    if ("weight".equalsIgnoreCase(args[2])) {
                        Dber dber = new Dber(plugin, databaseConnection);
                        dber.updateWeight(args[1], Integer.valueOf(args[3]));
                        respawnWeights.put(args[1], Integer.valueOf(args[3]));
                        sender.sendMessage("§a§oUpdate Succeed");
                    }
                    if ("chat".equalsIgnoreCase(args[2])) {
                        Dber dber = new Dber(plugin, databaseConnection);
                        String str = args[3].replace('&', '§');
                        dber.updatePromptChat(args[1], str);
                        String[] nr = respawnPrompts.get(args[1]);
                        nr[0] = str;
                        respawnPrompts.put(args[1], nr);
                        sender.sendMessage("§a§oUpdate Succeed");
                    }
                    if ("title".equalsIgnoreCase(args[2])) {
                        Dber dber = new Dber(plugin, databaseConnection);
                        String str = args[3].replace('&', '§');
                        dber.updatePromptTitle(args[1], str);
                        String[] nr = respawnPrompts.get(args[1]);
                        nr[1] = str;
                        respawnPrompts.put(args[1], nr);
                        sender.sendMessage("§a§oUpdate Succeed");
                    }
                    if ("subtitle".equalsIgnoreCase(args[2])) {
                        Dber dber = new Dber(plugin, databaseConnection);
                        String str = args[3].replace('&', '§');
                        dber.updatePromptSubTitle(args[1], str);
                        String[] nr = respawnPrompts.get(args[1]);
                        nr[2] = str;
                        respawnPrompts.put(args[1], nr);
                        sender.sendMessage("§a§oUpdate Succeed");
                    }
                } else {
                    sender.sendMessage("§c§oThe respawn not found");
                }
                return true;
            }
            if ("list".equalsIgnoreCase(args[0])) {
                sender.sendMessage("§6§o§lSuperRespawn List");
                for (Map.Entry<String, Location> respawnLocationsEntry : respawnLocations.entrySet()) {
                    sender.sendMessage(String.format("§c%s§f:\n§fWeight: %s§f,\nChat: %s§f,\nTitle: %s§f,\nSubTitle: %s",
                            respawnLocationsEntry.getKey(),
                            respawnWeights.get(respawnLocationsEntry.getKey()),
                            respawnPrompts.get(respawnLocationsEntry.getKey())[0],
                            respawnPrompts.get(respawnLocationsEntry.getKey())[1],
                            respawnPrompts.get(respawnLocationsEntry.getKey())[2]));
                }
                return true;
            }
            if ("reload".equalsIgnoreCase(args[0])) {
                Dber dber = new Dber(plugin, databaseConnection);
                respawnLocations = dber.getRespawnLocations();
                respawnWeights = dber.getRespawnWeights();
                respawnPrompts = dber.getRespawnPrompts();
                plugin.reloadConfig();
                sender.sendMessage("§a§oReloaded");
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
            subCommandList.add("reload");
            return subCommandList;
        }
        if (args.length == 2) {
            if ("mode".equalsIgnoreCase(args[0])) {
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
