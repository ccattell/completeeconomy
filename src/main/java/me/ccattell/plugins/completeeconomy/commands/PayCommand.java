package me.ccattell.plugins.completeeconomy.commands;

import java.util.HashMap;
import static me.ccattell.plugins.completeeconomy.CompleteEconomy.plugin;
import me.ccattell.plugins.completeeconomy.database.MainResultSet;
import me.ccattell.plugins.completeeconomy.database.QueryFactory;
import me.ccattell.plugins.completeeconomy.utilities.MajorMinor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class PayCommand implements CommandExecutor {

    public String prefix = plugin.getConfig().getString("System.Currency.Prefix");
    public String moduleName = ChatColor.DARK_GREEN + prefix + ChatColor.RESET + " ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("pay")) {
            Player player;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(moduleName + "The pay command cannot be used from the console!");
                return true;
            }
            if (!player.hasPermission("ce.pay")) {
                player.sendMessage(moduleName + "You don't have permission to use that command!");
                return true;
            } else {
                String to_name;
                String from_name;
                float pay_amount;

                if (args.length != 2) { // if args.length != 2 - incorrect number of arguments?
                    player.sendMessage(moduleName + "Incorrect number of arguments");
                    return true;
                }
                try {
                    pay_amount = Float.parseFloat(args[0]);
                } catch (NumberFormatException nfe) {
                    return true;
                }
                to_name = args[1];
                from_name = player.getName();
                if (from_name.equalsIgnoreCase(args[1])) { // can't pay yourself
                    player.sendMessage(moduleName + "You can't pay yourself!");
                    return true;
                }
                // name shouldn't be empty cause we're checking argument length
                HashMap<String, Object> where_from = new HashMap<String, Object>();
                where_from.put("player_name", from_name);
                MainResultSet fq = new MainResultSet(where_from);
                if (fq.resultSet()) {
                    // found a record so load data
                    float c;
                    String s;
                    c = fq.getCash();
                    if (c < pay_amount) {
                        player.sendMessage(moduleName + "Not enough cash on hand to complete this transaction.");
                        return true;
                    } else {
                        HashMap<String, Object> where_to = new HashMap<String, Object>();
                        where_to.put("player_name", to_name);
                        MainResultSet tq = new MainResultSet(where_to);
                        if (tq.resultSet()) {
                            //                        old_to = tq.getCash();
                            s = new MajorMinor().getFormat(pay_amount);
                            QueryFactory qf = new QueryFactory();
                            qf.alterBalance("cash", from_name, -pay_amount);
                            qf.alterBalance("cash", to_name, pay_amount);
                            player.sendMessage(moduleName + "You paid " + to_name + " " + s);
                            return true;
                        } else {
                            player.sendMessage(moduleName + " does not exist in the CE database! Check your spelling.");
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }
}
