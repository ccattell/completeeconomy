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

public class CompleteEconomy extends JavaPlugin implements Listener {

    public static CompleteEconomy plugin;
    CEDatabase service = CEDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
    public ConsoleCommandSender console;
    public String pluginName;
    public String dbtype;
    public String moneyName = "";

    //Saves the plugin's folder for future referencing
    public String pluginFolder = this.getDataFolder().getAbsolutePath();
    
    //Functions need to be implemented but has everything needed for a flatfile storage of bank accounts in hashmaps.
    public BankInfo bInfo;
    
    //Needs to be implemented into a LoginListener
    private UpdateChecker updateChecker;
    
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
                String path = getDataFolder() + File.separator + "CompleteEconomy.db";
                service.setConnection(path);
            } else {
                // mysql
                //service.setConnection();
            }       
        } catch (Exception e) {
            console.sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
        
        /*
         * Gathers info from the Bukkit page, and when enabling, if the config has enabled the UpdateMessage and
         * there is a newer file than the one they have, send a message to the console.
        */
        updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/server-mods/complete-economy/files.rss");
        if(updateChecker.updateNeeded()){
        	getServer().getLogger().warning(updateChecker.getUpdateString());
        }
        
        this.bInfo = new BankInfo(this, new File(pluginFolder + File.separator + "Database.yml"));
        
        service.createTables();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	//Suggest making the "Grandmaster" part configurable/linked with a prefix on a permissions (probably would require Vault)
        event.getPlayer().sendMessage("Welcome, Grandmaster " + event.getPlayer().getDisplayName() + "!");
    }
}
