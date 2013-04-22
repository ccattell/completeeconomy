package me.ccattell.plugins.completeeconomy;

import java.io.File;
import me.ccattell.plugins.completeeconomy.commands.CECommands;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.ccattell.plugins.completeeconomy.database.CEDatabase;
import me.ccattell.plugins.completeeconomy.database.CEInitMySQL;
import me.ccattell.plugins.completeeconomy.database.CEInitSQLite;
import me.ccattell.plugins.completeeconomy.listeners.CEJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class CompleteEconomy extends JavaPlugin implements Listener {

    public static CompleteEconomy plugin;
    CEDatabase service = CEDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
    public ConsoleCommandSender console;
    public String pluginName;
    public String dbtype;
    public PluginManager pm = Bukkit.getServer().getPluginManager();
    public CEJoinListener joinListener;
    public CECommands commands;

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
        try {
            if (CompleteEconomy.plugin.getConfig().getString("System.Database.Type").equals("sqlite")) {
                console.sendMessage(CompleteEconomy.plugin.pluginName + "Loading SQLite Database");
                String path = getDataFolder() + File.separator + "CompleteEconomy.db";
                service.setConnection(path);
                new CEInitSQLite().initSQLite();
            } else {
                // mysql
                console.sendMessage(CompleteEconomy.plugin.pluginName + "Loading MySQL Database");
                service.setConnection();
                new CEInitMySQL().initMYSQL();
            }
        } catch (Exception e) {
            console.sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
        joinListener = new CEJoinListener();
        pm.registerEvents(joinListener, this);
        commands = new CECommands();
        getCommand("ce").setExecutor(commands);
        getCommand("cash").setExecutor(commands);
        getCommand("pay").setExecutor(commands);
    }
}
