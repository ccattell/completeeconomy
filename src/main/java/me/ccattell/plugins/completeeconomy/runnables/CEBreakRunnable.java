package me.ccattell.plugins.completeeconomy.runnables;

import java.util.HashMap;
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
            HashMap<String, HashMap<String, CERunnableData>> queue = plugin.getBreakQueue();
            // copy it
            HashMap<String, HashMap<String, CERunnableData>> work = new HashMap<String, HashMap<String, CERunnableData>>(queue);
            // clear it

            @Override
            protected Boolean doInBackground() throws Exception {
                queue.clear();
                int experience = 0;
                String skill = "";
                // loop through clone
                for (Map.Entry<String, HashMap<String, CERunnableData>> m : work.entrySet()) {
                    String p = m.getKey();
                    HashMap<String, CERunnableData> counts = m.getValue();
                    for (Map.Entry<String, CERunnableData> c : counts.entrySet()) {
                        // get the value of mineable from the skills list
                        experience += plugin.configs.getSkillList().getInt(c.getKey() + "break.experience") * c.getValue().getCount();
                        skill = c.getValue().getSkill();
                    }
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("experience", experience);
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player_name", p);
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
                    where.put("job", j);
                    qf.doUpdate("CEJobs", set, where);

                    // ----
                }
                return true;
            }
        };
        worker.execute();
    }
}
