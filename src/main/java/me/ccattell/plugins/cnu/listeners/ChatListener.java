package me.ccattell.plugins.cnu.listeners;

import java.util.HashMap;
import me.ccattell.plugins.cnu.CompleteNovusUtilities;
import me.ccattell.plugins.cnu.database.MainResultSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    e.setCancelled(true);
    HashMap<String, Object> where = new HashMap<String, Object>();
    where.put("player_UUID", e.getPlayer().getUniqueId().toString());
    MainResultSet fq = new MainResultSet(where);
    String playerChannel = fq.getChannel();
    for(Player p : Bukkit.getOnlinePlayers()){
      where.put("player_UUID", p.getUniqueId().toString());
      fq = new MainResultSet(where);
      if(fq.getChannel().equalsIgnoreCase(playerChannel)){
        p.sendMessage(e.getMessage());
      }
    }
  }
}
