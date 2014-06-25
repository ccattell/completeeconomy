package me.ccattell.plugins.cnu.commands;

import java.util.Set;
import static me.ccattell.plugins.cnu.CompleteNovusUtilities.plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nicolas
 */
public class AliasesCommand implements CommandExecutor{
  public ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
  String ErrorP = ChatColor.DARK_RED+"[ERROR] "+ChatColor.WHITE;
  public String Prefix = ChatColor.translateAlternateColorCodes('&', plugin.configs.getAliases().getString("Aliasing.Prefix"))+ChatColor.WHITE+" ";
  public String UCPrefix = plugin.configs.getAliases().getString("Aliasing.Prefix")+" ";
  public String moduleStatus = plugin.configs.getAliases().getString("Aliasing.Enabled");
  Set<String> aliases = plugin.configs.getAliases().getConfigurationSection("Aliasing.Aliases").getKeys(false);  // Initial alias list declaration
  @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      if(plugin.configs.getAliases().getBoolean("Aliasing.Enabled") == false){
        moduleStatus = ChatColor.DARK_RED+plugin.configs.getAliases().getString("Aliasing.Enabled");
      }
      else{
        moduleStatus = ChatColor.GREEN+plugin.configs.getAliases().getString("Aliasing.Enabled");
      }
      if(cmd.getName().equalsIgnoreCase("alias")){
        if(args.length == 0){
          if(sender instanceof Player){
            Player p = (Player) sender;
            p.sendMessage(ChatColor.DARK_GRAY+"---=---------------------=---");
            p.sendMessage(" - CNU Alias Module v0.1");
            p.sendMessage(" - Enabled: "+moduleStatus);
            p.sendMessage(" - Loaded Aliases: "+aliases.size());
            p.sendMessage(ChatColor.DARK_GRAY+"---=---------------------=---");
            p.sendMessage(" Available Commands:");
            p.sendMessage(" /alias"+ChatColor.GRAY+" - General information about the module");
            p.sendMessage(" /alias list"+ChatColor.GRAY+" - Displays a list of all aliases");
            p.sendMessage(" /alias reload"+ChatColor.GRAY+" - reload the alias configuration file");
          }
          else{
            console.sendMessage("---=---------------------=---");
            console.sendMessage(" - CNU Alias Module v0.1");
            console.sendMessage("- Enabled: "+moduleStatus);
            console.sendMessage("- Loaded Aliases: "+aliases.size());
            console.sendMessage("---=---------------------=---");
            console.sendMessage(" Available Commands:");
            console.sendMessage(" /alias - General information about the module");
            console.sendMessage(" /alias list - Displays a list of all aliases");
            console.sendMessage(" /alias reload - reload the alias configuration file");
            
          }
        }
        if(args.length == 1){
          if(args[0].equalsIgnoreCase("help")){
            if(sender instanceof Player){
              Player p = (Player) sender;
              p.sendMessage(" Available Commands:");
              p.sendMessage(" /alias"+ChatColor.GRAY+" - General information about the module");
              p.sendMessage(" /alias list"+ChatColor.GRAY+" - Displays a list of all aliases");
              p.sendMessage(" /alias reload"+ChatColor.GRAY+" - reload the alias configuration file");
            }
            else{
              console.sendMessage(" Available Commands:");
              console.sendMessage(" /alias - General information about the module");
              console.sendMessage(" /alias list - Displays a list of all aliases");
              console.sendMessage(" /alias reload - reload the alias configuration file");
            }
          }
          else if(args[0].equalsIgnoreCase("list")){
            if(sender instanceof Player){
              Player p = (Player) sender;
              String msg = "| ";
              for(String alias : aliases){
                msg = msg+alias+" | ";
              }
              p.sendMessage(Prefix+msg);
            }
            else{
              String msg = " | ";
              for(String alias : aliases){
                msg = msg+alias+" | ";
              }
              console.sendMessage(UCPrefix+msg);
            }
          }
          else if(args[0].equalsIgnoreCase("reload")){
            plugin.configs.reloadAliases();  //  Reloads the Alias configuration file.
            aliases = plugin.configs.getAliases().getConfigurationSection("Aliasing.Aliases").getKeys(false);  //  Refreshes Alias list.
            moduleStatus = plugin.configs.getAliases().getString("Aliasing.Enabled");  //  Refreshes Alias module status.
            if(sender instanceof Player){
              Player p = (Player) sender;
              p.sendMessage(Prefix+"Alias configuration file successfully reloaded");
            }
            else{
              console.sendMessage(UCPrefix+"Alias configuration file successfully reloaded");
            }
          }
          else{
            if(sender instanceof Player){
              Player p = (Player) sender;
              p.sendMessage(ErrorP+"That is not a valid argument.");
            }
          }
        }
      }
      return true;
    }
}
