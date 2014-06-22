package me.ccattell.plugins.cnu.listeners;

import me.ccattell.plugins.cnu.CompleteNovusUtilities;
import me.ccattell.plugins.cnu.database.QueryFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * @author Charlie
 */
public class DeathListener implements Listener {

    CompleteNovusUtilities plugin;

    public DeathListener(CompleteNovusUtilities plugin) {
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
