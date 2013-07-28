package me.ccattell.plugins.completeeconomy.listeners;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Charlie
 */
public class CEItemFrameListener implements Listener {

    CompleteEconomy plugin;

    public CEItemFrameListener(CompleteEconomy plugin) {
        this.plugin = plugin;
    }
    public String prefix = plugin.configs.getShopConfig().getString("Shops.Prefix");
    public String moduleName = ChatColor.BLUE + prefix + ChatColor.RESET + " ";

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Action action = event.getAction();
        if (player.isSneaking() && action.equals(Action.RIGHT_CLICK_BLOCK)) {
            player.sendMessage(moduleName + "You already have a shop in edit mode, please save it first");
        
        }
    }
}
