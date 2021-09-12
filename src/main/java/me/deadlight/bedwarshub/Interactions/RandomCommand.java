package me.deadlight.bedwarshub.Interactions;

import me.deadlight.bedwarshub.Objects.Game;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        List<Game> allGames = new ArrayList<>();
        allGames.addAll(Utils.soloArenasList);
        allGames.addAll(Utils.doubleArenasList);
        allGames.addAll(Utils.tripleArenasList);
        allGames.addAll(Utils.squadArenasList);
        List<Game> newArenas = new ArrayList<>();
        for (Game game : allGames) {
            if (game.state.equalsIgnoreCase("starting") && game.currentcount != game.max) {
                newArenas.add(game);
                continue;
            }
            if (game.state.equalsIgnoreCase("open")) {
                newArenas.add(game);
            }

        }
        Collections.shuffle(newArenas);
        Collections.sort(newArenas);
        Collections.reverse(newArenas);
        if (newArenas.size() != 0) {
            Game theGame = newArenas.get(0);
            player.sendMessage(Utils.colorify("&aDar hale etesal..."));
            Utils.sendPlayerJoinRequest(player, theGame.arenaid, theGame.server);
        } else {

            player.sendMessage(Utils.colorify("&cMoteasefane matchi dar hale hazer vojod nadarad. :( kami sabr konid"));
        }



        return false;
    }
}
