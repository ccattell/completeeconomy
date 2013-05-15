package me.ccattell.plugins.completeeconomy.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Charlie
 */
public class CEMiningListener implements Listener {

    private CompleteEconomy plugin;
    private Set<String> blocks;

    public void CEMiningListener(CompleteEconomy plugin) {
        this.plugin = plugin;
        // list is static for now, but should be determined by which blocks are attached to the mining skill
        this.blocks = plugin.configs.skillList.getConfigurationSection("Skill.Mining.break").getKeys(false);
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        // get block
        String type = event.getBlock().getType().toString();
        // is it a mining skills block?
        if (!blocks.contains(type)) {
            return;
        }
        // does the player have mining skills as part of their job description?
        if (!hasMiningSkill(event.getPlayer().getName())) {
            return;
        }
        // yes & yes, so add it to the queue
        // will need to determine number of drops based on player skill level
        String name = event.getPlayer().getName();
        int drops = getDropsForSkill(name);
        HashMap<String, Integer> counts = plugin.getMiningQueue().get(name);
        if (counts == null) {
            // first time ever mining
            HashMap<String, Integer> newcount = new HashMap<String, Integer>();
            newcount.put(name, drops);
            plugin.getMiningQueue().put(name, newcount);
        } else {
            int minecount = counts.get(name);
            if (minecount == 0) {
                // first time they've mined this block
                counts.put(name, drops);
            } else {
                // increase count
                counts.put(name, minecount + drops);
            }
            plugin.getMiningQueue().put(name, counts);
        }
    }

    private int getDropsForSkill(String player) {
        // determine number of drops the player should get based on their skill level
        return 1;
    }

    private boolean hasMiningSkill(String p) {
        // get active player jobs
        HashMap<String, String> jobs = new CEQueryFactory().getPlayerJobs(p);
        if (jobs.size() > 0) {
            // check whether a job has mining skills
            for (Map.Entry<String, String> job : jobs.entrySet()) {
                if (plugin.configs.jobList.getStringList("Jobs." + job.getKey()).contains("Mining")) {
                    return true;
                }
            }
        }
        return false;
    }
}
