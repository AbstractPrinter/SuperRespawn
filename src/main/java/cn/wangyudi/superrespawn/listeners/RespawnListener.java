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

public class RespawnListener implements Listener {
    private final JavaPlugin plugin;
    private final HashMap<String, Location> respawnLocations;
    private final HashMap<String, Integer> respawnWeights;

    public RespawnListener(JavaPlugin plugin, HashMap<String, Location> respawnLocations, HashMap<String, Integer> respawnWeights) {
        this.plugin = plugin;
        this.respawnLocations = respawnLocations;
        this.respawnWeights = respawnWeights;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNearestSpawn(PlayerRespawnEvent event) {
        String respawnName = null;
        Location respawnLocation = null;
        if (plugin.getConfig().getString("mode").equals("nearest")) {
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
        if (plugin.getConfig().getString("mode").equals("weight")) {
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
        if (plugin.getConfig().getString("mode").equals("default")) {
            Player player = event.getPlayer();
            respawnLocation = player.getWorld().getSpawnLocation();
        }
        event.setRespawnLocation(respawnLocation);

    }
}
