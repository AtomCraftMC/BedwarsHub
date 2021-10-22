package me.deadlight.bedwarshub.Interactions;

import dev.triumphteam.gui.guis.GuiItem;
import me.deadlight.bedwarshub.BedwarsHub;
import me.deadlight.bedwarshub.Objects.Game;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Runnables {

    public Runnables() {
        soloGuiRunnable();
        doubleGuiRunnable();
        tripleGuiRunnable();
        squadGuiRunnable();
    }
    //ingame, starting, open


    public void soloGuiRunnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BedwarsHub.getInstance(), new Runnable() {
            @Override
            public void run() {


                try {
                    int finalSoloCount = 0;
                    HashMap<String, Integer> serverCounts = new HashMap<>();
                    Map<Game, Integer> arenasWithPlayers = new LinkedHashMap<>();
                    for (Game game : Utils.soloArenasList) {
                        finalSoloCount += game.currentcount;
                        Utils.addCountToHashMap(serverCounts, game.server, game.currentcount);
                        if (!game.state.equalsIgnoreCase("ingame")) {
                            arenasWithPlayers.put(game, game.currentcount);
                        }
                    }
                    Utils.soloCount = finalSoloCount;
                    LinkedHashMap<Game, Integer> sortedMap = new LinkedHashMap<>();
                    arenasWithPlayers.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
                    //the new balancing formulight for GUIs
                    List<Game> startingGames = new ArrayList<>(sortedMap.keySet());
                    List<Game> zeroGames = new ArrayList<>(sortedMap.keySet());
                    zeroGames.removeIf(theArena -> theArena.currentcount > 0);
                    startingGames.removeIf(theArena -> theArena.currentcount == 0);
                    Collections.shuffle(zeroGames);
                    Comparator<Game> compareByServerCount = new Comparator<Game>() {
                        @Override
                        public int compare(Game arena1, Game arena2) {
                            int arena1servercount = serverCounts.get(arena1.server);
                            int arena2servercount = serverCounts.get(arena2.server);
                            return Integer.compare(arena1servercount, arena2servercount);
                        }
                    };
                    zeroGames.sort(compareByServerCount);
                    startingGames.addAll(zeroGames);


                    //soloGui
                    int count = 11;
                    for (Game game : startingGames) {
                        if (count == 16) {
                            continue;
                        }
                        if (game.state.equalsIgnoreCase("starting") && game.max == game.currentcount) {

                            ItemStack itemesh = Utils.fullGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&cʘ &c&l" + game.name + " &cʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Solo (1v1) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&cIn arena por mibashad..."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&cIn game por mibashad!"));
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.soloArenaGui.setItem(count, arenaItem);
                            count = count + 1;

                        } else if (game.state.equalsIgnoreCase("starting")) {
                            ItemStack itemesh = Utils.startingGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&6ʘ &6&l" + game.name + " &6ʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Solo (1v1) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.soloArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        } else {
                            ItemStack itemesh = Utils.openGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&aʘ &a&l" + game.name + " &aʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Solo (1v1) - &e&lOpen &e㋡_/¯"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);


                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.soloArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        }

                    }
                    GuiItem noArenaItem = new GuiItem(Utils.noGameItem, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&cGamei dar hale hazer vojod nadarad!"));
                        event.getWhoClicked().closeInventory();
                    });
                    for (int i = count; i < 16; i++) {
                        Utils.soloArenaGui.setItem(i, noArenaItem);
                    }

                    Utils.soloArenaGui.update();
                }catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 0, 5);
    }

    public void doubleGuiRunnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BedwarsHub.getInstance(), new Runnable() {
            @Override
            public void run() {

                try {
                    int finalDoubleCount = 0;
                    HashMap<String, Integer> serverCounts = new HashMap<>();
                    Map<Game, Integer> arenasWithPlayers = new LinkedHashMap<>();
                    for (Game game : Utils.doubleArenasList) {
                        finalDoubleCount += game.currentcount;
                        Utils.addCountToHashMap(serverCounts, game.server, game.currentcount);
                        if (!game.state.equalsIgnoreCase("ingame")) {
                            arenasWithPlayers.put(game, game.currentcount);
                        }
                    }
                    Utils.doubleCount = finalDoubleCount;
                    LinkedHashMap<Game, Integer> sortedMap = new LinkedHashMap<>();
                    arenasWithPlayers.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
                    //the new balancing formulight for GUIs
                    List<Game> startingGames = new ArrayList<>(sortedMap.keySet());
                    List<Game> zeroGames = new ArrayList<>(sortedMap.keySet());
                    zeroGames.removeIf(theArena -> theArena.currentcount > 0);
                    startingGames.removeIf(theArena -> theArena.currentcount == 0);
                    Collections.shuffle(zeroGames);
                    Comparator<Game> compareByServerCount = new Comparator<Game>() {
                        @Override
                        public int compare(Game arena1, Game arena2) {
                            int arena1servercount = serverCounts.get(arena1.server);
                            int arena2servercount = serverCounts.get(arena2.server);
                            return Integer.compare(arena1servercount, arena2servercount);
                        }
                    };
                    zeroGames.sort(compareByServerCount);
                    startingGames.addAll(zeroGames);


                    //soloGui
                    int count = 11;
                    for (Game game : startingGames) {
                        if (count == 16) {
                            continue;
                        }
                        if (game.state.equalsIgnoreCase("starting") && game.max == game.currentcount) {

                            ItemStack itemesh = Utils.fullGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&cʘ &c&l" + game.name + " &cʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Double (2v2) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&cIn game por mibashad!"));
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.doubleArenaGui.setItem(count, arenaItem);
                            count = count + 1;

                        } else if (game.state.equalsIgnoreCase("starting")) {
                            ItemStack itemesh = Utils.startingGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&6ʘ &6&l" + game.name + " &6ʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Double (2v2) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.doubleArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        } else {
                            ItemStack itemesh = Utils.openGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&aʘ &a&l" + game.name + " &aʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Double (2v2) - &e&lOpen &e㋡_/¯"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);


                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.doubleArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        }

                    }
                    GuiItem noArenaItem = new GuiItem(Utils.noGameItem, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&cGamei dar hale hazer vojod nadarad!"));
                        event.getWhoClicked().closeInventory();
                    });
                    for (int i = count; i < 16; i++) {
                        Utils.doubleArenaGui.setItem(i, noArenaItem);
                    }

                    Utils.doubleArenaGui.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 0, 20);
    }


    public void tripleGuiRunnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BedwarsHub.getInstance(), new Runnable() {
            @Override
            public void run() {

                try {
                    int finalTripleCount = 0;
                    HashMap<String, Integer> serverCounts = new HashMap<>();
                    Map<Game, Integer> arenasWithPlayers = new LinkedHashMap<>();
                    for (Game game : Utils.tripleArenasList) {
                        finalTripleCount += game.currentcount;
                        Utils.addCountToHashMap(serverCounts, game.server, game.currentcount);
                        if (!game.state.equalsIgnoreCase("ingame")) {
                            arenasWithPlayers.put(game, game.currentcount);
                        }
                    }
                    Utils.tripleCount = finalTripleCount;
                    LinkedHashMap<Game, Integer> sortedMap = new LinkedHashMap<>();
                    arenasWithPlayers.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
                    //the new balancing formulight for GUIs
                    List<Game> startingGames = new ArrayList<>(sortedMap.keySet());
                    List<Game> zeroGames = new ArrayList<>(sortedMap.keySet());
                    zeroGames.removeIf(theArena -> theArena.currentcount > 0);
                    startingGames.removeIf(theArena -> theArena.currentcount == 0);
                    Collections.shuffle(zeroGames);
                    Comparator<Game> compareByServerCount = new Comparator<Game>() {
                        @Override
                        public int compare(Game arena1, Game arena2) {
                            int arena1servercount = serverCounts.get(arena1.server);
                            int arena2servercount = serverCounts.get(arena2.server);
                            return Integer.compare(arena1servercount, arena2servercount);
                        }
                    };
                    zeroGames.sort(compareByServerCount);
                    startingGames.addAll(zeroGames);


                    //soloGui
                    int count = 11;
                    for (Game game : startingGames) {
                        if (count == 16) {
                            continue;
                        }
                        if (game.state.equalsIgnoreCase("starting") && game.max == game.currentcount) {

                            ItemStack itemesh = Utils.fullGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&cʘ &c&l" + game.name + " &cʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Triple (3v3) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&cIn game por mibashad!"));
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.tripleArenaGui.setItem(count, arenaItem);
                            count = count + 1;

                        } else if (game.state.equalsIgnoreCase("starting")) {
                            ItemStack itemesh = Utils.startingGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&6ʘ &6&l" + game.name + " &6ʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Triple (3v3) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.tripleArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        } else {
                            ItemStack itemesh = Utils.openGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&aʘ &a&l" + game.name + " &aʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Triple (3v3) - &e&lOpen &e㋡_/¯"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);


                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.tripleArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        }

                    }
                    GuiItem noArenaItem = new GuiItem(Utils.noGameItem, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&cGamei dar hale hazer vojod nadarad!"));
                        event.getWhoClicked().closeInventory();
                    });
                    for (int i = count; i < 16; i++) {
                        Utils.tripleArenaGui.setItem(i, noArenaItem);
                    }

                    Utils.tripleArenaGui.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 0, 20);
    }


    public void squadGuiRunnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BedwarsHub.getInstance(), new Runnable() {
            @Override
            public void run() {

                try {
                    int finalSquadCount = 0;
                    HashMap<String, Integer> serverCounts = new HashMap<>();
                    Map<Game, Integer> arenasWithPlayers = new LinkedHashMap<>();
                    for (Game game : Utils.squadArenasList) {
                        finalSquadCount += game.currentcount;
                        Utils.addCountToHashMap(serverCounts, game.server, game.currentcount);
                        if (!game.state.equalsIgnoreCase("ingame")) {
                            arenasWithPlayers.put(game, game.currentcount);
                        }
                    }
                    Utils.squadCount = finalSquadCount;
                    LinkedHashMap<Game, Integer> sortedMap = new LinkedHashMap<>();
                    arenasWithPlayers.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
                    //the new balancing formulight for GUIs
                    List<Game> startingGames = new ArrayList<>(sortedMap.keySet());
                    List<Game> zeroGames = new ArrayList<>(sortedMap.keySet());
                    zeroGames.removeIf(theArena -> theArena.currentcount > 0);
                    startingGames.removeIf(theArena -> theArena.currentcount == 0);
                    Collections.shuffle(zeroGames);
                    Comparator<Game> compareByServerCount = new Comparator<Game>() {
                        @Override
                        public int compare(Game arena1, Game arena2) {
                            int arena1servercount = serverCounts.get(arena1.server);
                            int arena2servercount = serverCounts.get(arena2.server);
                            return Integer.compare(arena1servercount, arena2servercount);
                        }
                    };
                    zeroGames.sort(compareByServerCount);
                    startingGames.addAll(zeroGames);

                    //soloGui
                    int count = 11;
                    for (Game game : startingGames) {
                        if (count == 16) {
                            continue;
                        }
                        if (game.state.equalsIgnoreCase("starting") && game.max == game.currentcount) {

                            ItemStack itemesh = Utils.fullGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&cʘ &c&l" + game.name + " &cʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Squad (4v4) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&cIn game por mibashad!"));
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.squadArenaGui.setItem(count, arenaItem);
                            count = count + 1;

                        } else if (game.state.equalsIgnoreCase("starting")) {
                            ItemStack itemesh = Utils.startingGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&6ʘ &6&l" + game.name + " &6ʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Squad (4v4) - &e&lStarting &e♥w♥"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);

                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.squadArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        } else {
                            ItemStack itemesh = Utils.openGameItem.clone();
                            ItemMeta meta = itemesh.getItemMeta();
                            meta.setDisplayName(Utils.colorify("&aʘ &a&l" + game.name + " &aʘ"));
                            //lore of item and ... continue later from here
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(Utils.colorify("&7Squad (4v4) - &e&lOpen &e㋡_/¯"));
                            lore.add("");
                            lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                            lore.add(Utils.colorify("&eServer: &6 " + game.server));
                            lore.add("");
                            lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                            meta.setLore(lore);
                            itemesh.setItemMeta(meta);


                            GuiItem arenaItem = new GuiItem(itemesh, event -> {
                                event.setCancelled(true);
                                event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                                Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                                event.getWhoClicked().closeInventory();
                            });
                            Utils.squadArenaGui.setItem(count, arenaItem);
                            count = count + 1;
                        }

                    }
                    GuiItem noArenaItem = new GuiItem(Utils.noGameItem, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&cGamei dar hale hazer vojod nadarad!"));
                        event.getWhoClicked().closeInventory();
                    });
                    for (int i = count; i < 16; i++) {
                        Utils.squadArenaGui.setItem(i, noArenaItem);
                    }

                    Utils.squadArenaGui.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 0, 20);
    }





}
