package me.ccattell.plugins.completeeconomy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Charlie
 */
public class SillyCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ping")) {
            sender.sendMessage("Pong!");
        }
        if (cmd.getName().equalsIgnoreCase("marco")) {
            sender.sendMessage("Polo!");
        }
        return true;
    }
}
