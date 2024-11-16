package me.kuraky.spamkiller.data;

import me.kuraky.spamkiller.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.UUID;

public class PlayerData {

    //GLOBAL MESSAGE LIST FOR GROUP SPAM CHECK
    private static LinkedList<String> globalMessages = new LinkedList<>();
    private static LinkedList<UUID> globalSenders = new LinkedList<>();

    //PLAYER UUID
    private UUID uuid;

    //DATA
    private int muteTime;
    private int accumulatedTime;
    private long lastUpdateTime;
    private LinkedList<String> messages;
    private LinkedList<Long> messageTimes;

    //CHECKS DATA
    private int similarityViolations;
    private int typingSpeedViolations;
    private int nonAsciiCharactersViolations;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;

        //DATA
        this.muteTime = 0;
        this.accumulatedTime = 0;
        this.lastUpdateTime = System.currentTimeMillis();
        this.messages = new LinkedList<>();
        this.messageTimes = new LinkedList<>();

        //CHECKS DATA
        similarityViolations = 0;
        typingSpeedViolations = 0;
        nonAsciiCharactersViolations = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getMuteTime() {
        return muteTime;
    }

    public LinkedList<Long> getMessageTimes() {
        if(messageTimes == null) { //in case of a weird error
            messageTimes = new LinkedList<>();
        }
        return messageTimes;
    }

    public LinkedList<String> getMessages() {
        if(messages == null) { //same as above
            messages = new LinkedList<>();
        }
        return messages;
    }

    public static LinkedList<String> getGlobalMessages() {
        if(globalMessages == null) { //same as above
            globalMessages = new LinkedList<>();
            Bukkit.getLogger().warning("[Spamkiller] Null global messages list");
        }
        return globalMessages;
    }

    public static LinkedList<UUID> getGlobalSenders() {
        if(globalSenders == null) { //same as above
            globalSenders = new LinkedList<>();
            Bukkit.getLogger().warning("[Spamkiller] Null global senders list");
        }
        return globalSenders;
    }

    public void setMuteTime(int time) {
        updateMuteTime();

        muteTime = time + accumulatedTime;
        accumulatedTime += time;

        if(muteTime < ConfigManager.getMinimumMuteTime()) {
            muteTime = 0;
        }
        else {
            accumulatedTime = 0;
            Player targetPlayer = Bukkit.getPlayer(uuid);
            if(targetPlayer == null) {
                Bukkit.getLogger().warning("Tried to change mute time of an offline player");
                return;
            }

            if(!ConfigManager.getMuteMessage().equals("none")) {
                targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.replacePlaceholders(
                        ConfigManager.getMuteMessage(), muteTime, null
                )));
            }
            if(!ConfigManager.getNotifyMessage().equals("none")) {

                if(muteTime > ConfigManager.getMinimumConsoleNotifyTime() || muteTime > ConfigManager.getMinimumModeratorNotifyTime()) {
                    String message = ConfigManager.replacePlaceholders(
                            ConfigManager.getNotifyMessage(), muteTime, targetPlayer.getName());

                    if(muteTime > ConfigManager.getMinimumConsoleNotifyTime()) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                    if(muteTime > ConfigManager.getMinimumModeratorNotifyTime()) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission("spamkiller.notify")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateMuteTime() {
        muteTime -= System.currentTimeMillis() - lastUpdateTime;
        accumulatedTime -= System.currentTimeMillis() - lastUpdateTime;
        if(muteTime < 0) muteTime = 0;
        if(accumulatedTime < 0) accumulatedTime = 0;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void addMessage(String message) {
        messages.addFirst(message);
        messageTimes.addFirst(System.currentTimeMillis());
        if(messages.size() > 10) {
            messages.removeLast();
            messageTimes.removeLast();
        }
        globalMessages.addFirst(message);
        globalSenders.addFirst(uuid);
        if(globalMessages.size() > 7) {
            globalMessages.removeLast();
            globalSenders.removeLast();
        }
        setTypingSpeedViolations(getTypingSpeedViolations() - 1);
        setNonAsciiCharactersViolations(getNonAsciiCharactersViolations() - 1);
    }

    public void removeFirstMessage() {
        messages.removeFirst();
        messageTimes.removeFirst();
        globalMessages.removeFirst();
        globalSenders.removeFirst();
    }

    public int getSimilarityViolations() {
        return similarityViolations;
    }

    public int getTypingSpeedViolations() {
        return typingSpeedViolations;
    }

    public int getNonAsciiCharactersViolations() {
        return nonAsciiCharactersViolations;
    }

    public void setSimilarityViolations(int similarityViolations) {
        if(similarityViolations < 0) similarityViolations = 0;
        this.similarityViolations = similarityViolations;
    }

    public void setTypingSpeedViolations(int typingSpeedViolations) {
        if(typingSpeedViolations < 0) typingSpeedViolations = 0;
        this.typingSpeedViolations = typingSpeedViolations;
    }

    public void setNonAsciiCharactersViolations(int nonAsciiCharactersViolations) {
        if(nonAsciiCharactersViolations < 0) nonAsciiCharactersViolations = 0;
        this.nonAsciiCharactersViolations = nonAsciiCharactersViolations;
    }
}
