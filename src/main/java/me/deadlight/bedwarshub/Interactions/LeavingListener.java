package me.deadlight.bedwarshub.Interactions;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeavingListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (Utils.outGoingPlayers.contains(event.getPlayer().getName())) {
            Utils.outGoingPlayers.remove(event.getPlayer().getName());
        }
    }

}
