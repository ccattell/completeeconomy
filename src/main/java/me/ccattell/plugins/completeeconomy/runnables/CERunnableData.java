/*
 *  Copyright 2013 eccentric_nz.
 */
package me.ccattell.plugins.completeeconomy.runnables;

/**
 *
 * @author eccentric_nz
 */
public class CERunnableData {

    private String player;
    private String block;
    private String skill;
    private String job;
    private boolean correctTool;
    private int count;

    public CERunnableData() {
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

    public boolean isCorrectTool() {
        return correctTool;
    }

    public void setCorrectTool(boolean correctTool) {
        this.correctTool = correctTool;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
