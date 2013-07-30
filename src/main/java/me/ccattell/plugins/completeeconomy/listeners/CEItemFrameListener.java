package me.ccattell.plugins.completeeconomy.listeners;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
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
            ItemFrame SellFrame = ((ItemFrame) event.getRightClicked());
            ItemStack SellStack = SellFrame.getItem();
            String SellItem = SellStack.getType().toString();
            MaterialData data = SellStack.getData();
            byte data_type = data.getData();
            String block = (data_type > 0) ? SellItem + ":" + data_type : SellItem;
            String Alias = plugin.configs.blockList.getString(block + ".alias");
            String BuyPrice = plugin.configs.blockList.getString(block + ".buy");
            if (Alias == null) {
                Alias = SellItem.replace('_', ' ').toLowerCase();
                Alias = WordUtils.capitalizeFully(Alias);
            }
            double x = event.getRightClicked().getLocation().getX();
            double y = event.getRightClicked().getLocation().getY();
            double z = event.getRightClicked().getLocation().getZ();
            double new_y = y - 1;
//            float direction = event.getRightClicked().getLocation().getYaw();

            BlockFace direction = SellFrame.getAttachedFace();
            byte dir_data;
            switch (direction) {
                case NORTH:
                    dir_data = 3;
                    break;
                case WEST:
                    dir_data = 5;
                    break;
                case SOUTH:
                    dir_data = 2;
                    break;
                default:
                    dir_data = 4;
                    break;
            }
            Block b = event.getRightClicked().getLocation().getBlock().getRelative(BlockFace.DOWN);
            if (b.getTypeId() == 0) {
                b.setTypeIdAndData(68, dir_data, true);
                Sign sign = (Sign) b.getState();
                sign.setLine(0, player.getName()); // what if player name is 16 chars long - you can only fit 15 chars
                sign.setLine(1, Alias); // aliases can only be 15 chars long!
                sign.setLine(2, "Buy: " + BuyPrice);
                sign.update();
                player.sendMessage(moduleName + "Create a " + Alias + " sign at " + x + ", " + new_y + ", " + z + " facing " + direction + " with a buy price of " + BuyPrice);
            }
        }
    }
}
