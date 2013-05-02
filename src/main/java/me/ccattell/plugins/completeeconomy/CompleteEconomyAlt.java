package me.ccattell.plugins.completeeconomy;

import java.io.File;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.ccattell.plugins.completeeconomy.commands.CECashCommand;
import me.ccattell.plugins.completeeconomy.commands.CEPayCommand;
import me.ccattell.plugins.completeeconomy.commands.CEBankCommand;
import me.ccattell.plugins.completeeconomy.commands.CEXPBankCommand;
import me.ccattell.plugins.completeeconomy.database.CEDatabase;
import me.ccattell.plugins.completeeconomy.database.CEInitMySQL;
import me.ccattell.plugins.completeeconomy.database.CEInitSQLite;
import me.ccattell.plugins.completeeconomy.listeners.CEJoinListener;
import me.ccattell.plugins.completeeconomy.listeners.CEDeathListener;
import me.ccattell.plugins.completeeconomy.utilities.CECustomConfigs;
import me.ccattell.plugins.completeeconomy.utilities.CEGiveInterest;
import me.ccattell.plugins.completeeconomy.utilities.CEVersionCheckAlt;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class CompleteEconomyAlt extends JavaPlugin implements Listener {

    public static CompleteEconomyAlt plugin;
    CEDatabase service = CEDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
    public ConsoleCommandSender console;
    public String pluginName;
    public String dbtype;
    public PluginManager pm = Bukkit.getServer().getPluginManager();
    public CECustomConfigs configs;
    protected CEVersionCheckAlt versionCheck;

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        pdfFile = getDescription();
        pluginName = ChatColor.DARK_PURPLE + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";

        plugin = this;
        console = getServer().getConsoleSender();
        String UpdateChannel = getConfig().getString("System.UpdateChannel");
        if (!UpdateChannel.equalsIgnoreCase("none")) {
            this.versionCheck = new CEVersionCheckAlt(this, "http://dev.bukkit.org/server-mods/tardis/files.rss");
            if (versionCheck.updateNeeded()) {
                // send details
                plugin.console.sendMessage(pluginName + "A new version is available: " + ChatColor.GOLD + versionCheck.getVersion());
                plugin.console.sendMessage(pluginName + "Get it from: " + ChatColor.GOLD + versionCheck.getLink());
            } else {
                plugin.console.sendMessage(pluginName + "Congratulations, you are running the latest version of CompleteEconomy!");
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
            console.sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
        configs = new CECustomConfigs(CompleteEconomy.plugin);
        configs.copyDefaultConfigs();
        console.sendMessage(pluginName + "Loading config files");
        configs.loadCustomConfigs();
        pm.registerEvents(new CEJoinListener(), this);
        pm.registerEvents(new CEDeathListener(), this);
        getCommand("cash").setExecutor(new CECashCommand());
        getCommand("pay").setExecutor(new CEPayCommand());
        getCommand("bank").setExecutor(new CEBankCommand());
        getCommand("xpbank").setExecutor(new CEXPBankCommand());
        new CEGiveInterest().interest();
    }
}