package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Resource {
    private static FileConfiguration file = FacilityProp.instance.getConfig();

    public static String getNameSpace(String n) {
        return file.getString("facility." + n + ".namespace");
    }

    public static List<Integer> getNeedItems(String n) {
        return file.getIntegerList("facility." + n + ".needitems");
    }

    public static Integer getCustomModelData(String n) {
        return file.getInt("facility." + n + ".custom_model_data");
    }

    public static Integer getTime(String n) {
        return file.getInt("facility." + n + ".waitticks") * 20;
    }
}
