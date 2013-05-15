package me.ccattell.plugins.completeeconomy.runnables;

import java.util.HashMap;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;

/**
 *
 * @author Charlie
 */
public class CEMiningRunnable implements Runnable {

    private CompleteEconomy plugin;
    private CEQueryFactory qf;

    public CEMiningRunnable(CompleteEconomy plugin) {
        this.plugin = plugin;
        this.qf = new CEQueryFactory();
    }

    @Override
    public void run() {
        // get own thread to run in
        // get the mining queue
        HashMap<String, HashMap<String, Integer>> queue = plugin.getMiningQueue();
        // copy it
        HashMap<String, HashMap<String, Integer>> work = new HashMap<String, HashMap<String, Integer>>(queue);
        // clear it
        queue.clear();
        int experience = 0;
        // loop through clone
        for (Map.Entry<String, HashMap<String, Integer>> m : work.entrySet()) {
            String p = m.getKey();
            HashMap<String, Integer> counts = m.getValue();
            for (Map.Entry<String, Integer> c : counts.entrySet()) {
                // get the value of mineable from the skills list
                experience += plugin.configs.getSkillList().getInt("Skills.Mining." + c.getKey() + ".Experience") * c.getValue();
            }
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("experience", experience);
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player_name", p);
            // how do we know what job the mining skill belongs to ???
            HashMap<String, String> jobs = new CEQueryFactory().getPlayerJobs(p);
            // check whether a job has mining skills
            String j = "";
            for (Map.Entry<String, String> job : jobs.entrySet()) {
                if (plugin.configs.jobList.getStringList("Jobs." + job.getKey()).contains("Mining")) {
                    j = job.getKey();
                    break;
                }
            }
            where.put("job", j);
            qf.doUpdate("CEJobs", set, where);
        }
        // update database
    }
}
