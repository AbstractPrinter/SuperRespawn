package cn.wangyudi.superrespawn.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AbstractPrinter
 */
public class RespawnListener implements Listener {
    private final JavaPlugin plugin;
    private final HashMap<String, Location> respawnLocations;
    private final HashMap<String, Integer> respawnWeights;
    private final HashMap<String, String[]> respawnPrompts;

    public RespawnListener(JavaPlugin plugin, HashMap<String, Location> respawnLocations, HashMap<String, Integer> respawnWeights, HashMap<String, String[]> respawnPrompts) {
        this.plugin = plugin;
        this.respawnLocations = respawnLocations;
        this.respawnWeights = respawnWeights;
        this.respawnPrompts = respawnPrompts;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        try {
            String mode = plugin.getConfig().getString("mode");
            String respawnKey = null;
            boolean isChat = plugin.getConfig().getBoolean("prompt.chat");
            boolean isTitle = plugin.getConfig().getBoolean("prompt.title");
            Player player = event.getPlayer();
            Location respawnLoc = null;
            switch (mode) {
                // 根据最近传送点重生
                case "nearest":
                    double minLength = 2147483647;
                    for (Map.Entry<String, Location> entry : this.respawnLocations.entrySet()) {
                        if (player.getLocation().distance(entry.getValue()) < minLength) {
                            respawnKey = entry.getKey();
                            respawnLoc = entry.getValue();
                        }
                    }
                    break;
                // 根据权重重生
                case "weight":
                    if (this.respawnWeights.size() > 0) {
                        int max = 0;
                        String maxKey = null;
                        for (Map.Entry<String, Integer> entry : this.respawnWeights.entrySet()) {
                            if (max < entry.getValue()) {
                                max = entry.getValue();
                                maxKey = entry.getKey();
                            }
                        }
                        respawnKey = maxKey;
                        respawnLoc = this.respawnLocations.get(maxKey);
                    } else {
                        player.sendMessage("似乎没有设置过权重的重生点!");
                    }
                    break;
                // mode项错填或者填写了 default
                default:
                    respawnKey = "default";
                    respawnLoc = event.getRespawnLocation();
            }

            String respawnDefault = "default";
            assert respawnKey != null;
            if (respawnKey.equals(respawnDefault)) {
                if (isChat) {
                    player.sendMessage(this.respawnPrompts.get(respawnKey)[0]);
                }
                if (isTitle) {
                    player.sendTitle(this.respawnPrompts.get(respawnKey)[1], this.respawnPrompts.get(respawnKey)[1], 10, 70, 20);
                }
            } else {
                if (isChat) {
                    player.sendMessage("Default");
                }
                if (isTitle) {
                    player.sendTitle("Default", "默认出生点", 10, 70, 20);
                }
            }
            event.setRespawnLocation(respawnLoc);
        } catch (Exception e) {
            event.getPlayer().sendMessage("因为出现异常，所以本次重生事件并没有被插件接管，请联系管理员检查出现的问题");
            plugin.getLogger().info(e.getMessage());
            plugin.getLogger().info("出现异常，请在本项目 Github Issues 提交本次出错信息");
            plugin.getLogger().info("https://github.com/AbstractPrinter/SuperRespawn/issues");
            plugin.getLogger().info("请附上异常信息，感谢你为这个插件做出的贡献！");
        }
    }
}
