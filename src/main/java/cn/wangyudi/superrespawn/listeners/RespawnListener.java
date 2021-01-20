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
    public void onNearestSpawn(PlayerRespawnEvent event) {
        String respawnName = null;
        Location respawnLocation = null;
        if ("nearest".equals(plugin.getConfig().getString("mode"))) {
            double min = 999999999;
            Player player = event.getPlayer();
            for (Map.Entry<String, Location> spawnLocationsEntry : respawnLocations.entrySet()) {
                if (player.hasPermission(String.format("superrespawn.spawn.%s", spawnLocationsEntry.getKey())) && player.getWorld().equals(spawnLocationsEntry.getValue().getWorld()) && min > spawnLocationsEntry.getValue().distance(player.getLocation())) {
                    min = spawnLocationsEntry.getValue().distance(player.getLocation());
                    respawnName = spawnLocationsEntry.getKey();
                    respawnLocation = spawnLocationsEntry.getValue();
                }
            }
        }
        if ("weight".equals(plugin.getConfig().getString("mode"))) {
            int max = 0;
            Player player = event.getPlayer();
            for (Map.Entry<String, Integer> spawnWeightsEntry : respawnWeights.entrySet()) {
                if (player.hasPermission(String.format("superrespawn.spawn.%s", spawnWeightsEntry.getKey())) && player.getWorld().equals(respawnLocations.get(spawnWeightsEntry.getKey()).getWorld()) && max < spawnWeightsEntry.getValue()) {
                    max = spawnWeightsEntry.getValue();
                    respawnName = spawnWeightsEntry.getKey();
                    respawnLocation = respawnLocations.get(spawnWeightsEntry.getKey());
                }
            }
        }
        if ("default".equals(plugin.getConfig().getString("mode"))) {
            Player player = event.getPlayer();
            respawnLocation = player.getWorld().getSpawnLocation();
        }
        if (plugin.getConfig().getBoolean("prompt.chat")) {
            Player player = event.getPlayer();
            player.sendMessage(respawnPrompts.get(respawnName)[0]);
        }
        if (plugin.getConfig().getBoolean("prompt.title")) {
            Player player = event.getPlayer();
            player.sendTitle(respawnPrompts.get(respawnName)[1], respawnPrompts.get(respawnName)[2]);
        }
        event.setRespawnLocation(respawnLocation);

    }
}
