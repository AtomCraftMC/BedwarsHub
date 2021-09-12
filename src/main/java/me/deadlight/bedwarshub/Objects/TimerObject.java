package me.deadlight.bedwarshub.Objects;

import me.deadlight.bedwarshub.BedwarsHub;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TimerObject {

    public String playerName;

    public TimerObject(Player player) {
        this.playerName = player.getName();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(BedwarsHub.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (Utils.outGoingPlayers.contains(playerName)) {
                    Utils.outGoingPlayers.remove(playerName);
                    player.sendMessage(Utils.colorify("&cMoteasefane ghader be ersale shoma be in match nashodim, yek bar digar emtehan konid."));
                }
            }
        }, 20 * 10);
    }




}
