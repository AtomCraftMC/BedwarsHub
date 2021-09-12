package me.deadlight.bedwarshub;

import dev.triumphteam.gui.guis.Gui;
import me.deadlight.bedwarshub.Objects.Game;
import me.deadlight.bedwarshub.Objects.TimerObject;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Bed;
import org.json.simple.JSONObject;
import redis.clients.jedis.Jedis;
import sv.file14.procosmetics.cosmetic.CosmeticCategoryType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static List<String> outGoingPlayers = new ArrayList<>();
    public static String colorify(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }

    public static List<Game> soloArenasList = new ArrayList<>();
    public static List<Game> doubleArenasList = new ArrayList<>();
    public static List<Game> tripleArenasList = new ArrayList<>();
    public static List<Game> squadArenasList = new ArrayList<>();
    public static int soloCount = 0;
    public static int doubleCount = 0;
    public static int tripleCount = 0;
    public static int squadCount = 0;

    public static String prefix = "&b[&cBedWarsHub&b]&r ";

    public static HashMap<Integer, Location> map = new HashMap<>();


    public static String convertLocToString(Location loci) {

        int x = loci.getBlockX();
        int y = loci.getBlockY();
        int z = loci.getBlockZ();
        String world = loci.getWorld().getName();
        float pitch = loci.getPitch();
        float yaw = loci.getYaw();
        //int x, int y, int z, String world, float pitch, float yaw

        return x + ":" + y + ":" + z + ":" + world + ":" + pitch + ":" + yaw;
    }

    public static Location convertStringToLoc(String stringloc) {

        //4 --> pitch
        //5 --> yaw
        List<String> splited = Arrays.asList(stringloc.split(":"));

        return new Location(Bukkit.getWorld(splited.get(3)), Integer.parseInt(splited.get(0)) + 0.5, Integer.parseInt(splited.get(1)), Integer.parseInt(splited.get(2)) + 0.5, Float.parseFloat(splited.get(5)), Float.parseFloat(splited.get(4)));
    }


    public static Gui soloArenaGui;

    public static Gui doubleArenaGui;

    public static Gui tripleArenaGui;

    public static Gui squadArenaGui;

    public static ItemStack fullGameItem;

    public static ItemStack startingGameItem;

    public static ItemStack openGameItem;

    public static ItemStack noGameItem;

    public static void InitializeItems() {

        fullGameItem = new ItemStack(Material.FIREWORK_CHARGE, 1);
        ItemMeta fullGameItemMeta = fullGameItem.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) fullGameItemMeta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.RED).build();
        metaFw.setEffect(aa);
        fullGameItem.setItemMeta(metaFw);
        //
        startingGameItem = new ItemStack(Material.FIREWORK_CHARGE, 1);
        ItemMeta startingGameItemMeta = startingGameItem.getItemMeta();
        FireworkEffectMeta metaFw2 = (FireworkEffectMeta) startingGameItemMeta;
        FireworkEffect aa2 = FireworkEffect.builder().withColor(Color.YELLOW).build();
        metaFw2.setEffect(aa2);
        startingGameItem.setItemMeta(metaFw2);
        //
        openGameItem = new ItemStack(Material.FIREWORK_CHARGE, 1);
        ItemMeta openGameItemMeta = openGameItem.getItemMeta();
        FireworkEffectMeta metaFw3 = (FireworkEffectMeta) openGameItemMeta;
        FireworkEffect aa3 = FireworkEffect.builder().withColor(Color.LIME).build();
        metaFw3.setEffect(aa3);
        openGameItem.setItemMeta(metaFw3);
        //barrier for not available
        noGameItem = new ItemStack(Material.BARRIER, 1);
        ItemMeta noGameMeta = noGameItem.getItemMeta();
        noGameMeta.setDisplayName(Utils.colorify("&cNo Game available (ノಥ,_｣ಥ)ノ彡┻━┻"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.colorify("&7Gamei dar hale hazer vojod nadarad... "));
        lore.add(Utils.colorify("&7Kami sabr konid ^_^"));
        noGameMeta.setLore(lore);
        noGameItem.setItemMeta(noGameMeta);

    }

    public static void InitializeGuis() {
        //solo
        Gui gui = new Gui(3, Utils.colorify("&a&lSolo Games &c(◠‿◠✿)"));

        gui.setDefaultClickAction(event ->
                {
                    event.setCancelled(true);
                }
        );
        Utils.soloArenaGui = gui;
        Gui gui2 = new Gui(3, Utils.colorify("&a&lDuo Games &c(づ｡◕‿‿◕｡)づ"));
        gui2.setDefaultClickAction(event ->
                {
                    event.setCancelled(true);
                }
        );
        Utils.doubleArenaGui = gui2;

        Gui gui3 = new Gui(3, Utils.colorify("&a&lTriple Games &c≧◡≦"));
        gui3.setDefaultClickAction(event ->
                {
                    event.setCancelled(true);
                }
        );
        Utils.tripleArenaGui = gui3;
        Gui gui4 = new Gui(3, Utils.colorify("&a&lSquad Games &c◎[▪‿▪]◎"));
        gui4.setDefaultClickAction(event ->
                {
                    event.setCancelled(true);
                }
        );
        Utils.squadArenaGui = gui4;

    }

    public static void sendPlayerJoinRequest(Player player, int gameid, String server) {

        if (Utils.outGoingPlayers.contains(player.getName())) {
            player.sendMessage(Utils.colorify("&cShoma dar hale enteghal be yek match digar hastid! Lotfan sabr konid."));
            return;
        }
        JSONObject jsonObject = new JSONObject();
        //jsonObject.put("type", "comingplayer");
        jsonObject.put("game", String.valueOf(gameid));
        //jsonObject.put("player", player.getName());
        String arrowName = "null";
        String deathName = "null";
        try{
            arrowName = sv.file14.procosmetics.api.API.getUser(player).getCosmetic(CosmeticCategoryType.ARROW_EFFECTS).getCosmeticType().getVariableName();
        } catch (Exception ex) {

        }
        try {
            deathName = sv.file14.procosmetics.api.API.getUser(player).getCosmetic(CosmeticCategoryType.DEATH_EFFECTS).getCosmeticType().getVariableName();
        } catch (Exception ex) {

        }

        Utils.outGoingPlayers.add(player.getName());
        TimerObject timerObject = new TimerObject(player);
        jsonObject.put("arrow", arrowName);
        jsonObject.put("death", deathName);
        Jedis j = null;
        try {
            j = BedwarsHub.pool.getResource();
            j.auth("piazcraftmc");
            j.set("ip-" + player.getName(), jsonObject.toJSONString());
            j.expire("ip-" + player.getName(), 10);
        } finally {
            if (j != null) {
                j.close();
            }
        }

        //BedwarsHub.getInstance().socketServer.sendMessage(jsonObject.toJSONString(), server);
        sendPlayerToServer(player, server);

    }

    public static void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(BedwarsHub.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            player.sendMessage(ChatColor.RED+"Error when trying to connect to "+server);
        }
    }




}
