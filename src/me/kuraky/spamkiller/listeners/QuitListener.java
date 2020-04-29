package me.kuraky.spamkiller.listeners;

import me.kuraky.spamkiller.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DataManager.addToRemoveQueue(event.getPlayer().getUniqueId());
    }
}
