package me.ccattell.plugins.completeeconomy;

import java.io.File;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.ccattell.plugins.completeeconomy.database.CEDatabase;
import me.ccattell.plugins.completeeconomy.database.CEInitMySQL;
import me.ccattell.plugins.completeeconomy.database.CEInitSQLite;

public class CompleteEconomy extends JavaPlugin implements Listener {

    public static CompleteEconomy plugin;
    CEDatabase service = CEDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
    public ConsoleCommandSender console;
    public String pluginName;
    public String dbtype;

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
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        loadPlayer();
//        event.getPlayer().sendMessage("Welcome, "+ PlayerTitle +" " + event.getPlayer().getDisplayName() + "!");
        event.getPlayer().sendMessage("Welcome, Grandmaster " + event.getPlayer().getDisplayName() + "!");
    }
    public void loadPlayer(){
    }
}
