package me.ccattell.plugins.cnu.listeners;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;
import me.ccattell.plugins.cnu.CompleteNovusUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author Nicolas
 */
public class RawCommandListener implements Listener{
  CompleteNovusUtilities plugin;
  String ErrorP = ChatColor.DARK_RED+"[ERROR] "+ChatColor.WHITE;
  public ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

  public RawCommandListener(CompleteNovusUtilities plugin) {
    this.plugin = plugin;
  }
  public String formatCommand(String cmd, int argSize, int argumentsLength, String[] arguments, Player sender){
    if(cmd.contains("%sender")){
      cmd = cmd.replaceAll("%sender", sender.getName());
    }
    if(cmd.contains("%target")){
      boolean done = false;
      String target = null;
      for(String arg : arguments){
        for(Player player : Bukkit.getOnlinePlayers()){
          if(player.getName().equals(arg)){
            target = player.getName();
            done = true;
            break;
          }
        }
        if(done == true){
          break;
        }
      }
      if(target != null){
        cmd = cmd.replaceAll("%target", target);
      }
      else{
        sender.sendMessage(ErrorP+" That player doesnt exist, check spelling?");
      }
    }
    if(cmd.contains("%oppositeGamemode")){
      String GM;
      if(sender.getGameMode().equals(GameMode.SURVIVAL)){
        GM = "Creative";
      }
      else{
        GM = "Survival";
      }
      cmd = cmd.replaceAll("%oppositeGamemode", GM);
    }
    if(cmd.contains("%message")){
      String newcmd = "";
      for(int i = argSize; i < argumentsLength; i++){
        String arg = arguments[i] + " ";
        newcmd = newcmd + arg;
      }
      cmd = cmd.replaceAll("%message", newcmd);
    }
    return ChatColor.translateAlternateColorCodes('&', cmd);
  }

  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
    String[] cmd = e.getMessage().toLowerCase().split(" ");
    int argsSize = Array.getLength(cmd);
    argsSize = argsSize-1;
    Player p = e.getPlayer();
    Set<String> aliases = plugin.configs.getAliases().getConfigurationSection("Aliasing.Aliases").getKeys(false);
    for(String alias : aliases){
      int AliasArguments = plugin.configs.getAliases().getInt("Aliasing.Aliases."+alias+".Args");
      boolean ConsoleCommand = plugin.configs.getAliases().getBoolean("Aliasing.Aliases."+alias+".Console");
      List<String> Command = plugin.configs.getAliases().getStringList("Aliasing.Aliases."+alias+".Command");
      String Usage = plugin.configs.getAliases().getString("Aliasing.Aliases."+alias+".Usage");
      String AliasNode = plugin.configs.getAliases().getString("Aliasing.Aliases."+alias+".Permission");
      String ReturnMessage = plugin.configs.getAliases().getString("Aliasing.Aliases."+alias+".ReturnMessage");
      if(plugin.configs.getAliases().getBoolean("Aliasing.Enabled") == true){
        if(cmd[0].replace("/", "").equalsIgnoreCase(alias)){
          if(argsSize < AliasArguments){  // Check if arguments are less than specified arguments
            p.sendMessage(ErrorP+"Insufficient arguments");
            if(Usage != null){  // Check if Usage is present
              p.sendMessage(ErrorP+"Usage: "+Usage);  // Usage message
            }
          }
          else if(argsSize >= AliasArguments){
            if(ConsoleCommand == false){  // Check if Console Command
              if(AliasNode != null){  // Check if Alias has a permission node
                if(p.hasPermission("aliases."+AliasNode)){  // Check if player has alias permission node
                  for(String command : Command){  // For every command it finds in "Command:" run this
                    p.performCommand(formatCommand(command.replace("/", ""), AliasArguments, argsSize, cmd, p)); // Execution of command
                  }
                  if(ReturnMessage != null){  // Checks if "ReturnMessage:" is present
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ReturnMessage));  // ReturnMessage execution
                  }
                }
                else{  // If player doesnt have permission node
                  p.sendMessage(ErrorP+"You do not have permission to use this alias.");
                }
              }
              else{  // Alias doesnt need permission, Command Execution
                for(String command : Command){  // For every command it finds in "Command:" run this
                  p.performCommand(formatCommand(command.replace("/", ""), argsSize, argsSize, cmd, p)); // Execution of command
                }
                if(ReturnMessage != null){  // Checks if "ReturnMessage:" is present
                  p.sendMessage(ChatColor.translateAlternateColorCodes('&', ReturnMessage));  // ReturnMessage execution
                }
              }
            }
            else{  // If console Command is true, Command Execution from Console
              for(String command : Command){  // For every command it finds in "Command:" run this
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatCommand(command.replace("/", ""), AliasArguments, argsSize, cmd, p)); // Execution of command
              }
              if(ReturnMessage != null){  // Checks if "ReturnMessage:" is present
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ReturnMessage));  // ReturnMessage execution
              }
            }
          }
          e.setCancelled(true);  //  Cancels the event so it doesnt become a "real" command
        }
      }
    }
  }
}
