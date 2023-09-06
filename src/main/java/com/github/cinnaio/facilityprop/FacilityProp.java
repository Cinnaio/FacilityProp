package com.github.cinnaio.facilityprop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class FacilityProp extends JavaPlugin {
    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        this.saveResource("zh_cn.yml", true);

        Bukkit.getPluginManager().registerEvents(new FacilityHandler(), this);
    }

    @Override
    public void onDisable() {

    }
}
