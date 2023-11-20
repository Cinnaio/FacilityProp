package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class ConfigurationHandler {
    private JavaPlugin instance = FacilityProp.getInstance();

    private FileConfiguration file;

    private final HashMap<String, Object> configMap;

    private File i18;

    private FileConfiguration lang;

    private i18Handler i18h;

    private String prefix;

    public ConfigurationHandler() {
        file = instance.getConfig();

        configMap = new HashMap<>();

        i18 = new File(instance.getDataFolder(), "zh_cn.yml");

        instance.saveDefaultConfig();

        if (!i18.exists()) {
            instance.saveResource("zh_cn.yml", false);
        }

        lang = YamlConfiguration.loadConfiguration(i18);

        i18h = new i18Handler(lang);

        initConfigMap();

        prefix = configMap.get("basic.prefix") + " &#FBEDF6";
    }

    public void reload() {
        instance.reloadConfig();

        file = instance.getConfig();

//        暂时放弃对其他文件的重载
//        i18 = new File(instance.getDataFolder(), "zh_cn.yml");
//
//        lang = YamlConfiguration.loadConfiguration(i18);
//
//        i18h = new i18Handler(lang);

        configMap.clear();
        initConfigMap();
    }

    private void initConfigMap() {
        ConfigurationSection basic = this.file.getConfigurationSection("basic");

        if (basic != null) {
            for (String elementNode : basic.getKeys(false)) {
                Object value = basic.get(elementNode);

                String key = basic.getName() + "." + elementNode;

                configMap.put(key, value);
            }
        }

        ConfigurationSection facilities = this.file.getConfigurationSection("facilities");

        if (facilities != null) {
            for (String elementNode : facilities.getKeys(false)) {
                ConfigurationSection elementNodee = facilities.getConfigurationSection(elementNode);

                if (elementNodee != null) {
                    for (String innerKey : elementNodee.getKeys(true)) {
                        String tempKey = facilities.getName() + "." + elementNode + "." + innerKey;

                        configMap.put(tempKey, elementNodee.get(innerKey));
                    }
                }
            }
        }
    }

    public FileConfiguration getFile() {
        return file;
    }

    public i18Handler getI18h() {
        return i18h;
    }

    public HashMap<String, Object> getConfigMap() {
        return configMap;
    }

    public String getPrefix() {
        return prefix;
    }
}
