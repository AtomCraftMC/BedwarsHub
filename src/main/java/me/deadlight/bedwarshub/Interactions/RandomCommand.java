package me.deadlight.bedwarshub.Interactions;

import me.deadlight.bedwarshub.Objects.Game;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

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
        HashMap<String, Integer> serverCounts = new HashMap<>();
        List<Game> newArenas = new ArrayList<>();
        for (Game game : allGames) {
            Utils.addCountToHashMap(serverCounts, game.server, game.currentcount);
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
        List<Game> zeroArenas = new ArrayList<>(newArenas);
        newArenas.removeIf(theArena -> theArena.currentcount == 0);
        zeroArenas.removeIf(theArena -> theArena.currentcount > 0);
        Collections.shuffle(zeroArenas);
        //List<Game> zeroArenas = newArenas;
        //custom comparator
        Comparator<Game> compareByServerCount = new Comparator<Game>() {
            @Override
            public int compare(Game arena1, Game arena2) {
                int arena1servercount = serverCounts.get(arena1.server);
                int arena2servercount =serverCounts.get(arena2.server);
                return Integer.compare(arena1servercount, arena2servercount);
            }
        };
        zeroArenas.sort(compareByServerCount);

        newArenas.addAll(zeroArenas);


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
