package me.ccattell.plugins.completeeconomy.commands;

import java.util.ArrayList;
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
            }else{
                if (args.length != 2) { // if args.length != 2 - incorrect number of arguments?
                    //player.sendMessage(moduleName + "Incorrect number of arguments");
                    //return true;
                }
                //Set<String> jobsList = CompleteEconomy.plugin.configs.getJobConfig().getConfigurationSection("Jobs.Types").getKeys(false);
                //sender.sendMessage(CompleteEconomy.plugin.configs.getJobConfig().getString(jobsList));
                //new ArrayList();
            }
        }
        return true;
    }
}
