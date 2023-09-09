package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Resource {
    private static FileConfiguration file = FacilityProp.instance.getConfig();

    public static String getHexCode(HexCodeUtils.HexCode style) {
        if (style == HexCodeUtils.HexCode.ACTIONBAR)
            return file.getString("main.hexcode_actionbar");
        else if (style == HexCodeUtils.HexCode.BOSSBAR)
            return file.getString("main.hexcode_bossbar");
        else
            return file.getString("main.hexcode_normal");
    }

    public static String getNameSpace(String n) {
        return file.getString("facility." + n + ".namespace");
    }

    public static List<List<Integer>> getNeedItems(String n) {
        List<List<Integer>> outerList = new ArrayList<>();

        if (file.isConfigurationSection("facility." + n + ".needitems")) {
            ConfigurationSection outerPart = file.getConfigurationSection("facility." + n + ".needitems");

            for (String string : outerPart.getKeys(false)) {

                for (String innerKey : outerPart.getKeys(true)) {
                    List<Integer> innerList = new ArrayList<>();

                    if (innerKey.equals(string + ".custom_model_data")) {
                        innerList.add(outerPart.getInt(innerKey));
                    } else if (innerKey.equals(string + ".amount")) {
                        innerList.add(outerPart.getInt(innerKey));
                    }
                    outerList.add(innerList);
                }
            }
        }
        return outerList;
    }

    public static Integer getCustomModelData(String n) {
        return file.getInt("facility." + n + ".custom_model_data");
    }

    public static Integer getWaittingTime(String n) {
        return file.getInt("facility." + n + ".waitticks") * 20;
    }

    public static String getWeather(String n) {
        if (file.isConfigurationSection("facility." + n + ".condition.weather"))
            return file.getString("facility." + n + ".condition.weather");
        else
            return "null";
    }
}
