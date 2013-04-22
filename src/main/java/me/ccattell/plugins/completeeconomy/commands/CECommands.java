package me.ccattell.plugins.completeeconomy.commands;

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
        return false;
    }
}
