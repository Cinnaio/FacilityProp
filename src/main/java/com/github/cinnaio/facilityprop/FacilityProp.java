package com.github.cinnaio.facilityprop;

import com.github.cinnaio.facilityprop.event.BlockStatusListener;
import com.github.cinnaio.facilityprop.event.PlayerInteractionListener;
import com.github.cinnaio.facilityprop.handler.CommandHandler;
import com.github.cinnaio.facilityprop.handler.OperationHandler;
import com.github.cinnaio.facilityprop.handler.StructionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;

public final class FacilityProp extends JavaPlugin {
    private static FacilityProp instance;

    private File langFile;
    private File machineFile;
    private File saveDataFile;

    private YamlConfiguration langConfig;
    private YamlConfiguration machineConfig;
    private YamlConfiguration saveDataConfig;

    private StructionHandler structionHandler;
    private OperationHandler operationHandler;

    private final HashMap<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> machineGUIs = new HashMap<>();

    public final String CYAN = "\u001B[36m";
    public final String RESET = "\u001B[0m";

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        initializeFiles();

        structionHandler = new StructionHandler();
        operationHandler = new OperationHandler();

        structionHandler.loadMachineData(machineGUIs, saveDataFile, saveDataConfig);

        Bukkit.getPluginManager().registerEvents(new PlayerInteractionListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockStatusListener(), this);

        Bukkit.getPluginCommand("facilityprop").setExecutor(new CommandHandler());
        Bukkit.getPluginCommand("facilityprop").setTabCompleter(new CommandHandler());

        System.out.println(
                CYAN +
                        "\n" +
                        "  _____ _    ____ ___ _     ___ _______   ______  ____   ___  ____  \n" +
                        " |  ___/ \\  / ___|_ _| |   |_ _|_   _\\ \\ / /  _ \\|  _ \\ / _ \\|  _ \\ \n" +
                        " | |_ / _ \\| |    | || |    | |  | |  \\ V /| |_) | |_) | | | | |_) |\n" +
                        " |  _/ ___ \\ |___ | || |___ | |  | |   | | |  __/|  _ <| |_| |  __/ \n" +
                        " |_|/_/   \\_\\____|___|_____|___| |_|   |_| |_|   |_| \\_\\\\___/|_|    \n" +
                        RESET
        );
    }

    @Override
    public void onDisable() {
        structionHandler.saveMachineData(machineGUIs, saveDataFile, saveDataConfig);

        Bukkit.getScheduler().cancelTasks(this);

        instance = null;
    }

    public void reload() {
        reloadConfig();

        langConfig = YamlConfiguration.loadConfiguration(langFile);
        machineConfig = YamlConfiguration.loadConfiguration(machineFile);

        getLogger().info("配置文件已重新加载！");
    }

    private void initializeFiles() {
        langFile = new File(getDataFolder(), "lang/zh_cn.yml");
        if (!langFile.exists()) {
            saveResource("lang/zh_cn.yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        machineFile = new File(getDataFolder(), "machine.yml");
        if (!machineFile.exists()) {
            saveResource("machine.yml", false);
        }
        machineConfig = YamlConfiguration.loadConfiguration(machineFile);

        saveDataFile = new File(getDataFolder(), "savedata.yml");
        if (!saveDataFile.exists()) {
            try {
                saveDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().severe("无法创建 savedata.yml 文件！");
            }
        }
        saveDataConfig = YamlConfiguration.loadConfiguration(saveDataFile);
    }

    public static FacilityProp getInstance() {
        return instance;
    }

    public File getSaveDataFile() {
        return saveDataFile;
    }

    public YamlConfiguration getMachineConfig() {
        return machineConfig;
    }

    public YamlConfiguration getLangConfig() {
        return langConfig;
    }

    public YamlConfiguration getSaveDataConfig() {
        return saveDataConfig;
    }

    public StructionHandler getStructionHandler() {
        return structionHandler;
    }

    public OperationHandler getOperationHandler() {
        return operationHandler;
    }

    public HashMap<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> getMachineGUIs() {
        return machineGUIs;
    }
}
