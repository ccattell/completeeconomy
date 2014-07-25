package me.ccattell.plugins.cnu.commands;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

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
        if(cmd.getName().equalsIgnoreCase("live")){
          if(args.length < 1){
            if(sender instanceof ConsoleCommandSender){
              sender.sendMessage("You are a console you silly, you dont live.");
            }
            else{
              sender.sendMessage("You are already alive you silly potato.");
            }
          }
          else{
            if(Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
              sender.sendMessage("Player '"+args[0]+"' is already alive you silly potato.");
            }
            else{
              sender.sendMessage("You have inputed a fake player, and shall be called silly potato for doing so."); 
            }
          }
        }
        return true;
    }
}
