package me.deadlight.bedwarshub.Objects;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Game implements Comparable<Game> {

    public String name; //game name
    public int arenaid;
    public int max; //max player count for this game
    public int currentcount; //current player count in game (or waiting)
    public String state; //open, starting, ingame
    public int startCount; //triggers start
    public int gameType; //1-2-3-4
    public List<String> playersInGame;
    public String server;
    //name, arenaid, max, currentcount, state, startCount, gametype, playerslist, server
    //ingame, starting, open
    public Game(String name,int arenaid, int max, int currentcount, String state, int startCount, int gameType, List<String> playersInGame, String server) {
        this.name = name;
        this.arenaid = arenaid;
        this.max = max;
        this.currentcount = currentcount;
        this.state = state;
        this.startCount = startCount;
        this.gameType = gameType;
        this.playersInGame = playersInGame;
        this.server = server;
    }

    public int compareTo(Game other) {
        return Integer.compare(this.currentcount, other.currentcount);
    }

}
