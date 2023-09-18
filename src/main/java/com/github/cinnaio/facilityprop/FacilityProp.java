package com.github.cinnaio.facilityprop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FacilityProp extends JavaPlugin {
    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.saveResource("zh_cn.yml", false);

        Bukkit.getPluginManager().registerEvents(new FacilityHandler(), this);
        Bukkit.getPluginCommand("facilityprop").setExecutor(new CommandHandler());
    }

    @Override
    public void onDisable() {
    }
}
