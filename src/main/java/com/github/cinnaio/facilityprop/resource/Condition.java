package com.github.cinnaio.facilityprop.resource;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class Condition {
    public static String getWeather(Player p) {
        World w = p.getWorld();
        String sun = "sun";
        String rain = "rain";
        String snow = "snow";
        String storm = "storm";

        String tmp = w.isClearWeather() ? sun : rain;

        if (tmp == rain) {
            if (w.hasStorm()) {
                return w.hasStorm() ? rain : snow;
            } else {
                return storm;
            }
        } else
            return sun;
    }
}
