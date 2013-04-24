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
        if (cmd.getName().equalsIgnoreCase("ce")) {
            // do some stuff
            sender.sendMessage("You just used the /ce command!");
            return true;
        }
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

        if (cmd.getName().equalsIgnoreCase("pay")) {
            String to_name;
            String from_name;
            float pay_amount;

            if (args.length < 2) { // if args.length != 2 - incorrect number of arguments?
                sender.sendMessage("Not enough supplied arguments");
                return false;
            } else if (args.length > 2) {
                sender.sendMessage("Too many supplied arguments");
                return false;
            } else {
                try { // check it's a number, if not return false
                    pay_amount = Float.parseFloat(args[0]);
                } catch (NumberFormatException nfe) {
                    return false;
                }
                to_name = args[1];
            }
            from_name = sender.getName();
            if (from_name.equalsIgnoreCase(args[1])) { // can't pay yourself
                sender.sendMessage("You can't pay yourself!");
                return true;
            }
            // name shouldn't be empty cause we're checking argument length
            HashMap<String, Object> where_from = new HashMap<String, Object>();
            where_from.put("player_name", from_name);
            CEMainResultSet fq = new CEMainResultSet(where_from);
            if (fq.resultSet()) {
                // found a record so load data
                float c;
//                float new_from;
//                float old_to;
//                float new_to;
                long major_amt;
                long minor_amt;
                double m;
                String s;
                c = fq.getCash();
                if (c < pay_amount) {
                    sender.sendMessage("Not enough cash on hand to complete this transaction.");
                    return false;
                } else {
                    HashMap<String, Object> where_to = new HashMap<String, Object>();
                    where_to.put("player_name", to_name);
                    CEMainResultSet tq = new CEMainResultSet(where_to);
                    if (tq.resultSet()) {
//                        old_to = tq.getCash();
                        if (format.equalsIgnoreCase("false")) {
                            if (pay_amount == 1) {
                                major = majorSingle;
                            } else {
                                major = majorPlural;
                            }
                            s = pay_amount + " " + major;
                        } else {
                            s = "";
                            major_amt = (long) pay_amount;
                            m = pay_amount - major_amt;
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
//                        new_from = c - pay_amount;
//                        new_to = old_to + pay_amount;
                        // Do some stuff
                        CEQueryFactory qf = new CEQueryFactory();
                        qf.alterCashBalance(from_name, -pay_amount);
                        qf.alterCashBalance(to_name, pay_amount);
                        sender.sendMessage("You paid the player " + to_name + " " + s);
                        return true;
                    } else {
                        sender.sendMessage(to_name + " does not exist in the CE database! Check your spelling.");
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
