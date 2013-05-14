package me.ccattell.plugins.completeeconomy.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Charlie
 */
public class CEMinerListener implements Listener {

    private CompleteEconomy plugin;
    private List<Integer> blocks;

    public void CEJoinListener(CompleteEconomy plugin) {
        this.plugin = plugin;
        this.blocks = Arrays.asList(new Integer[]{1, 14, 15, 16, 21, 24, 48, 49, 56, 73, 74, 87, 89, 121, 129, 153});
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        // get block
        int id = event.getBlock().getTypeId();
        // is it a mining skills block?
        if (!blocks.contains(id)) {
            return;
        }
        // is the player a miner?
        boolean miner = true;
        if (!miner) {
            return;
        }
        // yes, so add it to the queue
        // will need to determine number of drops based on player skill level
        String name = event.getPlayer().getName();
        int drops = getDropsForSkill(name);
        HashMap<Integer, Integer> counts = plugin.getMiningQueue().get(name);
        if (counts == null) {
            // first time ever mining
            HashMap<Integer, Integer> newcount = new HashMap<Integer, Integer>();
            newcount.put(id, drops);
            plugin.getMiningQueue().put(name, newcount);
        } else {
            int minecount = counts.get(id);
            if (minecount == 0) {
                // first time they've mined this block
                counts.put(id, drops);
            } else {
                // increase count
                counts.put(id, minecount + drops);
            }
            plugin.getMiningQueue().put(name, counts);
        }
    }

    private int getDropsForSkill(String player) {
        // determine number of drops the player should get based on their skill level
        return 1;
    }
}
