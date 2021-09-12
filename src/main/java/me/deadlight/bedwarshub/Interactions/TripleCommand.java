package me.deadlight.bedwarshub.Interactions;

import me.deadlight.bedwarshub.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TripleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            Utils.tripleArenaGui.open(player);


        }

        return false;
    }
}
