package me.ccattell.plugins.cnu.listeners;

import java.util.HashMap;
import me.ccattell.plugins.cnu.CompleteNovusUtilities;
import me.ccattell.plugins.cnu.database.MainResultSet;
import me.ccattell.plugins.cnu.database.QueryFactory;
import me.ccattell.plugins.cnu.utilities.VersionCheck;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Charlie
 */
public class JoinListener implements Listener {

    protected VersionCheck versionCheck;
    private CompleteNovusUtilities plugin;

    public JoinListener(CompleteNovusUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        String UpdateChannel = plugin.getConfig().getString("System.UpdateChannel");
        if (!UpdateChannel.equalsIgnoreCase("none") && event.getPlayer().isOp()) {
            this.versionCheck = new VersionCheck(plugin, "http://dev.bukkit.org/server-mods/complete-economy/files.rss");
            if (this.versionCheck.updateNeeded()) {
                String update = this.versionCheck.getUpdate();
                if (update.equalsIgnoreCase("yes")) {
                    event.getPlayer().sendMessage(plugin.pluginName + ChatColor.GOLD + "A new version is available: " + ChatColor.DARK_GREEN + this.versionCheck.getVersion() + ChatColor.RESET);
                    event.getPlayer().sendMessage(plugin.pluginName + ChatColor.GOLD + "Get it from: " + ChatColor.DARK_GREEN + this.versionCheck.getLink() + ChatColor.RESET);
                }
            }
        }
        loadPlayer(name);
//        event.getPlayer().sendMessage("Welcome, "+ PlayerTitle +" " + event.getPlayer().getDisplayName() + "!");
        event.getPlayer().sendMessage("Welcome, Grandmaster " + name + "!");
    }

    public void loadPlayer(String name) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player_name", name);
        MainResultSet rsm = new MainResultSet(where);
        float c;
        HashMap<String, Object> set = new HashMap<String, Object>();
        QueryFactory qf = new QueryFactory();
        if (rsm.resultSet()) {
            // found a record so load data
            // c = rsm.getCash();
            // do something with it
            // update last_login
            set.put("last_login", System.currentTimeMillis());
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player_name", name);
            qf.doUpdate("CNUMain", set, wherep);
        } else {
            // insert a record for new player
            c = plugin.getConfig().getInt("System.Default.Holdings") * 1.0F; // can't getFloat() so make one from int
            set.put("player_name", name);
            set.put("cash", c);
            set.put("bank", 0);
            set.put("xp", 0);
            set.put("last_login", System.currentTimeMillis());
            qf.doInsert("CNUMain", set);
            // do something with it
        }
    }
}
