package me.ccattell.plugins.completeeconomy.commands;

import java.util.HashMap;
import me.ccattell.plugins.completeeconomy.database.CEMainResultSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Charlie
 */
public class CECommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ce")) {
            // do some stuff
            sender.sendMessage("You just used the /ce command!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("cash")) {
            String name = sender.getName();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player_name", name);
            CEMainResultSet rsm = new CEMainResultSet(where);
            float c;
            if (rsm.resultSet()) {
                // found a record so load data
                c = rsm.getCash();
                // do something with it
            }else{
                c = 0;
            }
            sender.sendMessage(name + "'s Cash Balance: " + c );
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("pay")) {
            // pay another player amount shown and save to the database
            sender.sendMessage("You just used the /pay command!");
            return true;
        }
        return false;
    }
}
