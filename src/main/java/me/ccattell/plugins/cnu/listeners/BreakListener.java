package me.ccattell.plugins.cnu.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.cnu.CompleteNovusUtilities;
import me.ccattell.plugins.cnu.database.QueryFactory;
import me.ccattell.plugins.cnu.runnables.BreakData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Charlie
 */
public class BreakListener implements Listener {

    private CompleteNovusUtilities plugin;

    public BreakListener(CompleteNovusUtilities plugin) {
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
         * a) check if the player has the skill and the block has the break
         *
         * b) add data to a data class and then add the class to the queue
         *
         * c) the end, keep the listener simple - no major calculations are done here
         *
         * ****** Everything below here is done on the queued items (in the BreakRunnable class), NOT in the listener ************
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
         * 7) give job xp, skill xp, and pay to player
         */
        if (!plugin.configs.blockList.contains(block + ".break")) {
            return;
        }
        // does the player have a break skill as part of their job description?
        if (!hasBreakSkill(event.getPlayer().getName(), plugin.configs.blockList.getStringList(block + ".break.skill"))) {
            return;
        }
        // yes & yes, so add it to the queue
        // will need to determine number of drops based on player skill level
        String name = event.getPlayer().getName();
        // BreakData is just a storage class it doesn't do anythin except hold onto the data associated with this break
        BreakData rd = new BreakData();
        // add some data for later processing
        rd.setPlayer(name);
        rd.setBlock(block);
        rd.setDrops(getDropsForSkill(name));
        rd.setSkills(plugin.configs.blockList.getStringList(block + ".break.skill"));
        rd.setTool(event.getPlayer().getItemInHand().getTypeId());
        // get the break queue and add the BreakData to it
        plugin.getBreakQueue().add(rd);
    }

    private int getDropsForSkill(String player) {
        // determine number of drops the player should get based on their skill level
        return 1;
    }

    private boolean hasBreakSkill(String p, List<String> list) {
        // get active player jobs
        HashMap<String, String> jobs = new QueryFactory().getPlayerJobs(p);
        if (jobs.size() > 0) {
            // check whether a job has break skills
            for (Map.Entry<String, String> job : jobs.entrySet()) {
                for (String s : list) {
                    if (plugin.configs.jobList.getStringList("Jobs." + job.getKey()).contains(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
