package me.deadlight.bedwarshub;
import me.deadlight.bedwarshub.Interactions.*;
import me.deadlight.bedwarshub.Objects.Game;
import me.deadlight.bedwarshub.Objects.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public final class BedwarsHub extends JavaPlugin {

    private static BedwarsHub bedwarsHub;
    public static BedwarsHub getInstance() {
        return bedwarsHub;
    }
    public static JedisPool pool;
    public static String serverName;
    public static List<String> gameServers;

    @Override
    public void onEnable() {
        bedwarsHub = this;
        // Plugin startup logic
        logConsole(Utils.prefix + " &eStarting BedwarsHub");
        saveDefaultConfig();
        gameServers = getConfig().getStringList("gameservers");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new LeavingListener(), this);
        Utils.InitializeItems();
        Utils.InitializeGuis();
        Runnables runnables = new Runnables();
        //startJedisPool();
        //LoadSignData signData = new LoadSignData();
        //signData.loadData();

        getServer().getPluginCommand("solo").setExecutor(new SoloCommand());
        getServer().getPluginCommand("double").setExecutor(new DoubleCommand());
        getServer().getPluginCommand("triple").setExecutor(new TripleCommand());
        getServer().getPluginCommand("squad").setExecutor(new SquadCommand());
        getServer().getPluginCommand("random").setExecutor(new RandomCommand());
        getServer().getPluginCommand("adminpanel").setExecutor(new StaffGuiCommand());
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder(this).register();
        }
        logConsole(Utils.prefix + " &aLoaded BedwarsHub");
        logConsole(Utils.prefix + " &bSTARTING REDIS");
//        SocketServer server = new SocketServer();
//        this.socketServer = server;
        pool = new JedisPool(getConfig().getString("ip"), 6379);
        Bukkit.broadcastMessage("tst");
        serverName = getConfig().getString("servername");
        startThePool();
        logConsole(Utils.prefix + " &aRedis connection finished");
        cosmeticsReload();



    }

    public void cosmeticsReload() {
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console, "/procosmetics reload");
            }
        }, 0, 20 * 300);
    }

    public void startThePool() {

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                Jedis j = null;
                try {
                    j = pool.getResource();
                    // If you want to use a password, use
                    j.auth("piazcraftmc");
                    try {
                        List<Game> finalGameServers = new ArrayList<>();
                        for (String gameServer : gameServers) {
                            String arenas = j.get(gameServer);
                            if (arenas == null) {
                                continue;
                            }
                            List<Game> games = ProcessData.PorcessIncomingData(arenas, gameServer);
                            finalGameServers.addAll(games);
                        }
                        Utils.sortFinalGameServers(finalGameServers);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } finally {
                    // Be sure to close it! It can and will cause memory leaks.
                    if (j != null) {
                        j.close();
                    }

                }
            }
        }, 0, 3);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

            @Override
            public void run() {
                Jedis j = null;
                try {
                    j = pool.getResource();
                    // If you want to use a password, use
                    j.auth("piazcraftmc");
                    j.set("count-" + serverName, String.valueOf(getServer().getOnlinePlayers().size()));
                    j.expire("count-" + serverName, 2);
                } finally {
                    // Be sure to close it! It can and will cause memory leaks.
                    if (j != null) {
                        j.close();
                    }
                }

            }
        }, 0, 20);
    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pool.close();
        try {
            getServer().getScheduler().cancelTasks(this);
            Utils.soloArenaGui.getInventory().getViewers().forEach(HumanEntity::closeInventory);
            Utils.doubleArenaGui.getInventory().getViewers().forEach(HumanEntity::closeInventory);
            Utils.tripleArenaGui.getInventory().getViewers().forEach(HumanEntity::closeInventory);
            Utils.squadArenaGui.getInventory().getViewers().forEach(HumanEntity::closeInventory);
        } catch (Exception e) {

        }
        //socketServer.stop();

    }

    public void logConsole(String message) {
        getServer().getConsoleSender().sendMessage(Utils.colorify(message));
    }


}
