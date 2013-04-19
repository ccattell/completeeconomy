package me.ccattell.plugins.completeeconomy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class CompleteEconomy extends JavaPlugin implements Listener {

	public void onDisable() {
		// TODO: Place any custom disable code here.
	}
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage("Welcome, Grandmaster " + event.getPlayer().getDisplayName() + "!");
	}
} 