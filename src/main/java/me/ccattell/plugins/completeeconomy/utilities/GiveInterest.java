package me.ccattell.plugins.completeeconomy.utilities;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
 
public class GiveInterest {
    public void interest(){
        String enabled = CompleteEconomy.plugin.getConfig().getString("System.Interest.Enabled");
        String online = CompleteEconomy.plugin.getConfig().getString("System.Interest.Online");
        String announce = CompleteEconomy.plugin.getConfig().getString("System.Announce.Enabled");
        long interval = CompleteEconomy.plugin.getConfig().getInt("System.Interval.Seconds") * 20;
        float cutoff = CompleteEconomy.plugin.getConfig().getInt("System.Amount.Cutoff");
        float percent = CompleteEconomy.plugin.getConfig().getInt("System.Amount.Percent");

        CompleteEconomy.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(CompleteEconomy.plugin, new Runnable() {
            @Override
            public void run() {
                CompleteEconomy.plugin.getServer().getConsoleSender().sendMessage("Give Interest");
                
            }
        }, 60L, interval);
    }
}