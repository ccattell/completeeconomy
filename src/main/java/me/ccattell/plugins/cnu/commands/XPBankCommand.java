package me.ccattell.plugins.cnu.commands;

import java.util.HashMap;
import static me.ccattell.plugins.cnu.CompleteNovusUtilities.plugin;
import me.ccattell.plugins.cnu.database.MainResultSet;
import me.ccattell.plugins.cnu.database.QueryFactory;
import me.ccattell.plugins.cnu.utilities.XPCalculator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class XPBankCommand implements CommandExecutor {

    public String prefix = plugin.configs.getBankConfig().getString("XPBanking.Prefix");
    public String moduleName = ChatColor.AQUA + prefix + ChatColor.RESET + " ";
    public boolean XPBankEnabled = plugin.configs.getBankConfig().getBoolean("XPBanking.Enabled");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("xpbank")) {
            if(XPBankEnabled){
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.sendMessage(moduleName + "The bank command cannot be used from the console!");
                    return true;
                }
                if (!player.hasPermission("cnu.xpbank")) {
                    sender.sendMessage(moduleName + "You don't have permission to use the xp bank!");
                    return true;
                }
                String name = player.getName();
                String transaction_type;
                int transaction_amount;
                float balance;
                XPCalculator xpc = new XPCalculator(player);

                if (args.length == 0) {
                    HashMap<String, Object> where_from = new HashMap<String, Object>();
                    where_from.put("player_name", name);
                    MainResultSet fq = new MainResultSet(where_from);
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
                        MainResultSet fq = new MainResultSet(where);
                        if (fq.resultSet()) {
                            if (transaction_type.equalsIgnoreCase("withdrawal") || transaction_type.equalsIgnoreCase("w")) {
                                balance = fq.getXp();
                                if (balance >= transaction_amount) {
                                    QueryFactory qf = new QueryFactory();
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
                                    QueryFactory qf = new QueryFactory();
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
            }else{
                sender.sendMessage(moduleName + "XP Banking has been disabled");
            }
        }
        return true;
    }
}
