package me.ccattell.plugins.completeeconomy.runnables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.QueryFactory;
import me.ccattell.plugins.completeeconomy.database.SkillsResultSet;
import me.ccattell.plugins.completeeconomy.utilities.ToolBoost;
import org.bukkit.Material;

/**
 *
 * @author Charlie
 */
public class BreakRunnable implements Runnable {

    private CompleteEconomy plugin;
    private QueryFactory qf;
    private ToolBoost tb;

    public BreakRunnable(CompleteEconomy plugin) {
        this.plugin = plugin;
        this.qf = new QueryFactory();
        this.tb = new ToolBoost();
    }

    @Override
    public void run() {

        // get own thread to run in
        BukkitWorker<Boolean, String, CompleteEconomy> worker = new BukkitWorker<Boolean, String, CompleteEconomy>(plugin) {
            // get the mining queue
            List<BreakData> queue = plugin.getBreakQueue();
            // copy it
            List<BreakData> work = new ArrayList<BreakData>(queue);

            @Override
            protected Boolean doInBackground() throws Exception {
                // clear it
                queue.clear();
                // loop through clone

                for (BreakData m : work) {
                    String p = m.getPlayer();
                    List<String> skills = m.getSkills();
                    double skill_level = getSkillLevel(p);
                    // how do we know what job the mining skill belongs to ???
                    HashMap<String, String> jobs = new QueryFactory().getPlayerJobs(p);
                    // this needs to move...
                    // check whether a job has mining skills
                    String j = "";
                    for (Map.Entry<String, String> job : jobs.entrySet()) {
                        //System.out.println("Job: " + job.getKey() + " - Skill: " + skills);
                        for (String skill : skills) {
                            if (plugin.configs.jobList.getStringList("Jobs." + job.getKey()).contains(skill)) {
                                j = job.getKey();
                                break;
                            }
                        }
                    }
                    double job_boost_percent = getJobBoost(p, j);
                    int drops = m.getDrops();
                    // determine if correct tool
                    double tool_boost_percent = getToolMultiplier(m.getTool(), m.getBlock());
                    // exp = (block_exp * (Math.pow(1.05, skill_level))) * (1 + tool_boost_percent + job_boost_percent));
                    double exp = (plugin.configs.getBlockList().getInt(m.getBlock() + ".break.exp") * (Math.pow(1.05, skill_level))) * (1 + tool_boost_percent + job_boost_percent);
                    //System.out.println("Experience: " + exp);
                    qf.alterExperience(exp, p, j);

                    // calculate pay - pay = block_pay * (Math.pow(1.04, job_level));
                    // calculate pay - pay = (block_pay * (Math.pow(1.04, job_level))) * drops; ?

                }
                return true;
            }
        };
        worker.execute();
    }

    public double getSkillLevel(String player) {
        double power = 1;
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player_name", player);
        SkillsResultSet srs = new SkillsResultSet(where);
        if (srs.resultSet()) {
            power = srs.getLevel();
        }
        return Math.pow(1.05, power);
    }

    public double getJobBoost(String player, String job) {
        // not implemented yet
        return 0.00D;
    }

    public double getToolMultiplier(int tool, String block) {
        double m = 0.00D;
        String[] data = block.split(":");
        int id = Material.getMaterial(data[0]).getId();
        List<Integer> validTools = tb.getLookup().get(id);
        if (validTools.contains(tool)) {
            m = plugin.getConfig().getDouble("System.Levels.ValidTool") / 10D;
        }
        return m;
    }
}
