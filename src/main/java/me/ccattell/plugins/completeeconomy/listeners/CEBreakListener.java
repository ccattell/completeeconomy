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
        /*
         * Player break a block
         * 
         * 1) we check the block list for the skills, pay, and exp associated with breaking the block
         * 
         * 2) we check the internal block list for valid tool type for breaking the block
         * 
         * 4) we check to see if player holds any jobs associated with the skills found in block list
         * 
         * 5) we calculate how much xp we award to the player's skill and job levels (including configurable xp boost for held jobs and valid tool used)
         *      exp = (block_exp * (1.05 ^ skill_level)) * (1 + tool_boost_percent + job_boost_percent));
         * 
         * 6) we calculate how much pay we award to the player's cash on hand for held jobs only
         *      pay = block_pay * (1.04 ^ job_level);
         * 
         * 7) add job xp, skill xp, and pay into player's queue
         */
        if (!plugin.configs.blockList.contains(block + ".break")) {
            // listeners don't return values...
            return;
            // need to check somehwere if the player is using the right tool for this block, so they can get a small boost to thier exp.
            // so the correct tool either needs to added to blocks.yml or we need a separate lookup table
        }
        // does the player have a break skill as part of their job description?
        if (!hasBreakSkill(event.getPlayer().getName(), plugin.configs.blockList.getString(block + ".break.skill"))) {
            return;
        }
        // yes & yes, so add it to the queue
        // will need to determine number of drops based on player skill level
        String name = event.getPlayer().getName();
        int drops = getDropsForSkill(name);
        HashMap<String, CERunnableData> counts = plugin.getBreakQueue().get(name);
        if (counts == null) {
            // first time ever breaking
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
            // check whether a job has break skills
            for (Map.Entry<String, String> job : jobs.entrySet()) {
                if (plugin.configs.jobList.getStringList("Jobs." + job.getKey()).contains("s")) {
                    return true;
                }
            }
        }
        return false;
    }
}
