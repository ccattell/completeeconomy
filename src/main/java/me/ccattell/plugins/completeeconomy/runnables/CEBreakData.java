/*
 *  Copyright 2013 eccentric_nz.
 */
package me.ccattell.plugins.completeeconomy.runnables;

/**
 *
 * @author eccentric_nz
 */
public class CEBreakData {

    private String player;
    private String block;
    private String skill;
    private String job;
    private int tool;
    private int drops;

    public CEBreakData() {
    }

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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
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
