package me.ccattell.plugins.completeeconomy.commands;

import java.util.HashMap;
import static me.ccattell.plugins.completeeconomy.CompleteEconomy.plugin;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class CEShopCommand implements CommandExecutor {
    
    public boolean ShopsEnabled = plugin.configs.getShopConfig().getBoolean("Shops.Enabled");
    public String prefix = plugin.configs.getShopConfig().getString("Shops.Prefix");
    public String moduleName = ChatColor.BLUE + prefix + ChatColor.RESET + " ";
    CEQueryFactory qf = new CEQueryFactory();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

          HashMap<String, Object> setw = new HashMap<String, Object>();

          if (cmd.getName().equalsIgnoreCase("shop") || cmd.getName().equalsIgnoreCase("shops")) {
            // don't do anything unless it's our command
            if (ShopsEnabled) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.sendMessage(moduleName + "The shop command cannot be used from the console!");
                    return true;
                }
                if (!sender.hasPermission("ce.shop")) {
                    sender.sendMessage(moduleName + "You don't have permission to use shops!");
                    return true;
                } else {
                    if (args.length == 0) {
                        player.sendMessage(moduleName + "Not Enough Arguments");
                        return true;
                    }
                    if ((args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("add")) && args.length > 1) {
                      int count = args.length;
                      if (count < 2) {
                        return false;
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
                        return false;
                      }else{
                        player.sendMessage(moduleName + "'" + tmp + "' already exists, please choose another name");
                        return false;
                      }
                    } else if ((args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("destroy") || args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("remove")) && args.length > 1) {
                      int count = args.length;
                      if (count < 2) {
                        return false;
                      }
                      StringBuilder buf = new StringBuilder(args[1]);
                      for (int i = 2; i < count; i++) {
                        buf.append(" ").append(args[i]);
                      }
                      String tmp = buf.toString();
                      
                      String shopCheck = qf.checkShop(tmp,player.getName());
                      if(shopCheck.equalsIgnoreCase("none")){
                        player.sendMessage(moduleName + "'" + tmp + "' does not exist, are you sure you have the right name?");
                        return false;
                      }else if(shopCheck.equalsIgnoreCase("other")){
                        player.sendMessage(moduleName + "'" + tmp + "' does not belong to you, what are you trying to do?");
                        return false;
                      }else{
                        //Return all itemframes and inventory to player
                        //Detroy itemframes and signson the wall
                        setw.put("player_name", player.getName());
                        setw.put("shop_name", tmp);
                        qf.doDelete("CEShops", setw);
                        player.sendMessage(moduleName + "Deleted '" + tmp + "'");
                        return false;
                      }
                    } else if ((args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e") || args[0].equalsIgnoreCase("change") || args[0].equalsIgnoreCase("update")) && args.length > 1) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Check to see if player has any other shops in edit mode needing to be saved
                      //Close the shop for purchases
                      //Change a shop in shops DB to edit mode
                      player.sendMessage(moduleName + "Edit a shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("setowner") || args[0].equalsIgnoreCase("changeowner") || args[0].equalsIgnoreCase("owner") || args[0].equalsIgnoreCase("o") || args[0].equalsIgnoreCase("transfer") || args[0].equalsIgnoreCase("t"))&& args.length > 1) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Edit a shop's owner in shops DB
                      player.sendMessage(moduleName + "Transfer a shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("setmode") || args[0].equalsIgnoreCase("changemode") || args[0].equalsIgnoreCase("mode") || args[0].equalsIgnoreCase("m")) && args.length > 1) {
                      //Check to see if item clicked on exists in shops DB and is owned by player
                      //Edit item's mode in shops DB
                      player.sendMessage(moduleName + "Change an item's mode");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("setprice") || args[0].equalsIgnoreCase("changeprice") || args[0].equalsIgnoreCase("price") || args[0].equalsIgnoreCase("p")) && args.length > 1) {
                      //Check to see if item clicked on exists in shops DB and is owned by player
                      //Edit item's price in shops DB
                      player.sendMessage(moduleName + "Change an item's price");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("s")) && args.length > 1) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Change a shop in shops DB to active mode
                      player.sendMessage(moduleName + "Save shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("open")) && args.length > 1) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Change a shop in shops DB status to open
                      player.sendMessage(moduleName + "Opened shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("close")) && args.length > 1) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Change a shop in shops DB status to closed
                      player.sendMessage(moduleName + "Closed shop");
                      return true;
                    } else {
                      player.sendMessage(moduleName + "not enough arguments");
                      // show them the proper usage
                      return false;
                    }
                    
                }
                
            }
            
        }
        
        return true;
    }
}
