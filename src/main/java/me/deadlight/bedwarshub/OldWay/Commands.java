package me.deadlight.bedwarshub.OldWay;

import me.deadlight.bedwarshub.BedwarsHub;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sv.file14.procosmetics.cosmetic.AbstractCosmetic;
import sv.file14.procosmetics.cosmetic.CosmeticCategoryType;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Commands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {


            Player player = (Player) sender;
            //sv.file14.procosmetics.api.API.getUser(player).setCosmetic(CosmeticCategoryType.ARROW_EFFECTS);
            if (args.length >= 2 & lookingatsing(player.getTargetBlock((Set<Material>)null, 5).getLocation())) {

                BedwarsHub main = BedwarsHub.getInstance();

                if (args[0].equalsIgnoreCase("add")) {

                    if (isInData(args[1])) {
                        player.sendMessage(Utils.colorify("&cAlready registered arena."));
                    } else {

                        main.getConfig().set("data." + args[1], Utils.convertLocToString(player.getTargetBlock((Set<Material>)null, 5).getLocation()));
                        main.saveConfig();
                        Sign sign = (Sign) player.getTargetBlock((Set<Material>)null, 5).getLocation().getBlock().getState();
                        sign.setLine(1, Utils.colorify("&e&lLoading..."));
                        sign.update();
                        //do other stuff



                        player.sendMessage(Utils.colorify("&aadded new sign for this arena."));

                    }



                } else if (args[0].equalsIgnoreCase("remove")){
                    if (isInData(args[1])) {

                        main.getConfig().set("data." + args[1], null);
                        main.saveConfig();
                        player.getTargetBlock((Set<Material>)null, 5).getLocation().getBlock().breakNaturally();
//                        Sign sign = (Sign) player.getTargetBlock((Set<Material>)null, 5).getState();
//                        sign.setLine(0, "");
//                        sign.setLine(1, "");
//                        sign.setLine(2, "");
//                        sign.setLine(3, "");
                    } else {
                        player.sendMessage(Utils.colorify("&cNot registered before."));
                    }


                } else {
                    player.sendMessage(Utils.colorify("&cSomething is wrong... 1"));
                }

            } else {
                player.sendMessage(Utils.colorify("&cSomething is wrong... 2"));
            }



        }


        return false;
    }

    public boolean lookingatsing(Location loc) {

        if (loc == null) {
            return false;
        }

        if (loc.getBlock().getType() == Material.WALL_SIGN) {
            return true;
        } else {
            return false;
        }


    }
    public boolean isInData(String num) {
        AtomicBoolean result = new AtomicBoolean(false);
        if (BedwarsHub.getInstance().getConfig().getConfigurationSection("data") == null) {
            return false;
        }
        BedwarsHub.getInstance().getConfig().getConfigurationSection("data").getKeys(false).forEach(key -> {
            if (key.equalsIgnoreCase(num)) {
                result.set(true);
            }
        });
        return result.get();
    }


}
