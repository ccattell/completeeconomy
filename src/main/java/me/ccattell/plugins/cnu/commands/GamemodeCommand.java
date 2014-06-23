/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.ccattell.plugins.cnu.commands;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charles
 */
public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gm") || cmd.getName().equalsIgnoreCase("gamemode")) {
            //toggle gamemode for player
        }
        if (cmd.getName().equalsIgnoreCase("survival") || cmd.getName().equalsIgnoreCase("0")) {
            int count = args.length;
            if (count > 1) {
                sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "Too many arguments!");
                return true;
            }else if (count == 0) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    player.setGameMode(GameMode.SURVIVAL);
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Survival" + ChatColor.GOLD + " for " + player.getName());
                    return true;
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "You must give a player name to set gamemode!");
                    return true;
                }
            }else {
                if (Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                    Bukkit.getPlayer(args[0]).setGameMode(GameMode.SURVIVAL);
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GOLD + "Your gamemode has been changed to " + ChatColor.RED + "Survival");
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Survival" + ChatColor.GOLD + " for " + Bukkit.getPlayer(args[0]).getName());
                    return true;
                }else{
                    sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "That player is not online!");
                    return true;
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("creative") || cmd.getName().equalsIgnoreCase("1")) {
            int count = args.length;
            if (count > 1) {
                sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "Too many arguments!");
                return true;
            }else if (count == 0) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    player.setGameMode(GameMode.CREATIVE);
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Creative" + ChatColor.GOLD + " for " + player.getName());
                    return true;
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "You must give a player name to set gamemode!");
                    return true;
                }
            }else {
                if (Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                    Bukkit.getPlayer(args[0]).setGameMode(GameMode.CREATIVE);
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GOLD + "Your gamemode has been changed to " + ChatColor.RED + "Creative");
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Creative" + ChatColor.GOLD + " for " + Bukkit.getPlayer(args[0]).getName());
                    return true;
                }else{
                    sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "That player is not online!");
                    return true;
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("adventure") || cmd.getName().equalsIgnoreCase("2")) {
            int count = args.length;
            if (count > 1) {
                sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "Too many arguments!");
                return true;
            }else if (count == 0) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    player.setGameMode(GameMode.ADVENTURE);
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Adventure" + ChatColor.GOLD + " for " + player.getName());
                    return true;
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "You must give a player name to set gamemode!");
                    return true;
                }
            }else {
                if (Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                    Bukkit.getPlayer(args[0]).setGameMode(GameMode.ADVENTURE);
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GOLD + "Your gamemode has been changed to " + ChatColor.RED + "Adventure");
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Adventure" + ChatColor.GOLD + " for " + Bukkit.getPlayer(args[0]).getName());
                    return true;
                }else{
                    sender.sendMessage(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD + "That player is not online!");
                    return true;
                }
            }
        }
        return true;
    }    
}
