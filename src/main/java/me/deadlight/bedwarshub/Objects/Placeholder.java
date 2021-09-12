package me.deadlight.bedwarshub.Objects;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.deadlight.bedwarshub.BedwarsHub;
import me.deadlight.bedwarshub.Utils;
import org.bukkit.OfflinePlayer;

public class Placeholder extends PlaceholderExpansion {
    private final BedwarsHub plugin;
    public Placeholder(BedwarsHub plugin) {
        this.plugin = plugin;
    }


    @Override
    public String getAuthor() {
        return "Dead_Light";
    }

    @Override
    public String getIdentifier() {
        return "bedwarshub";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("solo")){
            return String.valueOf(Utils.soloCount);
        }
        if(params.equalsIgnoreCase("double")){
            return String.valueOf(Utils.doubleCount);
        }
        if(params.equalsIgnoreCase("triple")){
            return String.valueOf(Utils.tripleCount);
        }
        if(params.equalsIgnoreCase("squad")){
            return String.valueOf(Utils.squadCount);
        }



        return null; // Placeholder is unknown by the Expansion
    }


}
