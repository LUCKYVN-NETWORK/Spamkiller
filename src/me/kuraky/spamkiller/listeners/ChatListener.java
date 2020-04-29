package me.kuraky.spamkiller.listeners;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.check.CheckManager;
import me.kuraky.spamkiller.log.LogManager;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.DataManager;
import me.kuraky.spamkiller.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(!event.getPlayer().hasPermission("spamkiller.exempt")) {
            PlayerData playerData = DataManager.getDataFromUUID(event.getPlayer().getUniqueId());
            playerData.updateMuteTime();

            if (playerData.getMuteTime() > 0) {
                event.setCancelled(true);
                if (!ConfigManager.getInMuteMessage().equals("none"))
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.replacePlaceholders(
                            ConfigManager.getInMuteMessage(), playerData.getMuteTime(), null
                    )));
            }

            if (!event.isCancelled()) {
                int chatDelay = 0;
                String message = event.getMessage();

                playerData.addMessage(message);

                if (ConfigManager.getAccountForNicknames()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        message = message.replace(player.getName(), "");
                    }
                    if (message.length() == 0) message = " ";
                }

                boolean block = false;

                ArrayList<String> logMessages = new ArrayList<>();
                logMessages.add(event.getPlayer().getName() + ": " + message);

                for (Check check : CheckManager.getChecks()) {
                    if (check.isEnabled() && check.getWeight() > 0) {
                        int mute = check.onChat(message, playerData) * check.getWeight();

                        if (mute > 0) {
                            StringBuilder logMessage = new StringBuilder();
                            logMessage.append(check.getName()).append(": ");

                            int checkMaximumTime = check.getMaximumMuteTime();

                            if (check.getBlockInstead()) {
                                mute = 0;
                                block = true;
                                logMessage.append("Block");
                            } else if (mute > checkMaximumTime) {
                                logMessage.append(mute).append("(").append(checkMaximumTime).append(")");
                                mute = checkMaximumTime;
                            } else logMessage.append(mute);

                            logMessages.add(logMessage.toString());
                        }
                        chatDelay += mute;
                    }
                }

                playerData.setMuteTime(chatDelay);

                if (block || playerData.getMuteTime() > 0 && ConfigManager.getEnableLogs()) LogManager.log(logMessages);

                if (chatDelay >= ConfigManager.getMinimumMuteTime() / 3 && playerData.getMuteTime() == 0 && !block) {
                    String warningMessage = ConfigManager.getWarningMessage();
                    if (!warningMessage.equals("none"))
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.replacePlaceholders(
                                warningMessage, null, null)));
                }

                if (playerData.getMuteTime() >= 0) {
                    if (ConfigManager.getBlockLastMessage() && playerData.getMuteTime() > 0) event.setCancelled(true);
                    else if (block) {
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.replacePlaceholders(
                                ConfigManager.getBlockMessage(), null, null)));
                        playerData.removeFirstMessage();
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
