package me.ccattell.plugins.cnu.commands;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if(cmd.getName().equalsIgnoreCase("poke")){
            if(args.length < 1) {
                sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.WHITE + "Insufficient arguments, /poke <player>");
            }
            else {
                if(Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                    Bukkit.getPlayer(args[0]).sendMessage("You have been poked by: " + sender.getName());
                    sender.sendMessage("You have successfully poked " + Bukkit.getPlayer(args[0]).getName() + ".");
                }
                else{
                    sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.WHITE + "Player: " + Bukkit.getPlayer(args[0]).getName() + " is not currently online.");
                }
            }
        }
        return true;
    }
}
