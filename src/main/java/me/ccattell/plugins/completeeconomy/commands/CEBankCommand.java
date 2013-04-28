package me.ccattell.plugins.completeeconomy.commands;

import java.util.HashMap;
import me.ccattell.plugins.completeeconomy.database.CEMainResultSet;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import me.ccattell.plugins.completeeconomy.utilities.CEMajorMinor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class CEBankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("bank")) {
            if (!sender.hasPermission("bank.command")) {
                sender.sendMessage("You don't have permission to use that command!");
                return true;
            }else{
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player != null) {
                    String name = player.getName();
                    String transaction_type;
                    float transaction_amount;
                    float balance;

                    if (args.length == 0) {
                        HashMap<String, Object> where_from = new HashMap<String, Object>();
                        where_from.put("player_name", name);
                        CEMainResultSet fq = new CEMainResultSet(where_from);
                        if (fq.resultSet()) {
                            balance = fq.getBank();
                            String s = new CEMajorMinor().getFormat(balance);
                            sender.sendMessage("Your current bank balance is " + s);
                            return true;
                        }
                    } else if (args.length == 2) {
                        transaction_type = args[0];
                        try { // check it's a number, if not return false
                            transaction_amount = Float.parseFloat(args[1]);
                        } catch (NumberFormatException nfe) {
                            return true;
                        }
                        if (transaction_amount > 0) {
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("player_name", name);
                            CEMainResultSet fq = new CEMainResultSet(where);
                            if (fq.resultSet()) {
                                if (transaction_type.equalsIgnoreCase("withdrawal") || transaction_type.equalsIgnoreCase("w")) {
                                    balance = fq.getBank();
                                    if (balance >= transaction_amount) {
                                        CEQueryFactory qf = new CEQueryFactory();
                                        qf.alterBalance("bank", name, -transaction_amount);
                                        qf.alterBalance("cash", name, transaction_amount);
                                        sender.sendMessage("Transaction complete");
                                        return true;
                                    } else {
                                        sender.sendMessage("Insufficient funds to complete withdrawal");
                                        return true;
                                    }
                                }
                                if (transaction_type.equalsIgnoreCase("deposit") || transaction_type.equalsIgnoreCase("d")) {
                                    balance = fq.getCash();
                                    if (balance >= transaction_amount) {
                                        CEQueryFactory qf = new CEQueryFactory();
                                        qf.alterBalance("bank", name, transaction_amount);
                                        qf.alterBalance("cash", name, -transaction_amount);
                                        sender.sendMessage("Transaction complete");
                                        return true;
                                    } else {
                                        sender.sendMessage("Insufficient funds to complete deposit");
                                        return true;
                                    }
                                }
                            }
                        }
                    } else {
                        sender.sendMessage("Incorrect number of arguments");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
