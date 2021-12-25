package me.deadlight.bedwarshub.Interactions;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.deadlight.bedwarshub.Objects.Game;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StaffGuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("client.admingui")) return false;
//            Utils.adminPanelGui.open(player);
            Utils.adminPanelGui = new PaginatedGui(6, Utils.colorify("&cAdmin Arenas Panel"));

            GuiItem nextPage = new GuiItem(ItemBuilder.from(Material.PAPER).setName(Utils.colorify("&aSafhe bad")).build(), event -> {
                event.setCancelled(true);
                Utils.adminPanelGui.next();

            });
            GuiItem previousPage = new GuiItem(ItemBuilder.from(Material.PAPER).setName(Utils.colorify("&cSafhe ghabl")).build(), event -> {
                event.setCancelled(true);
                Utils.adminPanelGui.previous();
            });
            ItemStack redPaneGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
            Utils.adminPanelGui.getFiller().fillBottom(new GuiItem(redPaneGlass, event -> {
                event.setCancelled(true);
            }));
            Utils.adminPanelGui.setItem(48, previousPage);
            Utils.adminPanelGui.setItem(50, nextPage);

            List<Game> allGames = new ArrayList<>();
            allGames.addAll(Utils.soloArenasList);
            allGames.addAll(Utils.doubleArenasList);
            allGames.addAll(Utils.tripleArenasList);
            allGames.addAll(Utils.squadArenasList);

            List<Game> alreadyInGame = new ArrayList<>(allGames);
            List<Game> startingButFull = new ArrayList<>(allGames);
            List<Game> starting = new ArrayList<>(allGames);
            List<Game> openButHasPlayers = new ArrayList<>(allGames);
            List<Game> open = new ArrayList<>(allGames);
            alreadyInGame.removeIf(theArena -> !theArena.state.equalsIgnoreCase("ingame"));
            startingButFull.removeIf(theArena -> !theArena.state.equalsIgnoreCase("starting") && theArena.currentcount < theArena.max);
            starting.removeIf(theArena -> !theArena.state.equalsIgnoreCase("starting"));
            starting.removeAll(startingButFull);
            openButHasPlayers.removeIf(theArena -> !theArena.state.equalsIgnoreCase("open") && !(theArena.currentcount > 0));
            open.removeIf(theArena -> !theArena.state.equalsIgnoreCase("open"));
            open.removeAll(openButHasPlayers);
            player.sendMessage(Utils.colorify("&5INGAME ARENAS: " + alreadyInGame.size()));
            player.sendMessage(Utils.colorify("&4FULL STARTING ARENAS: " + startingButFull.size()));
            player.sendMessage(Utils.colorify("&6STARTING ARENAS: " + starting.size()));
            player.sendMessage(Utils.colorify("&AOPEN WITH PLAYERS ARENAS: " + openButHasPlayers.size()));
            player.sendMessage(Utils.colorify("&5OPEN ARENAS: " + open.size()));
            player.sendMessage(Utils.colorify("&dShould be: " + (alreadyInGame.size() + startingButFull.size() + starting.size() + open.size() + openButHasPlayers.size())));
            alreadyInGame.addAll(startingButFull);
            alreadyInGame.addAll(starting);
            alreadyInGame.addAll(openButHasPlayers);
            alreadyInGame.addAll(open);
            player.sendMessage(Utils.colorify("&b&lALL GAMES COUNT: " + alreadyInGame.size()));

            for (Game game : alreadyInGame) {
                String gameType = "(" + game.gameType + "v" + game.gameType + ")";
                if (game.state.equalsIgnoreCase("ingame")) {
                    ItemStack itemesh = Utils.ingameItem.clone();
                    ItemMeta meta = itemesh.getItemMeta();

                    meta.setDisplayName(Utils.colorify("&5ʘ &5&l" + game.name + " &5ʘ"));
                    //lore of item and ... continue later from here
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(Utils.colorify("&7Type: " + gameType + " - &5&lIN-GAME &e♥w♥"));
                    lore.add("");
                    lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                    lore.add(Utils.colorify("&eServer: &6 " + game.server));
                    lore.add("");
                    lore.add(Utils.colorify("&cIn game dar hale anjam ast"));
                    meta.setLore(lore);
                    itemesh.setItemMeta(meta);

                    GuiItem arenaItem = new GuiItem(itemesh, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&cIn game dar hale anjam ast"));
                    });
                    Utils.adminPanelGui.addItem(arenaItem);
                } else if (game.state.equalsIgnoreCase("starting") && game.currentcount == game.max) {
                    ItemStack itemesh = Utils.fullGameItem.clone();
                    ItemMeta meta = itemesh.getItemMeta();
                    meta.setDisplayName(Utils.colorify("&cʘ &c&l" + game.name + " &cʘ"));
                    //lore of item and ... continue later from here
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(Utils.colorify("&7Type " + gameType + " - &e&lStarting &e♥w♥"));
                    lore.add("");
                    lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                    lore.add(Utils.colorify("&eServer: &6 " + game.server));
                    lore.add("");
                    lore.add(Utils.colorify("&cIn game por mibashad!"));
                    meta.setLore(lore);
                    itemesh.setItemMeta(meta);

                    GuiItem arenaItem = new GuiItem(itemesh, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&cIn game por mibashad!"));

                    });
                    Utils.adminPanelGui.addItem(arenaItem);
                } else if (game.state.equalsIgnoreCase("starting") && game.currentcount < game.max) {
                    ItemStack itemesh = Utils.startingGameItem.clone();
                    ItemMeta meta = itemesh.getItemMeta();
                    meta.setDisplayName(Utils.colorify("&6ʘ &6&l" + game.name + " &6ʘ"));
                    //lore of item and ... continue later from here
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(Utils.colorify("&7" + gameType +" - &e&lStarting &e♥w♥"));
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

                    });
                    Utils.adminPanelGui.addItem(arenaItem);
                } else if (game.state.equalsIgnoreCase("open") && game.currentcount > 0) {

                    ItemStack itemesh = Utils.openGameItem.clone();
                    ItemMeta meta = itemesh.getItemMeta();
                    meta.setDisplayName(Utils.colorify("&aʘ &a&l" + game.name + " &aʘ"));
                    //lore of item and ... continue later from here
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(Utils.colorify("&7 Type: " + gameType + " - &e&lOpen &e㋡_/¯"));
                    lore.add("");
                    lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                    lore.add(Utils.colorify("&eServer: &6 " + game.server));
                    lore.add("");
                    lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                    meta.setLore(lore);
                    itemesh.setItemMeta(meta);

                    //48 & 50
                    GuiItem arenaItem = new GuiItem(itemesh, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                        Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                    });
                    Utils.adminPanelGui.addItem(arenaItem);

                } else {
                    ItemStack itemesh = Utils.openGameItem.clone();
                    ItemMeta meta = itemesh.getItemMeta();
                    meta.setDisplayName(Utils.colorify("&aʘ &a&l" + game.name + " &aʘ"));
                    //lore of item and ... continue later from here
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(Utils.colorify("&7 Type: " + gameType + " - &e&lOpen &e㋡_/¯"));
                    lore.add("");
                    lore.add(Utils.colorify("&ePlayers: &6" + game.currentcount + "&e/&6" + game.max));
                    lore.add(Utils.colorify("&eServer: &6 " + game.server));
                    lore.add("");
                    lore.add(Utils.colorify("&aBaraye bazi kardan click konid."));
                    meta.setLore(lore);
                    itemesh.setItemMeta(meta);

                    //48 & 50
                    GuiItem arenaItem = new GuiItem(itemesh, event -> {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Utils.colorify("&aDar hale etesal..."));
                        Utils.sendPlayerJoinRequest((Player) event.getWhoClicked(), game.arenaid, game.server);
                    });
                    Utils.adminPanelGui.addItem(arenaItem);

                }


            }
            Utils.adminPanelGui.open(player);

        }

        return false;
    }
}
