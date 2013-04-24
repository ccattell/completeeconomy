package me.ccattell.plugins.completeeconomy.commands;

import java.util.HashMap;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEMainResultSet;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class CECashCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String major;
        String minor;
        String majorSingle = CompleteEconomy.plugin.getConfig().getString("System.Currency.MajorSingle");
        String minorSingle = CompleteEconomy.plugin.getConfig().getString("System.Currency.MinorSingle");
        String majorPlural = CompleteEconomy.plugin.getConfig().getString("System.Currency.MajorPlural");
        String minorPlural = CompleteEconomy.plugin.getConfig().getString("System.Currency.MinorPlural");
        String format = CompleteEconomy.plugin.getConfig().getString("System.Formatting.Separate");
        if (cmd.getName().equalsIgnoreCase("cash")) {
            String name = "";
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            boolean name_supplied = false; // use a boolean rather than comparing strings
            if (args.length > 0) {
                if (!sender.hasPermission("ce.admin")) { // console always has permission
                    sender.sendMessage("You don't have permission to get another player's balance!");
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
                long major_amt;
                long minor_amt;
                double m;
                String s;
                if (rsm.resultSet()) {
                    // found a record so load data
                    c = rsm.getCash();
                    if (format.equalsIgnoreCase("false")) {
                        if (c == 1) {
                            major = majorSingle;
                        } else {
                            major = majorPlural;
                        }
                        s = c + " " + major;
                    } else {
                        s = "";
                        major_amt = (long) c;
                        m = c - major_amt;
                        m = m * 100;
                        minor_amt = (long) m;
                        if (major_amt > 0) {
                            if (major_amt == 1) {
                                major = majorSingle;
                            } else {
                                major = majorPlural;
                            }
                            s = s + major_amt + " " + major + " ";
                        }
                        if (minor_amt > 0) {
                            if (minor_amt == 1) {
                                minor = minorSingle;
                            } else {
                                minor = minorPlural;
                            }
                            s = s + minor_amt + " " + minor;
                        }
                    }
                    String which = (name_supplied) ? name + "'s" : "Your";
                    sender.sendMessage(which + " cash balance: " + s);
                } else {
                    // player does not exist in the CEMain table
                    sender.sendMessage(name + " does not exist in the CE database! Check your spelling.");
                }
                return true;
            } else {
                sender.sendMessage("You must supply a player name");
            }
        }
        return false;
    }
}
