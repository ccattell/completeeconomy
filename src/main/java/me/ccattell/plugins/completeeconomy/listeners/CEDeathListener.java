package me.ccattell.plugins.completeeconomy.listeners;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
/**
 *
 * @author Charlie
 */
public class CEDeathListener implements Listener {
    CEQueryFactory qf = new CEQueryFactory();
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean purgeondeath = CompleteEconomy.plugin.getConfig().getBoolean("System.Currency.PurgeOnDeath");
        if(purgeondeath){
            qf.killBalance("cash", event.getEntity().getName());
            String name = event.getEntity().getName();
            event.getEntity().sendMessage("You Suck, Grandmaster " + name + "!");
        }
    }
}
