package me.ccattell.plugins.completeeconomy.listeners;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 *
 * @author Charlie
 */
public class CEItemFrameListener implements Listener {

    CompleteEconomy plugin;
    public String prefix;
    public String moduleName;

    public CEItemFrameListener(CompleteEconomy plugin) {
        this.plugin = plugin;
        prefix = this.plugin.configs.getShopConfig().getString("Shops.Prefix");
        moduleName = ChatColor.BLUE + prefix + ChatColor.RESET + " ";
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        //String name = player.getName();
        if (player.isSneaking() && event.getRightClicked() instanceof ItemFrame) {
            player.sendMessage(moduleName + "You already have a shop in edit mode, please save it first");
        }
    }
}
