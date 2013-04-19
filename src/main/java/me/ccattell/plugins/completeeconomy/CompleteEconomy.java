package me.ccattell.plugins.completeeconomy;

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
	public void onDisable() {
		// TODO: Place any custom disable code here.
	}
	public void onEnable() {
            pdfFile = getDescription();
            pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
            plugin = this;
            console = getServer().getConsoleSender();
            try {
                String path = getDataFolder() + File.separator + "CompleteEconomy.db";
                    service.setConnection(path);
                    service.createTables();
            } catch (Exception e) {
                console.sendMessage(pluginName + "Connection and Tables Error: " + e);
            }
            getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage("Welcome, Grandmaster " + event.getPlayer().getDisplayName() + "!");
	}
} 