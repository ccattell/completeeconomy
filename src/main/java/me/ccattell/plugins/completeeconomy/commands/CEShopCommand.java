package me.ccattell.plugins.completeeconomy.commands;

import static me.ccattell.plugins.completeeconomy.CompleteEconomy.plugin;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
                        player.sendMessage(moduleName + "Incorrect number of arguments");
                        return true;
                    }
                    if ((args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("add")) && args.length == 2) {
                      //Add to shops DB
                      player.sendMessage(moduleName + "Create a shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("destroy") || args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("remove")) && args.length == 2) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Return all itemframes and inventory to player
                      //Detroy itemframes and signson the wall
                      //Delete from shops DB
                      player.sendMessage(moduleName + "Delete a shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e") || args[0].equalsIgnoreCase("chnge") || args[0].equalsIgnoreCase("update")) && args.length == 2) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Edit a shop in shops DB
                      player.sendMessage(moduleName + "Edit a shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("setowner") || args[0].equalsIgnoreCase("changeowner") || args[0].equalsIgnoreCase("owner") || args[0].equalsIgnoreCase("o") || args[0].equalsIgnoreCase("transfer") || args[0].equalsIgnoreCase("t"))&& args.length == 3) {
                      //Check to see if shop exists in shops DB and is owned by player
                      //Edit a shop's owner in shops DB
                      player.sendMessage(moduleName + "Transfer a shop");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("setmode") || args[0].equalsIgnoreCase("changemode") || args[0].equalsIgnoreCase("mode") || args[0].equalsIgnoreCase("m")) && args.length == 2) {
                      //Check to see if item clicked on exists in shops DB and is owned by player
                      //Edit item's mode in shops DB
                      player.sendMessage(moduleName + "Change an item's mode");
                      return true;
                    } else if ((args[0].equalsIgnoreCase("setprice") || args[0].equalsIgnoreCase("changeprice") || args[0].equalsIgnoreCase("price") || args[0].equalsIgnoreCase("p")) && args.length == 2) {
                      //Check to see if item clicked on exists in shops DB and is owned by player
                      //Edit item's price in shops DB
                      player.sendMessage(moduleName + "Change an item's price");
                      return true;
                    } else {
                      player.sendMessage(moduleName + "Incorrect number of arguments");
                      // show them the proper usage
                      return false;
                    }
                    
                }
                
            }
            
        }
        
        return true;
    }
}
