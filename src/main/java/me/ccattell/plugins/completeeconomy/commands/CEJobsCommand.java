package me.ccattell.plugins.completeeconomy.commands;

import java.util.List;
import java.util.Set;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class CEJobsCommand implements CommandExecutor {

    public String moduleName;
    public String prefix = CompleteEconomy.plugin.configs.getJobConfig().getString("Jobs.Prefix");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        moduleName = ChatColor.BLUE + prefix + ChatColor.RESET + " ";

        if (cmd.getName().equalsIgnoreCase("jobs")) {
            Player player;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(moduleName + "The jobs command cannot be used from the console!");
                return true;
            }
            if (!sender.hasPermission("ce.jobs")) {
                sender.sendMessage(moduleName + "You don't have permission to use jobs!");
                return true;
            } else {
                if (args.length == 0) { // if args.length != 2 - incorrect number of arguments?
                    player.sendMessage(moduleName + "Incorrect number of arguments");
                    return true;
                }
                if (args[0].equalsIgnoreCase("list") && args.length == 1) {
                    Set<String> jobsList = CompleteEconomy.plugin.configs.getJobConfig().getConfigurationSection("Jobs.Types").getKeys(false);
                    player.sendMessage(moduleName + "Available jobs:");
                    for (String job : jobsList) {
                        player.sendMessage(job);
                    }
                    return true;
                }else if (args[0].equalsIgnoreCase("info") && args.length == 2) {
                    
                    // check args[1] is in the jobs list
                    List<String> infoList = CompleteEconomy.plugin.configs.getJobConfig().getStringList("Jobs.Types." + args[1].toLowerCase());
                    // loop thru list
                    for (String info : infoList) {
                        // send message
                    }
                }else{
                    player.sendMessage(moduleName + "Incorrect number of arguments");
                    return true;
                }
            }
        }
        return true;
    }
}
