package com.github.cinnaio.facilityprop;

import com.github.cinnaio.facilityprop.handler.CommandHandler;
import com.github.cinnaio.facilityprop.handler.ConfigurationHandler;
import com.github.cinnaio.facilityprop.handler.FunctionHandler;
import com.github.cinnaio.facilityprop.listener.InteractEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class FacilityProp extends JavaPlugin {
    private static JavaPlugin instance;

    private static FunctionHandler functionHandler;

    private static ConfigurationHandler configInstance;

    private static Economy economy = null;

    public void onEnable() {
        if (!this.setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        instance = this;

        configInstance = new ConfigurationHandler();

        functionHandler = new FunctionHandler();

        Bukkit.getPluginCommand("facilityprop").setExecutor(new CommandHandler());

        Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {

            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);

            if (rsp == null) {

                return false;
            } else {
                economy = (Economy)rsp.getProvider();

                return economy != null;
            }
        }
    }

    public void onDisable() {
        instance = null;
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static FunctionHandler getFunctionHandler() {
        return functionHandler;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static ConfigurationHandler getConfigInstance() {
        return configInstance;
    }
}