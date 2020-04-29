package me.kuraky.spamkiller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DataManager {

    private static ArrayList<PlayerData> playerDatas = new ArrayList<>();
    private static HashMap<PlayerData, Long> dataRemoveQueue = new HashMap<>();

    public static PlayerData getDataFromUUID(UUID uuid) {
        PlayerData data = null;
        for(PlayerData playerData : playerDatas) {
            if(playerData.getUuid().equals(uuid)) {
                data = playerData;
                break;
            }
        }
        return data;
    }

    public static void addPlayer(UUID uuid) {
        if(getDataFromUUID(uuid) == null) {
            playerDatas.add(new PlayerData(uuid));
        }
    }

    public static void removePlayer(UUID uuid) {
        PlayerData playerData = getDataFromUUID(uuid);
        if(playerData != null) playerDatas.remove(playerData);
    }

    public static void addToRemoveQueue(UUID uuid) {
        PlayerData playerData = getDataFromUUID(uuid);
        if(playerData != null) dataRemoveQueue.put(playerData, System.currentTimeMillis());
    }

    public static void removeFromRemoveQueue(UUID uuid) {
        PlayerData playerData = getDataFromUUID(uuid);
        if(playerData != null) dataRemoveQueue.remove(playerData);
    }

    public static void fullRemove(UUID uuid) { //should only be executed if the player is offline
        removeFromRemoveQueue(uuid); //has to be first
        removePlayer(uuid); //has to be second, otherwise the player won't get removed from the queue
    }

    public static HashMap<PlayerData, Long> getDataRemoveQueue() {
        return dataRemoveQueue;
    }
}
