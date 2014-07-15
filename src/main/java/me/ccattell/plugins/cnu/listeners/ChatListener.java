package me.ccattell.plugins.cnu.listeners;

import me.ccattell.plugins.cnu.CompleteNovusUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author Nicolas
 */
public class ChatListener implements Listener{
  CompleteNovusUtilities plugin;
  public ChatListener(CompleteNovusUtilities plugin){
    this.plugin = plugin;
  }
  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent e) {
    
  }
}
