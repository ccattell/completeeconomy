package me.ccattell.plugins.completeeconomy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.ccattell.plugins.completeeconomy.commands.CEBankCommand;
import me.ccattell.plugins.completeeconomy.commands.CECashCommand;
import me.ccattell.plugins.completeeconomy.commands.CEJobsCommand;
import me.ccattell.plugins.completeeconomy.commands.CEPayCommand;
import me.ccattell.plugins.completeeconomy.commands.CEXPBankCommand;
import me.ccattell.plugins.completeeconomy.database.CEDatabase;
import me.ccattell.plugins.completeeconomy.database.CEInitMySQL;
import me.ccattell.plugins.completeeconomy.database.CEInitSQLite;
import me.ccattell.plugins.completeeconomy.listeners.CEDeathListener;
import me.ccattell.plugins.completeeconomy.listeners.CEJoinListener;
import me.ccattell.plugins.completeeconomy.runnables.CEBreakRunnable;
import me.ccattell.plugins.completeeconomy.runnables.CEBreakData;
import me.ccattell.plugins.completeeconomy.utilities.CECustomConfigs;
import me.ccattell.plugins.completeeconomy.utilities.CEGiveInterest;
import me.ccattell.plugins.completeeconomy.utilities.CEVersionCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CompleteEconomy extends JavaPlugin implements Listener {

    public static CompleteEconomy plugin;
    CEDatabase service = CEDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
    public ConsoleCommandSender console;
    public String dbtype;
    public PluginManager pm = Bukkit.getServer().getPluginManager();
    public CECustomConfigs configs;
    protected CEVersionCheck versionCheck;
    public String pluginName = ChatColor.DARK_PURPLE + "[Complete Economy]" + ChatColor.RESET + " ";
    public List<CEBreakData> breakQueue = new ArrayList<CEBreakData>();

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        pdfFile = getDescription();

        plugin = this;
        console = getServer().getConsoleSender();
        String UpdateChannel = plugin.getConfig().getString("System.UpdateChannel");
        if (!UpdateChannel.equalsIgnoreCase("none")) {
            console.sendMessage(plugin.pluginName + "Update Channel: " + UpdateChannel);
            this.versionCheck = new CEVersionCheck(this, "http://dev.bukkit.org/server-mods/complete-economy/files.rss");
            if (this.versionCheck.updateNeeded()) {
                String update = this.versionCheck.getUpdate();
                if (update.equalsIgnoreCase("none")) {
                    console.sendMessage(pluginName + "There are no files to test in your channel");
                } else if (update.equalsIgnoreCase("no")) {
                    console.sendMessage(pluginName + "Congratulations, you are running the latest version of CompleteEconomy!");
                } else if (update.equalsIgnoreCase("yes")) {
                    console.sendMessage(pluginName + ChatColor.GOLD + "A new version is available: " + ChatColor.DARK_GREEN + this.versionCheck.getVersion() + ChatColor.RESET);
                    console.sendMessage(pluginName + ChatColor.GOLD + "Get it from: " + ChatColor.DARK_GREEN + this.versionCheck.getLink() + ChatColor.RESET);
                } else if (update.equalsIgnoreCase("dev")) {
                    console.sendMessage(pluginName + "You are using an unreleased version of the plugin!");
                }
            }
        }
        try {
            if (getConfig().getString("System.Database.Type").equals("sqlite")) {
                console.sendMessage(pluginName + "Loading SQLite Database");
                String path = getDataFolder() + File.separator + "CompleteEconomy.db";
                service.setConnection(path);
                new CEInitSQLite().initSQLite();
            } else {
                // mysql
                console.sendMessage(pluginName + "Loading MySQL Database");
                service.setConnection();
                new CEInitMySQL().initMYSQL();
            }
        } catch (Exception e) {
            console.sendMessage(pluginName + ChatColor.GOLD + "Connection and Tables Error: " + e + ChatColor.RESET);
        }
        configs = new CECustomConfigs(this);
        configs.copyDefaultConfigs();
        console.sendMessage(pluginName + "Loading config files");
        configs.loadCustomConfigs();
        pm.registerEvents(new CEJoinListener(), this);
        pm.registerEvents(new CEDeathListener(), this);
        getCommand("cash").setExecutor(new CECashCommand());
        getCommand("pay").setExecutor(new CEPayCommand());
        getCommand("bank").setExecutor(new CEBankCommand());
        getCommand("xpbank").setExecutor(new CEXPBankCommand());
        getCommand("jobs").setExecutor(new CEJobsCommand());
        new CEGiveInterest().interest();
        // start a repeating task to process the mining queue
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CEBreakRunnable(this), 300L, 1200L);
    }

    public List<CEBreakData> getBreakQueue() {
        return breakQueue;
    }
}
