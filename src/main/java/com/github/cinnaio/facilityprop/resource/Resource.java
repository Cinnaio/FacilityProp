package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Resource {
    private static FileConfiguration file = FacilityProp.instance.getConfig();

    public static String facility = FacilityProp.facility;

    public static String getHexCode(HexCodeUtils.HexCode style) {
        if (style == HexCodeUtils.HexCode.ACTIONBAR)
            return file.getString("main.hexcode_actionbar");
        else if (style == HexCodeUtils.HexCode.BOSSBAR)
            return file.getString("main.hexcode_bossbar");
        else
            return file.getString("main.hexcode_normal");
    }

    public static String getNameSpace(String n) {
        return file.getString(facility + n + ".namespace");
    }

    public static List<List<Integer>> getAcquireItems(String n) {
        List<List<Integer>> outerList = new ArrayList<>();

        if (file.isConfigurationSection(facility + n + ".acquire_items")) {
            ConfigurationSection outerPart = file.getConfigurationSection(facility + n + ".acquire_items");

            for (String outerKey : outerPart.getKeys(false)) {
                Boolean skipInnerList = true;

                for (String innerKey : outerPart.getKeys(true)) {
                    List<Integer> innerList = new ArrayList<>();

                    if (innerKey.equals(outerKey + ".custom_model_data")) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    } else if (innerKey.equals(outerKey + ".amount")) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    }

                    if (!skipInnerList) {
                        outerList.add(innerList);
                    }
                }
            }
        }
        outerList.removeIf(List::isEmpty);

        return outerList;
    }

    public static Integer getCustomModelData(String n) {
        return file.getInt(facility + n + ".custom_model_data");
    }

    public static Integer getWaittingTime(String n) {
        return file.getInt(facility + n + ".waitting_times") * 20;
    }

    public static String getWeather(String n) {
        if (file.isConfigurationSection(facility + n + ".condition")) {
            return file.getString(facility + n + ".condition.weather");
        }
        else
            return "null";
    }
}
