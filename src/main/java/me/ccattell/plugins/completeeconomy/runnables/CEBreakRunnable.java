package me.ccattell.plugins.completeeconomy.runnables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import me.ccattell.plugins.completeeconomy.database.CESkillsResultSet;
import me.ccattell.plugins.completeeconomy.utilities.CEToolBoost;
import org.bukkit.Material;

/**
 *
 * @author Charlie
 */
public class CEBreakRunnable implements Runnable {

    private CompleteEconomy plugin;
    private CEQueryFactory qf;
    private CEToolBoost tb;

    public CEBreakRunnable(CompleteEconomy plugin) {
        this.plugin = plugin;
        this.qf = new CEQueryFactory();
        this.tb = new CEToolBoost();
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
                    List<String> skills = m.getSkills();
                    double skill_level = getSkillLevel(p);
                    // how do we know what job the mining skill belongs to ???
                    HashMap<String, String> jobs = new CEQueryFactory().getPlayerJobs(p);
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
        CESkillsResultSet srs = new CESkillsResultSet(where);
        if (srs.resultSet()) {
            power = srs.getLevel();
        }
        return Math.pow(1.05, power);
    }

    public double getJobBoost(String player, String job) {
        return 0.01D;
    }

    public double getToolMultiplier(int tool, String block) {
        double m = 0.01D;
        String[] data = block.split(":");
        int id = Material.getMaterial(data[0]).getId();
        List<Integer> validTools = tb.getLookup().get(id);
        if (validTools.contains(tool)) {
            m = plugin.getConfig().getDouble("System.Levels.ValidTool") / 100D;
        }
        return m;
    }
}
