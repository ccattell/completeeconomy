package me.ccattell.plugins.completeeconomy.commands;

import java.util.HashMap;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEMainResultSet;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import me.ccattell.plugins.completeeconomy.utilities.CEXPCalculator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class CEXPBankCommand implements CommandExecutor {

    public CompleteEconomy plugin;
    public String moduleName;
    public String prefix = plugin.configs.getBankConfig().getString("XPBanking.Prefix");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        moduleName = ChatColor.AQUA + prefix + ChatColor.RESET + " ";

        if (cmd.getName().equalsIgnoreCase("xpbank")) {
            Player player;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(moduleName + "The bank command cannot be used from the console!");
                return true;
            }
            if (!player.hasPermission("ce.xpbank")) {
                sender.sendMessage(moduleName + "You don't have permission to use the xp bank!");
                return true;
            }
            String name = player.getName();
            String transaction_type;
            int transaction_amount;
            float balance;
            CEXPCalculator xpc = new CEXPCalculator(player);

            if (args.length == 0) {
                HashMap<String, Object> where_from = new HashMap<String, Object>();
                where_from.put("player_name", name);
                CEMainResultSet fq = new CEMainResultSet(where_from);
                if (fq.resultSet()) {
                    balance = fq.getXp();
                    int newLevel = xpc.getLevelForExp((int) balance);
                    int levelxp = xpc.getXpForLevel(newLevel);
                    int leftoverxp = (int) balance - levelxp;
                    sender.sendMessage(moduleName + "Your current balance is Level: " + newLevel + ", XP: " + leftoverxp);
                    return true;
                }
            } else if (args.length == 2) {
                transaction_type = args[0];
                try {
                    transaction_amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                    return true;
                }
                if (transaction_amount > 0) {
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player_name", name);
                    CEMainResultSet fq = new CEMainResultSet(where);
                    if (fq.resultSet()) {
                        if (transaction_type.equalsIgnoreCase("withdrawal") || transaction_type.equalsIgnoreCase("w")) {
                            balance = fq.getXp();
                            if (balance >= transaction_amount) {
                                CEQueryFactory qf = new CEQueryFactory();
                                qf.alterBalance("xp", name, -transaction_amount);
                                // alter player's  XP
                                xpc.changeExp(transaction_amount);
                                sender.sendMessage(moduleName + "Transaction complete");
                                return true;
                            } else {
                                sender.sendMessage(moduleName + "Insufficient xp to complete withdrawal");
                                return true;
                            }
                        }
                        if (transaction_type.equalsIgnoreCase("deposit") || transaction_type.equalsIgnoreCase("d")) {

                            balance = xpc.getCurrentExp(); ///change to Player's Current Game XP Amount
                            if (balance >= transaction_amount) {
                                CEQueryFactory qf = new CEQueryFactory();
                                qf.alterBalance("xp", name, transaction_amount);
                                // alter player's  XP
                                xpc.changeExp(-transaction_amount);
                                sender.sendMessage(moduleName + "Transaction complete");
                                return true;
                            } else {
                                sender.sendMessage(moduleName + "Insufficient xp to complete deposit");
                                return true;
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(moduleName + "Incorrect number of arguments");
                return true;
            }
        }
        return true;
    }
}
