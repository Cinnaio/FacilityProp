package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Language {
    private static YamlConfiguration file = YamlConfiguration.loadConfiguration(new File(FacilityProp.instance.getDataFolder().getPath() + File.separator + "zh_cn.yml"));

    public static String successful_teapan = file.getString("successful_teapan");

    public static String error_weather = file.getString("error_weather");
    public static String error_amount = file.getString("error_amount");
    public static String error_permission = file.getString("error_permission");

    public static String crafting_teapan = file.getString("crafting_teapan");
    public static String crafting_wait_time = file.getString("crafting_wait_time");
}
