package me.deadlight.bedwarshub.Interactions;

import me.deadlight.bedwarshub.Objects.Game;
import me.deadlight.bedwarshub.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ProcessData {

    static JSONParser parser = new JSONParser();

    public static List<Game> PorcessIncomingData(String data, String server) throws ParseException {
        //type: arena, playerleft, playerjoin, arenaend, arenastart
        try {
            JSONObject json = (JSONObject) parser.parse(data);
            String type = (String) json.get("type");
            String serverName = (String) json.get("server");
            if (type.equalsIgnoreCase("arenas")) {
                //update arena

                //new update processing data system
                List<Game> thegames = new ArrayList<>();
                List<List<String>> arenasList = (List<List<String>>) json.get("data");
                for (List<String> arena : arenasList) {
                    Game game = new Game(arena.get(0), Integer.parseInt(arena.get(1)), Integer.parseInt(arena.get(2)), Integer.parseInt(arena.get(3)), arena.get(4), Integer.parseInt(arena.get(5)), Integer.parseInt(arena.get(6)), new ArrayList<>(), arena.get(8));
                }
                return thegames;

                //end of the new system


//                if (serverName.equalsIgnoreCase("bedwars1")) {
//                    List<Game> soloGames = new ArrayList<>();
//                    List<Game> tripleGames = new ArrayList<>();
//                    List<List<String>> arenasList = (List<List<String>>) json.get("data");
//                    for (List<String> arena : arenasList) {
//                        Game game = new Game(arena.get(0), Integer.parseInt(arena.get(1)), Integer.parseInt(arena.get(2)), Integer.parseInt(arena.get(3)), arena.get(4), Integer.parseInt(arena.get(5)), Integer.parseInt(arena.get(6)), new ArrayList<>(), arena.get(8));
//
//                        if (game.gameType == 1) {
//                            soloGames.add(game);
//                        } else if (game.gameType == 3) {
//                            tripleGames.add(game);
//                        }
//                    }
//
//                    Utils.soloArenasList = soloGames;
//                    Utils.tripleArenasList = tripleGames;
//                } else if (serverName.equalsIgnoreCase("bedwars2")) {
//                    List<Game> DoubleGames = new ArrayList<>();
//                    List<Game> SquadGames = new ArrayList<>();
//                    List<List<String>> arenasList = (List<List<String>>) json.get("data");
//                    for (List<String> arena : arenasList) {
//                        Game game = new Game(arena.get(0), Integer.parseInt(arena.get(1)), Integer.parseInt(arena.get(2)), Integer.parseInt(arena.get(3)), arena.get(4), Integer.parseInt(arena.get(5)), Integer.parseInt(arena.get(6)), new ArrayList<>(), arena.get(8));
//
//                        if (game.gameType == 2) {
//                            DoubleGames.add(game);
//                        } else if (game.gameType == 4) {
//                            SquadGames.add(game);
//                        }
//                    }
//                    Utils.doubleArenasList = DoubleGames;
//                    Utils.squadArenasList = SquadGames;
//                }



            } else {
                //felan bejoz communicate arena ha chize digeyi nemigirim so yep
                return new ArrayList<>();
            }
        } catch (Exception error) {
            return new ArrayList<>();
        }


    }


//    public static void ProcessOutgoingData(String type, ) {
//        //solo/triple = bedwars1 | duo/squad = bedwars2
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type", "comingplayer");
//        jsonObject.put()
//    }


}
