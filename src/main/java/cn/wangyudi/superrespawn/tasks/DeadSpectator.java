package cn.wangyudi.superrespawn.tasks;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author AbstractPrinter
 */
public class DeadSpectator extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final Player player;
    private int time;

    public DeadSpectator(JavaPlugin plugin,Player player, int time) {
        this.plugin = plugin;
        this.player = player;
        this.time = time;
        player.setGameMode(GameMode.SPECTATOR);
    }

    @Override
    public void run() {
        this.time--;
        if (time != 0) {
            player.sendTitle(String.valueOf(this.time), "", 10, 70, 20);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            cancel();
        }
    }

    public void start(){
        this.runTaskTimer(this.plugin,0,20L);
    }
}
