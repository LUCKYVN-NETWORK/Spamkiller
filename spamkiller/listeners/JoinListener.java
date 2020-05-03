package me.kuraky.spamkiller.listeners;

import me.kuraky.spamkiller.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DataManager.addPlayer(event.getPlayer().getUniqueId());
        DataManager.removeFromRemoveQueue(event.getPlayer().getUniqueId());
    }
}
