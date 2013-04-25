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
public class CEXPBankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("xpbank")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            String name = player.getName();
            String transaction_type;
            float transaction_amount;
            float balance;

            if (args.length == 0) {
                HashMap<String, Object> where_from = new HashMap<String, Object>();
                where_from.put("player_name", name);
                CEMainResultSet fq = new CEMainResultSet(where_from);
                if (fq.resultSet()) {
                    balance = fq.getXp();
                    sender.sendMessage("Your current xp balance is " + balance);
                    return true;
                }
            } else if (args.length == 2) {
                transaction_type = args[0];
                try { // check it's a number, if not return false
                    transaction_amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                    return true;
                }
                if(transaction_amount > 0){
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player_name", name);
                    CEMainResultSet fq = new CEMainResultSet(where);
                    if (fq.resultSet()) {
                        if(transaction_type.equalsIgnoreCase("withdrawal") || transaction_type.equalsIgnoreCase("w")){
                            balance = fq.getXp();
                            if(balance >= transaction_amount){
                                CEQueryFactory qf = new CEQueryFactory();
                                qf.alterXPBalance(name, -transaction_amount);
                                sender.sendMessage("Transaction complete");
                                return true;
                            }else{
                                sender.sendMessage("Insufficient xp to complete withdrawal");
                                return true;
                            }
                        }
                        if(transaction_type.equalsIgnoreCase("deposit") || transaction_type.equalsIgnoreCase("d")){
                            balance = fq.getXp(); ///change to Player's Current Game XP Amount
                            if(balance >= transaction_amount){
                                CEQueryFactory qf = new CEQueryFactory();
                                qf.alterXPBalance(name, transaction_amount);
                                sender.sendMessage("Transaction complete");
                                return true;
                            }else{
                                sender.sendMessage("Insufficient xp to complete deposit");
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
        return false;
    }
}
