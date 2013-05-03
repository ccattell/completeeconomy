package me.ccattell.plugins.completeeconomy.commands;

import java.util.HashMap;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEMainResultSet;
import me.ccattell.plugins.completeeconomy.utilities.CEMajorMinor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class CECashCommand implements CommandExecutor {

    public CompleteEconomy plugin;
    public String moduleName;
    public String prefix = plugin.getConfig().getString("System.Currency.Prefix");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        moduleName = ChatColor.DARK_GREEN + prefix + ChatColor.RESET + " ";

        if (cmd.getName().equalsIgnoreCase("cash")) {
            if (!sender.hasPermission("ce.cash")) {
                sender.sendMessage(moduleName + "You don't have permission to use that command!");
                return true;
            } else {
                String name = "";
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                boolean name_supplied = false; // use a boolean rather than comparing strings
                if (args.length > 0) {
                    if (!sender.hasPermission("ce.admin")) { // console always has permission
                        sender.sendMessage(moduleName + "You don't have permission to get another player's balance!");
                        return true;
                    }
                    // player name supplied
                    name = args[0];
                    name_supplied = true;
                } else if (player != null) {
                    name = player.getName();
                }
                if (!name.isEmpty()) { // name could potentially still be empty, so check
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player_name", name);
                    CEMainResultSet rsm = new CEMainResultSet(where);
                    float c;
                    if (rsm.resultSet()) {
                        // found a record so load data
                        c = rsm.getCash();
                        String s = new CEMajorMinor().getFormat(c);
                        String which = (name_supplied) ? name + "'s" : "Your";
                        sender.sendMessage(moduleName + which + " cash balance: " + s);
                    } else {
                        // player does not exist in the CEMain table
                        sender.sendMessage(moduleName + name + " does not exist in the CE database! Check your spelling.");
                    }
                    return true;
                } else {
                    sender.sendMessage(moduleName + "You must supply a player name");
                }
            }
        }
        return true;
    }
}
