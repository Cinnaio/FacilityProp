package com.github.cinnaio.facilityprop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigHandler {
    public static FileConfiguration file = FacilityProp.instance.getConfig();
    public static File langFile = new File(FacilityProp.instance.getDataFolder(), "zh_cn.yml");
    public static FileConfiguration langYaml = YamlConfiguration.loadConfiguration(langFile);

    public static void relaodConfig() {
        FacilityProp.instance.reloadConfig();
        file = FacilityProp.instance.getConfig();
    }

}
