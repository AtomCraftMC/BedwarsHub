package me.deadlight.bedwarshub.OldWay;

import me.deadlight.bedwarshub.BedwarsHub;
import me.deadlight.bedwarshub.Interactions.ProcessData;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.net.*;
import java.io.*;

public class SocketServer {
    private ServerSocket serverSocket;
    public EchoClientHandler bedwars1;
    public EchoClientHandler bedwars2;

    public void sendMessage(String msg,String type) {
        if (type.equalsIgnoreCase("bedwars1")) {
            bedwars1.sendMessage(msg);
        } else if (type.equalsIgnoreCase("bedwars2")) {
            bedwars2.sendMessage(msg);
        }

    }
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true){

                Socket client = null;
                client = serverSocket.accept();
                new EchoClientHandler(client,this).run();



            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }


    public void stop() {
        try {

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class EchoClientHandler {
        private SocketServer server;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private BukkitTask taskID;

        public EchoClientHandler(Socket socket,SocketServer server) {
            this.server = server;
            this.clientSocket = socket;
        }
        public void sendMessage(String msg) {
            out.println(msg);
        }
        public void run() {
            EchoClientHandler BO = this;
            taskID = Bukkit.getScheduler().runTaskAsynchronously(BedwarsHub.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String inputLine;

                        out.println("name-give-me-plz");

                        while ((inputLine = in.readLine()) != null) {
                            try {
                                if (inputLine.contains("offline@")) {
                                    String serverName = inputLine.split("@")[1];
                                    if (serverName.equalsIgnoreCase("bedwars1")) {
                                        Utils.soloArenasList.clear();
                                        Utils.tripleArenasList.clear();
                                        server.bedwars1 = null;
                                    } else if (serverName.equalsIgnoreCase("bedwars2")) {
                                        Utils.doubleArenasList.clear();
                                        Utils.squadArenasList.clear();
                                        server.bedwars2 = null;
                                    }
                                    clientSocket.close();
                                    taskID.cancel();
                                    break;
                                }else if (inputLine.contains("secret-esm-yo!")) {
                                    String[] ds = inputLine.split("!");
                                    String name = ds[1];
                                    if (name.equalsIgnoreCase("bedwars1")) {
                                        server.bedwars1 = BO;
                                    } else if (name.equalsIgnoreCase("bedwars2")) {
                                        server.bedwars2 = BO;
                                    }
                                    continue;
                                }

                                ProcessData.PorcessIncomingData(inputLine, ""); //change if reverting

                            } catch(Exception e) {
                                e.printStackTrace();
                            }

                        }

                        in.close();
                        out.close();
                        clientSocket.close();

                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            });
        }
    }

    public SocketServer() {
        try {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(BedwarsHub.getInstance(),() -> this.start(5555), 0);
        } catch (Exception ex) {

        }

    }

}
