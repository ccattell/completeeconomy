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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charles
 */
public class GamemodeCommand implements CommandExecutor {
  ConsoleCommandSender console = Bukkit.getConsoleSender();
  String ErrorP = ChatColor.DARK_RED + "[ERROR] " + ChatColor.GOLD;
    
    public void toggleGameMode(CommandSender sender, String p, GameMode CurrentGameMode){
      if(CurrentGameMode.equals(GameMode.SURVIVAL)){
        Bukkit.getServer().getPlayer(p).setGameMode(GameMode.CREATIVE);
        Bukkit.getServer().getPlayer(p).sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Creative");
      }
      else if(CurrentGameMode.equals(GameMode.CREATIVE)){
        Bukkit.getServer().getPlayer(p).setGameMode(GameMode.SURVIVAL);
        Bukkit.getServer().getPlayer(p).sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Survival");
      }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gm") || cmd.getName().equalsIgnoreCase("gamemode")) {
            if(sender instanceof Player){
              Player p = (Player) sender;
              if(args.length == 0){
                toggleGameMode(sender, p.getName(), p.getGameMode());
                return true;
              }
              else if(args.length == 1){
                if(Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                  toggleGameMode(sender, args[0], Bukkit.getPlayer(args[0]).getGameMode());
                  return true;
                }
                else{
                  p.sendMessage(ErrorP+"Player, "+args[0]+" is not online.");
                  return false;
                }
              }
              else if(args.length > 1){
                p.sendMessage(ErrorP+" Too many arguments!");
                return false;
              }
            }
            else{
              if(args.length == 1){
                if(Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                  toggleGameMode(sender, args[0], Bukkit.getPlayer(args[0]).getGameMode());
                  return true;
                }
                else{
                  console.sendMessage("[ERROR] Player, "+args[0]+" is not online.");
                }
              }
              else if(args.length < 1){
                console.sendMessage("[Error] Not enough arguments!");
                console.sendMessage("Usage: /"+cmd.getName()+" {player}");
                return false;
              }
              else if(args.length > 1){
                console.sendMessage("[ERROR] Too many arguments!");
                console.sendMessage("Usage: /"+cmd.getName()+" {player}");
                return false;
              }
            }
        }
        if (cmd.getName().equalsIgnoreCase("survival") || cmd.getName().equalsIgnoreCase("0")) {
            int count = args.length;
            if (count > 1) {
                sender.sendMessage(ErrorP + "Too many arguments!");
                return true;
            }else if (count == 0) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    player.setGameMode(GameMode.SURVIVAL);
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Survival" + ChatColor.GOLD + " for " + player.getName());
                    return true;
                } else {
                    sender.sendMessage(ErrorP + "You must give a player name to set gamemode!");
                    return true;
                }
            }else {
                if (Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                    Bukkit.getPlayer(args[0]).setGameMode(GameMode.SURVIVAL);
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GOLD + "Your gamemode has been changed to " + ChatColor.RED + "Survival");
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Survival" + ChatColor.GOLD + " for " + Bukkit.getPlayer(args[0]).getName());
                    return true;
                }else{
                    sender.sendMessage(ErrorP + "That player is not online!");
                    return true;
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("creative") || cmd.getName().equalsIgnoreCase("1")) {
            int count = args.length;
            if (count > 1) {
                sender.sendMessage(ErrorP + "Too many arguments!");
                return true;
            }else if (count == 0) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    player.setGameMode(GameMode.CREATIVE);
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Creative" + ChatColor.GOLD + " for " + player.getName());
                    return true;
                } else {
                    sender.sendMessage(ErrorP + "You must give a player name to set gamemode!");
                    return true;
                }
            }else {
                if (Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                    Bukkit.getPlayer(args[0]).setGameMode(GameMode.CREATIVE);
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GOLD + "Your gamemode has been changed to " + ChatColor.RED + "Creative");
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Creative" + ChatColor.GOLD + " for " + Bukkit.getPlayer(args[0]).getName());
                    return true;
                }else{
                    sender.sendMessage(ErrorP + "That player is not online!");
                    return true;
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("adventure") || cmd.getName().equalsIgnoreCase("2")) {
            int count = args.length;
            if (count > 1) {
                sender.sendMessage(ErrorP + "Too many arguments!");
                return true;
            }else if (count == 0) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    player.setGameMode(GameMode.ADVENTURE);
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Adventure" + ChatColor.GOLD + " for " + player.getName());
                    return true;
                } else {
                    sender.sendMessage(ErrorP + "You must give a player name to set gamemode!");
                    return true;
                }
            }else {
                if (Arrays.toString(Bukkit.getServer().getOnlinePlayers()).contains(args[0])){
                    Bukkit.getPlayer(args[0]).setGameMode(GameMode.ADVENTURE);
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GOLD + "Your gamemode has been changed to " + ChatColor.RED + "Adventure");
                    sender.sendMessage(ChatColor.GOLD + " Gamemode changed to " + ChatColor.RED + "Adventure" + ChatColor.GOLD + " for " + Bukkit.getPlayer(args[0]).getName());
                    return true;
                }else{
                    sender.sendMessage(ErrorP + "That player is not online!");
                    return true;
                }
            }
        }
        return true;
    }    
}
