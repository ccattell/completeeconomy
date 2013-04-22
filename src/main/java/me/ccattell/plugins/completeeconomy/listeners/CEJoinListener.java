package me.ccattell.plugins.completeeconomy.listeners;

import java.util.HashMap;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEMainResultSet;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Charlie
 */
public class CEJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        loadPlayer(name);
//        event.getPlayer().sendMessage("Welcome, "+ PlayerTitle +" " + event.getPlayer().getDisplayName() + "!");
        event.getPlayer().sendMessage("Welcome, Grandmaster " + name + "!");
    }

    public void loadPlayer(String name) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player_name", name);
        CEMainResultSet rsm = new CEMainResultSet(where);
        float c;
        HashMap<String, Object> set = new HashMap<String, Object>();
        CEQueryFactory qf = new CEQueryFactory();
        if (rsm.resultSet()) {
            // found a record so load data
            c = rsm.getCash();
            // do something with it
            // update last_login
            set.put("last_login", System.currentTimeMillis());
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player_name", name);
            qf.doUpdate("CEMain", set, wherep);
        } else {
            // insert a record for new player
            c = CompleteEconomy.plugin.getConfig().getInt("System.Default.Holdings") * 1.0F; // can't getFloat() so make one from int
            set.put("player_name", name);
            set.put("cash", c);
            qf.doInsert("CEMain", set);
            // do something with it
        }
    }
}
