package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Language {
    private static YamlConfiguration file = YamlConfiguration.loadConfiguration(new File(FacilityProp.instance.getDataFolder().getPath() + File.separator + "zh_cn.yml"));

    public static String successful_teapan = file.getString("successful_teapan");
}
