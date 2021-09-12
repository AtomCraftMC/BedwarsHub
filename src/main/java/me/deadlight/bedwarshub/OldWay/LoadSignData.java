package me.deadlight.bedwarshub.OldWay;

import me.deadlight.bedwarshub.BedwarsHub;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class LoadSignData {


    public void loadData() {
        BedwarsHub main = BedwarsHub.getInstance();
        Configuration mainconfigroute = main.getConfig();
        ConfigurationSection configSec = main.getConfig().getConfigurationSection("data");
        if (configSec != null) {
            configSec.getKeys(false).forEach(key -> {
                String thelocation = mainconfigroute.getString("data." + key);
                Location signLocation = Utils.convertStringToLoc(thelocation);
                Utils.map.put(Integer.valueOf(key), signLocation);
            });
            main.logConsole(Utils.prefix + " &aSign Data successfully loaded");
            setSigns();
        }

    }

    public void setSigns() {
        BedwarsHub main = BedwarsHub.getInstance();
        Utils.map.values().forEach(location -> {
            if (location.getBlock().getState() instanceof Sign) {

                Sign sign = (Sign)location.getBlock().getState();
                sign.setLine(1, Utils.colorify("&b&lLoading..."));
                sign.update();

            } else {
                main.logConsole(Utils.prefix + " &cError while reading one of the signs with location : " + Utils.convertLocToString(location));
            }
        });
    }


}
