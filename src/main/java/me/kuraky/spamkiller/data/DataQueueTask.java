package me.kuraky.spamkiller.data;

import me.kuraky.spamkiller.config.ConfigManager;

import java.util.ArrayList;

public class DataQueueTask implements Runnable {

    @Override
    public void run() {
        ArrayList<PlayerData> toRemove = new ArrayList<>();

        for(PlayerData playerData : DataManager.getDataRemoveQueue().keySet()) {
            if(System.currentTimeMillis() - DataManager.getDataRemoveQueue().get(playerData) > ConfigManager.getDataTaskPeriod()*50) {
                toRemove.add(playerData);
            }
        }

        for(PlayerData playerData : toRemove) {
            DataManager.fullRemove(playerData.getUuid());
        }
    }
}
