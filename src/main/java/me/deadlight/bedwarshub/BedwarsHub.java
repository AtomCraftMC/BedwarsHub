package me.deadlight.bedwarshub;

import me.deadlight.bedwarshub.Interactions.*;
import me.deadlight.bedwarshub.Objects.Placeholder;
import me.deadlight.bedwarshub.OldWay.Commands;
import me.deadlight.bedwarshub.OldWay.SocketServer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class BedwarsHub extends JavaPlugin {

    private static BedwarsHub bedwarsHub;
    public static BedwarsHub getInstance() {
        return bedwarsHub;
    }
    public SocketServer socketServer;
    public static JedisPool pool;
    public static String serverName;

    @Override
    public void onEnable() {
        bedwarsHub = this;
        // Plugin startup logic
        logConsole(Utils.prefix + " &eStarting BedwarsHub");
        saveDefaultConfig();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new LeavingListener(), this);
        Utils.InitializeItems();
        Utils.InitializeGuis();
        Runnables runnables = new Runnables();
        //startJedisPool();
        //LoadSignData signData = new LoadSignData();
        //signData.loadData();

        getServer().getPluginCommand("bh").setExecutor(new Commands());
        getServer().getPluginCommand("solo").setExecutor(new SoloCommand());
        getServer().getPluginCommand("double").setExecutor(new DoubleCommand());
        getServer().getPluginCommand("triple").setExecutor(new TripleCommand());
        getServer().getPluginCommand("squad").setExecutor(new SquadCommand());
        getServer().getPluginCommand("random").setExecutor(new RandomCommand());
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder(this).register();
        }
        logConsole(Utils.prefix + " &aLoaded BedwarsHub");
        logConsole(Utils.prefix + " &bSTARTING REDIS");
//        SocketServer server = new SocketServer();
//        this.socketServer = server;
        pool = new JedisPool("127.0.0.1", 6379);
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
                    String arenas = j.get("bedwars1");
                    String arenas2 = j.get("bedwars2");
                    try {
                        ProcessData.PorcessIncomingData(arenas, "bedwars1");
                        ProcessData.PorcessIncomingData(arenas2, "bedwars2");
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
