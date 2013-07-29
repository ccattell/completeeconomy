package me.ccattell.plugins.completeeconomy.listeners;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

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
            ItemFrame SellFrame = ((ItemFrame)event.getRightClicked());
            ItemStack SellStack = SellFrame.getItem();
            String SellItem = SellStack.getType().toString();
            MaterialData data = SellStack.getData();
            byte data_type = data.getData();
            String block = (data_type > 0) ? SellItem + ":" + data_type : SellItem;
            String Alias = plugin.configs.blockList.getString(block + ".alias");
            double BuyPrice = plugin.configs.blockList.getDouble(block + ".buy");
            if(Alias == null){
                Alias = SellItem.replace('_', ' ').toLowerCase();
                Alias = WordUtils.capitalizeFully(Alias);
            }
            double x = event.getRightClicked().getLocation().getX();
            double y = event.getRightClicked().getLocation().getY();
            double z = event.getRightClicked().getLocation().getZ();
            double new_y = y-1;
            float direction = event.getRightClicked().getLocation().getYaw();

            //need to check to see if block at new_y is AIR, then place new sign filled with the following:
            //player name
            //Alias
            //Buy: BuyPrice
            //empty
            player.sendMessage(moduleName + "Create a "+ Alias + " sign at " + x + ", " + new_y + ", " + z + " facing " + direction + " with a buy price of " + BuyPrice);
        }
    }
}
