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

        if (cmd.getName().equalsIgnoreCase("jobs")) {
            // don't do anything unless it's our command
            moduleName = ChatColor.BLUE + prefix + ChatColor.RESET + " ";
            Set<String> jobsList = CompleteEconomy.plugin.configs.getJobConfig().getConfigurationSection("Jobs.Types").getKeys(false);

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
                    player.sendMessage(moduleName + "Available jobs:");
                    for (String job : jobsList) {
                        player.sendMessage(job);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("info") && args.length == 2) {
                    String job = args[1].toLowerCase();
                    // check args[1] is in the jobs list
                    if (!jobsList.contains(job)) {
                        player.sendMessage(moduleName + "Could not find a job with that name, use /jobs list first!");
                        return true;
                    }
                    List<String> infoList = CompleteEconomy.plugin.configs.getJobConfig().getStringList("Jobs.Types." + job);
                    player.sendMessage(moduleName + "Job description for " + job + ":");
                    // loop thru list
                    for (String info : infoList) {
                        // send message
                        player.sendMessage(info);
                    }
                    return true;
                } else {
                    player.sendMessage(moduleName + "Incorrect number of arguments");
                    // show them the proper usage
                    return false;
                }
            }
        }
        return true;
    }
}
