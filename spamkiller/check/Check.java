package me.kuraky.spamkiller.check;

import me.kuraky.spamkiller.data.PlayerData;

public class Check {

    private String name;
    private int weight;
    private boolean enabled;
    private int maximumMuteTime;
    private boolean blockInstead;

    public Check(String name) {
        this.name = name;
        this.weight = 1;
        this.maximumMuteTime = 0;
        blockInstead = false;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if(weight < 1) weight = 1;
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaximumMuteTime() {
        return maximumMuteTime;
    }

    public void setMaximumMuteTime(int maximumMuteTime) {
        if(maximumMuteTime < 0) maximumMuteTime = 0;
        this.maximumMuteTime = maximumMuteTime;
    }

    public boolean getBlockInstead() {
        return blockInstead;
    }

    public void setBlockInstead(boolean blockInstead) {
        this.blockInstead = blockInstead;
    }

    public int onChat(String message, PlayerData playerData) {
        return 0;
    }
}
