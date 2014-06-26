package me.ccattell.plugins.cnu.commands;

import java.util.HashMap;
import static me.ccattell.plugins.cnu.CompleteNovusUtilities.plugin;
import me.ccattell.plugins.cnu.database.QueryFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class ShopCommand implements CommandExecutor {
    
    public boolean ShopsEnabled = plugin.configs.getShopConfig().getBoolean("Shops.Enabled");
    public String prefix = plugin.configs.getShopConfig().getString("Shops.Prefix");
    public String moduleName = ChatColor.BLUE + prefix + ChatColor.RESET + " ";
    QueryFactory qf = new QueryFactory();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

          HashMap<String, Object> setw = new HashMap<String, Object>();
          HashMap<String, Object> seta = new HashMap<String, Object>();

          if (cmd.getName().equalsIgnoreCase("shop") || cmd.getName().equalsIgnoreCase("shops")) {
            if (ShopsEnabled) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.sendMessage(moduleName + "The shop command cannot be used from the console!");
                    return true;
                }
                if (!sender.hasPermission("cnu.shop")) {
                    sender.sendMessage(moduleName + "You don't have permission to use shops!");
                    return true;
                } else {
                    int count = args.length;
                    if (count == 0) {
                        player.sendMessage(moduleName + "Not Enough Arguments");
                        return true;
                    }
                    if ((args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("add")) && count > 1) {
                        int shopsInEdit = qf.checkPLayerShops(player.getName());
                        if(shopsInEdit != 0){
                            player.sendMessage(moduleName + "You already have a shop in edit mode, please save it first");
                            return true;
                        }
                        if (count < 2) {
                            return true;
                        }
                        StringBuilder buf = new StringBuilder(args[1]);
                        for (int i = 2; i < count; i++) {
                            buf.append(" ").append(args[i]);
                        }
                        String tmp = buf.toString();
                      
                        String shopCheck = qf.checkShop(tmp,player.getName());
                        if(shopCheck.equalsIgnoreCase("none")){
                            setw.put("player_name", player.getName());
                            setw.put("shop_name", tmp);
                            setw.put("status", "edit");
                            qf.doInsert("CEShops", setw);
                            player.sendMessage(moduleName + "Created shop '" + tmp + "'");
                            return true;
                        }else{
                            player.sendMessage(moduleName + "'" + tmp + "' already exists, please choose another name");
                            return true;
                        }
                    }else if ((args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("destroy") || args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("remove")) && count > 1) {
                        if (count < 2) {
                            return true;
                        }
                        StringBuilder buf = new StringBuilder(args[1]);
                        for (int i = 2; i < count; i++) {
                            buf.append(" ").append(args[i]);
                        }
                        String tmp = buf.toString();
                      
                        String shopCheck = qf.checkShop(tmp,player.getName());
                        if(shopCheck.equalsIgnoreCase("none")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not exist, are you sure you have the right name?");
                            return true;
                        }else if(shopCheck.equalsIgnoreCase("other")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not belong to you, what are you trying to do?");
                            return true;
                        }else{
                            //Return all itemframes and inventory to player
                            //Detroy itemframes and signson the wall
                            setw.put("player_name", player.getName());
                            setw.put("shop_name", tmp);
                            qf.doDelete("CEShops", setw);
                            player.sendMessage(moduleName + "Deleted '" + tmp + "'");
                            return true;
                        }
                    } else if ((args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e") || args[0].equalsIgnoreCase("change") || args[0].equalsIgnoreCase("update")) && count > 1) {
                        if (count < 2) {
                            return true;
                        }
                        int shopsInEdit = qf.checkPLayerShops(player.getName());
                        if(shopsInEdit != 0){
                            player.sendMessage(moduleName + "You already have a shop in edit mode, please save it first");
                            return true;
                        }
                        StringBuilder buf = new StringBuilder(args[1]);
                        for (int i = 2; i < count; i++) {
                            buf.append(" ").append(args[i]);
                        }
                        String tmp = buf.toString();
                  
                        String shopCheck = qf.checkShop(tmp,player.getName());
                        if(shopCheck.equalsIgnoreCase("none")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not exist, are you sure you have the right name?");
                            return true;
                        }else if(shopCheck.equalsIgnoreCase("other")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not belong to you, what are you trying to do?");
                            return true;
                        }else{
                            String current_status = qf.checkShopStatus(tmp);
                            if(current_status.equalsIgnoreCase("edit")){
                                player.sendMessage(moduleName + "'" + tmp + "' is already in edit mode");
                                return true;
                            }
                            setw.put("player_name", player.getName());
                            setw.put("shop_name", tmp);
                            seta.put("status", "edit");
                            qf.doUpdate("CEShops", seta, setw);
                            player.sendMessage(moduleName + "'" + tmp + "' is now in edit mode");
                            //broadcast closed message to all players?????
                            return true;
                        }
                    } else if ((args[0].equalsIgnoreCase("setowner") || args[0].equalsIgnoreCase("changeowner") || args[0].equalsIgnoreCase("owner") || args[0].equalsIgnoreCase("o") || args[0].equalsIgnoreCase("transfer") || args[0].equalsIgnoreCase("t"))&& count > 1) {
                        if (count < 3) {
                            return true;
                        }
                        StringBuilder buf = new StringBuilder(args[1]);
                        for (int i = 2; i < count-1; i++) {
                            buf.append(" ").append(args[i]);
                        }
                        String tmp = buf.toString();
                      
                        String shopCheck = qf.checkShop(tmp,player.getName());
                        if(shopCheck.equalsIgnoreCase("none")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not exist, are you sure you have the right name?");
                            return true;
                        }else if(shopCheck.equalsIgnoreCase("other")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not belong to you, what are you trying to do?");
                            return true;
                        }else{
                            String recipient = args[count-1];
                            if (plugin.getServer().getPlayer(recipient) == null) {
                                player.sendMessage(moduleName + recipient + " was not found on this server, perhaps is it misspelled?");
                                return true;
                            }

                            setw.put("player_name", player.getName());
                            setw.put("shop_name", tmp);
                            seta.put("player_name", recipient);
                            qf.doUpdate("CEShops", seta, setw);
                            player.sendMessage(moduleName + "'" + tmp + "' has been transferred to " + recipient);
                            if (plugin.getServer().getPlayer(recipient).isOnline()) {
                                Player targetPlayer = player.getServer().getPlayer(args[count-1]);
                                targetPlayer.sendMessage(moduleName + player.getName() + " has transferred '" + tmp + "' to you, Enjoy!");
                            }else{
                             //queue a mail to the recipient
                            }
                            return true;
                        }
                    } else if ((args[0].equalsIgnoreCase("setmode") || args[0].equalsIgnoreCase("changemode") || args[0].equalsIgnoreCase("mode") || args[0].equalsIgnoreCase("m")) && count > 1) {
                        int shopsInEdit = qf.checkPLayerShops(player.getName());
                        if(shopsInEdit != 0){
                            String mode = args[1];
                            plugin.trackPlayers.put(player.getName(), mode);
                            //Check to see if item clicked on exists in shops DB, belongs to shop in edit mode, and is owned by player
                            //Edit item's mode in shops DB
                        }else{
                            player.sendMessage(moduleName + "You do not currently have a shop in edit mode");
                        }
                        return true;
                    } else if ((args[0].equalsIgnoreCase("setprice") || args[0].equalsIgnoreCase("changeprice") || args[0].equalsIgnoreCase("price") || args[0].equalsIgnoreCase("p")) && count > 1) {
                        //Check to see if item clicked on exists in shops DB and is owned by player
                        //make sure shop is closed and in edit mode
                        //Edit item's price in shops DB
                        player.sendMessage(moduleName + "Change an item's price");
                        return true;
                    } else if ((args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("s")) && count > 1) {
                        if (count < 2) {
                            return true;
                        }
                        StringBuilder buf = new StringBuilder(args[1]);
                        for (int i = 2; i < count; i++) {
                            buf.append(" ").append(args[i]);
                        }
                        String tmp = buf.toString();
                      
                        String shopCheck = qf.checkShop(tmp,player.getName());
                        if(shopCheck.equalsIgnoreCase("none")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not exist, are you sure you have the right name?");
                            return true;
                        }else if(shopCheck.equalsIgnoreCase("other")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not belong to you, what are you trying to do?");
                            return true;
                        }else{
                            String current_status = qf.checkShopStatus(tmp);
                            if(!current_status.equalsIgnoreCase("edit")){
                                player.sendMessage(moduleName + "'" + tmp + "' is not in edit mode");
                                return true;
                            }
                            setw.put("player_name", player.getName());
                            setw.put("shop_name", tmp);
                            seta.put("status", "closed");
                            qf.doUpdate("CEShops", seta, setw);
                            player.sendMessage(moduleName + "'" + tmp + " has been saved, dont forget to open it");
                            return true;
                        }
                    } else if ((args[0].equalsIgnoreCase("open")) && count > 1) {
                        if (count < 2) {
                            return true;
                        }
                        StringBuilder buf = new StringBuilder(args[1]);
                        for (int i = 2; i < count; i++) {
                            buf.append(" ").append(args[i]);
                        }
                        String tmp = buf.toString();
                      
                        String shopCheck = qf.checkShop(tmp,player.getName());
                        if(shopCheck.equalsIgnoreCase("none")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not exist, are you sure you have the right name?");
                            return true;
                        }else if(shopCheck.equalsIgnoreCase("other")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not belong to you, what are you trying to do?");
                            return true;
                        }else{
                            String current_status = qf.checkShopStatus(tmp);
                            if(current_status.equalsIgnoreCase("open")){
                                player.sendMessage(moduleName + "'" + tmp + "' is already open!");
                                return true;
                            }
                            setw.put("player_name", player.getName());
                            setw.put("shop_name", tmp);
                            seta.put("status", "open");
                            qf.doUpdate("CEShops", seta, setw);
                            player.sendMessage(moduleName + "'" + tmp + " is now open for business");
                            //broadcast closed message to all players?????
                            return true;
                        }
                    } else if ((args[0].equalsIgnoreCase("close")) && count > 1) {
                        if (count < 2) {
                            return true;
                        }
                        StringBuilder buf = new StringBuilder(args[1]);
                        for (int i = 2; i < count; i++) {
                            buf.append(" ").append(args[i]);
                        }
                        String tmp = buf.toString();
                      
                        String shopCheck = qf.checkShop(tmp,player.getName());
                        if(shopCheck.equalsIgnoreCase("none")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not exist, are you sure you have the right name?");
                            return true;
                        }else if(shopCheck.equalsIgnoreCase("other")){
                            player.sendMessage(moduleName + "'" + tmp + "' does not belong to you, what are you trying to do?");
                            return true;
                        }else{
                            String current_status = qf.checkShopStatus(tmp);
                            if(!current_status.equalsIgnoreCase("open")){
                                player.sendMessage(moduleName + "'" + tmp + "' is not open!");
                                return true;
                            }
                            setw.put("player_name", player.getName());
                            setw.put("shop_name", tmp);
                            seta.put("status", "closed");
                            qf.doUpdate("CEShops", seta, setw);
                            player.sendMessage(moduleName + "'" + tmp + " is now closed.");
                            //broadcast closed message to all players?????
                            return true;
                        }
                    } else {
                      player.sendMessage(moduleName + "not enough arguments");
                      // show them the proper usage
                      return true;
                    }
                    
                }
                
            }
            
        }
        
        return true;
    }
}
