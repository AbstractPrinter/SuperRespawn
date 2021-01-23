package cn.wangyudi.superrespawn.listeners;

import cn.wangyudi.superrespawn.tasks.DeadSpectator;
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
        String mode = plugin.getConfig().getString("mode");
        String respawnKey = null;
        Player player = event.getPlayer();
        Location respawnLoc = null;
        switch (mode) {
            // 根据最近传送点重生
            case "nearest":
                double minLength = 999999999;
                for (Map.Entry<String, Location> entry : this.respawnLocations.entrySet()) {
                    if (player.getWorld().equals(entry.getValue().getWorld()) && player.getLocation().distance(entry.getValue()) < minLength) {
                        minLength = player.getLocation().distance(entry.getValue());
                        respawnKey = entry.getKey();
                        respawnLoc = entry.getValue();
                    }
                }
                break;
            // 根据权重重生
            case "weight":
                if (this.respawnWeights.size() > 0) {
                    int max = 0;
                    for (Map.Entry<String, Integer> entry : this.respawnWeights.entrySet()) {
                        if (max < entry.getValue()) {
                            max = entry.getValue();
                            respawnKey = entry.getKey();
                        }
                    }
                    respawnLoc = this.respawnLocations.get(respawnKey);
                } else {
                    player.sendMessage("似乎没有设置过权重的重生点!");
                }
                break;
            // mode项错填或者填写了 default
            default:
                respawnLoc = event.getRespawnLocation();
        }
        event.setRespawnLocation(respawnLoc);
        new DeadSpectator(this.plugin, player, 10);
        this.sendPrompts(player, respawnKey);
    }

    private void sendPrompts(Player player, String respawnKey) {
        boolean isChat = plugin.getConfig().getBoolean("prompt.chat");
        boolean isTitle = plugin.getConfig().getBoolean("prompt.title");
        if (respawnKey != null) {
            if (isChat) {
                player.sendMessage(this.respawnPrompts.get(respawnKey)[0]);
            }
            if (isTitle) {
                player.sendTitle(this.respawnPrompts.get(respawnKey)[1], this.respawnPrompts.get(respawnKey)[2], 10, 70, 20);
            }
        }
    }
}
