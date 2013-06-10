package me.ccattell.plugins.completeeconomy.runnables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;

/**
 *
 * @author Charlie
 */
public class CEBreakRunnable implements Runnable {

    private CompleteEconomy plugin;
    private CEQueryFactory qf;

    public CEBreakRunnable(CompleteEconomy plugin) {
        this.plugin = plugin;
        this.qf = new CEQueryFactory();
    }

    @Override
    public void run() {
        // get own thread to run in
        CEBukkitWorker<Boolean, String, CompleteEconomy> worker = new CEBukkitWorker<Boolean, String, CompleteEconomy>(plugin) {
            // get the mining queue
            List<CEBreakData> queue = plugin.getBreakQueue();
            // copy it
            List<CEBreakData> work = new ArrayList<CEBreakData>(queue);

            @Override
            protected Boolean doInBackground() throws Exception {
                // clear it
                queue.clear();
                // loop through clone
                for (CEBreakData m : work) {
                    String p = m.getPlayer();
                    String skill = m.getSkill();
                    double skill_level = getSkillLevel(p, skill);
                    // how do we know what job the mining skill belongs to ???
                    HashMap<String, String> jobs = new CEQueryFactory().getPlayerJobs(p);
                    // this needs to move...
                    // check whether a job has mining skills
                    String j = "";
                    for (Map.Entry<String, String> job : jobs.entrySet()) {
                        if (plugin.configs.jobList.getStringList("Jobs." + job.getKey()).contains(skill)) {
                            j = job.getKey();
                            break;
                        }
                    }
                    double job_boost_percent = getJobBoost(p, j);
                    int drops = m.getDrops();
                    // determine if correct tool
                    int tool_boost_percent = getToolMultiplier(m.getTool(), m.getBlock());
                    // exp = (block_exp * (1.05 ^ skill_level)) * (1 + tool_boost_percent + job_boost_percent));
                    double exp = (plugin.configs.getBlockList().getInt(m.getBlock() + "break.experience") * skill_level) * (1 + tool_boost_percent + job_boost_percent);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("experience", exp);
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player_name", p);
                    where.put("job", j);
                    qf.doUpdate("CEJobs", set, where);

                    // calculate pay
                }
                return true;
            }
        };
        worker.execute();
    }

    public double getSkillLevel(String player, String skill) {
        return 1.05D;
    }

    public double getJobBoost(String player, String job) {
        return 1D;
    }

    public int getToolMultiplier(int tool, String block) {
        return 1;
    }
}
