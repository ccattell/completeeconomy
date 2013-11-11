package me.ccattell.plugins.completeeconomy.runnables;

import java.util.List;

/**
 * This is a simple data storage class. Each time a block is broken we need to
 * store some information about it in this handy little container. This is
 * because Java is sucky at holding arrays of data of different data types.
 *
 * @author charlie
 */
public class BreakData {

    private String player;
    private String block;
    private List<String> skills;
    private String job;
    private int tool;
    private int drops;

    // class constructor
    public BreakData() {
    }

    // getters and setters
    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getTool() {
        return tool;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    public int getDrops() {
        return drops;
    }

    public void setDrops(int drops) {
        this.drops = drops;
    }
}
