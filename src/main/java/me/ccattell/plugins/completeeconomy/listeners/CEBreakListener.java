package me.ccattell.plugins.completeeconomy.listeners;

import java.util.HashMap;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import me.ccattell.plugins.completeeconomy.runnables.CERunnableData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Charlie
 */
public class CEBreakListener implements Listener {

    private CompleteEconomy plugin;

    public void CEBreakListener(CompleteEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // get block
        String type = event.getBlock().getType().toString();
        byte data = event.getBlock().getData();
        String block = (data > 0) ? type + ":" + data : type;

        // is it a breakable block?
        if (!plugin.configs.blockList.contains(block + ".break")) {
            return;
        }
        // does the player have mining skills as part of their job description?
        if (!hasBreakSkill(event.getPlayer().getName(), plugin.configs.blockList.getString(block + ".break.skill"))) {
            return;
        }
        // yes & yes, so add it to the queue
        // will need to determine number of drops based on player skill level
        String name = event.getPlayer().getName();
        int drops = getDropsForSkill(name);
        HashMap<String, CERunnableData> counts = plugin.getBreakQueue().get(name);
        if (counts == null) {
            // first time ever mining
            HashMap<String, CERunnableData> newcount = new HashMap<String, CERunnableData>();
            CERunnableData rd = new CERunnableData();
            rd.setCount(drops);
            rd.setSkill(plugin.configs.blockList.getString(block + ".break.skill"));
            newcount.put(name, rd);
            plugin.getBreakQueue().put(name, newcount);
        } else {
            CERunnableData rd_data = counts.get(block);
            if (rd_data == null) {
                // first time they've broken this block
                rd_data = new CERunnableData();
                rd_data.setSkill(plugin.configs.blockList.getString(block + ".break.skill"));
                rd_data.setCount(drops);
            } else {
                int minecount = rd_data.getCount();
                // increase count
                rd_data.setCount(minecount + drops);
            }
            counts.put(block, rd_data);
            plugin.getBreakQueue().put(name, counts);
        }
    }

    private int getDropsForSkill(String player) {
        // determine number of drops the player should get based on their skill level
        return 1;
    }

    private boolean hasBreakSkill(String p, String s) {
        // get active player jobs
        HashMap<String, String> jobs = new CEQueryFactory().getPlayerJobs(p);
        if (jobs.size() > 0) {
            // check whether a job has mining skills
            for (Map.Entry<String, String> job : jobs.entrySet()) {
                if (plugin.configs.jobList.getStringList("Jobs." + job.getKey()).contains("s")) {
                    return true;
                }
            }
        }
        return false;
    }
}
