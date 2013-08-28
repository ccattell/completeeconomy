package me.ccattell.plugins.completeeconomy.listeners;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.QueryFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * @author Charlie
 */
public class DeathListener implements Listener {

    CompleteEconomy plugin;

    public DeathListener(CompleteEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String name = event.getEntity().getName();
        boolean purgeondeath = plugin.getConfig().getBoolean("System.Currency.PurgeOnDeath");
        if (purgeondeath) {
            new QueryFactory().killBalance("cash", name);
        }
        event.getEntity().sendMessage("You Suck, Grandmaster " + name + "!");
    }
}
