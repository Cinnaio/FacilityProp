package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StructionHandler {
    private final FileConfiguration machine = FacilityProp.getInstance().getMachineConfig();

    private ConfigurationSection facility = machine.getConfigurationSection("facilities");

    public boolean isMachineId(String blockId) {
        for (String str: facility.getKeys(false)) {
            if (machine.get("facilities." + str + ".id").equals(blockId)) {
                return true;
            }
        }

        return false;
    }

    public Object getTargetMachineNode(String blockId) {
        for (String str: facility.getKeys(false)) {
            if (machine.get("facilities." + str + ".id").equals(blockId)) {
                return str;
            }
        }

        return null;
    }

    public boolean isExistRecipeItem(String str, CustomBlockInteractEvent e, ItemStack handItemStack) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        if (machine.get("facilities." + str + ".recipes") != null) {
            for (String strtmp: nodeRecipe.getKeys(false)) {
                ItemMeta itemMeta = handItemStack.getItemMeta();

                if (itemMeta.displayName() == null) {
                    Material material = e.getItem().getType();

                    if (!nodeRecipe.get(strtmp + ".id").toString().equalsIgnoreCase(material.toString())) {
                        continue;
                    }

                    return true;
                }

                String strItemId = ItemsAdder.getCustomItemName(e.getItem());

                if (nodeRecipe.get(strtmp + ".id").equals(strItemId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Integer getSpecialRecipeIndex(String str, CustomBlockInteractEvent e, ItemStack handItemStack) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        if (machine.get("facilities." + str + ".recipes") != null) {
                for (String strtmp: nodeRecipe.getKeys(false)) {
                ItemMeta itemMeta = handItemStack.getItemMeta();

                if (itemMeta.displayName() == null) {
                    Material material = e.getItem().getType();

                    if (!nodeRecipe.get(strtmp + ".id").toString().equalsIgnoreCase(material.toString())) {
                        continue;
                    }

                    return Integer.parseInt(strtmp);
                }

                String strItemId = ItemsAdder.getCustomItemName(e.getItem());

                if (nodeRecipe.get(strtmp + ".id").equals(strItemId)) {
                    return Integer.parseInt(strtmp);
                }
            }
        }

        return null;
    }

    public void saveMachineData(HashMap<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> machineGUIs, File saveDataFile, YamlConfiguration saveDataConfig) {
        for (Map.Entry<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> worldEntry : machineGUIs.entrySet()) {
            String worldName = worldEntry.getKey();
            HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>> machines = worldEntry.getValue();

            for (Map.Entry<Location, AbstractMap.SimpleEntry<Inventory, String>> entry : machines.entrySet()) {
                Location location = entry.getKey();
                AbstractMap.SimpleEntry<Inventory, String> entryValue = entry.getValue();
                Inventory inventory = entryValue.getKey();
                String machineName = entryValue.getValue();

                String locKey = worldName + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();

                List<ItemStack> nonNullItems = new ArrayList<>();
                for (ItemStack item : inventory.getContents()) {
                    if (item != null) {
                        nonNullItems.add(item);
                    }
                }

                saveDataConfig.set(locKey + ".items", nonNullItems);
                saveDataConfig.set(locKey + ".name", machineName);
            }
        }

        try {
            saveDataConfig.save(saveDataFile);
            FacilityProp.getInstance().getLogger().info("机器数据已成功保存！");
        } catch (IOException e) {
            e.printStackTrace();
            FacilityProp.getInstance().getLogger().severe("无法保存机器数据！");
        }
    }

    public void loadMachineData(HashMap<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> machineGUIs, File saveDataFile, YamlConfiguration saveDataConfig) {
        if (saveDataConfig == null) {
            FacilityProp.getInstance().getLogger().severe("无法加载机器数据：saveDataConfig 未正确初始化！");
            return;
        }

        for (String key : saveDataConfig.getKeys(false)) {
            try {
                String[] parts = key.split(",");
                String worldName = parts[0];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int z = Integer.parseInt(parts[3]);

                Location location = new Location(Bukkit.getWorld(worldName), x, y, z);

                if (location.getWorld() == null) {
                    FacilityProp.getInstance().getLogger().warning("无法加载机器数据：世界 " + worldName + " 不存在！");
                    continue;
                }

                List<ItemStack> itemList = (List<ItemStack>) saveDataConfig.getList(key + ".items");
                if (itemList == null) itemList = List.of();
                ItemStack[] items = itemList.toArray(new ItemStack[0]);

                String machineName = saveDataConfig.getString(key + ".name", "未知");
                int inv_amount = facility.getInt(machineName + ".gui.inv_amount");

                Inventory gui = Bukkit.createInventory(null, inv_amount, "产物箱");
                gui.setContents(items);

                // 使用 SimpleEntry 保存 Inventory 和机器名称
                machineGUIs.computeIfAbsent(worldName, k -> new HashMap<>()).put(location, new AbstractMap.SimpleEntry<>(gui, machineName));
            } catch (Exception e) {
                FacilityProp.getInstance().getLogger().severe("加载机器数据时出错：" + e.getMessage());
                e.printStackTrace();
            }
        }

        FacilityProp.getInstance().getLogger().info("机器数据已成功加载！");
    }

    public int getInvAmount(String str) {
        if (machine.get("facilities." + str + ".gui.inv_amount") != null)
            return (int) machine.get("facilities." + str + ".gui.inv_amount");

        return 0;
    }

    public boolean getIsCreateGUI(String str) {
        if (machine.get("facilities." + str + ".gui.create_gui") != null)
            return (boolean) machine.get("facilities." + str + ".gui.create_gui");

        return false;
    }

    public int getRecipeSpecialAmount(String str, int index) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        return nodeRecipe.getInt(index + ".amount");
    }

    public Integer getRecipeSpecialDelay(String str, int index) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        if (nodeRecipe.get(index + ".delay") != null) return nodeRecipe.getInt(index + ".delay");

        return null;
    }

    public Integer getRecipeSpecialXp(String str, int index) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        if (nodeRecipe.get(index + ".xp") != null) return nodeRecipe.getInt(index + ".xp");

        return null;
    }

    public String getRecipeFinalItem(String str, int index) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        return (String) nodeRecipe.get(index + ".out.id");
    }

    public Integer getRecipeFinalAmount(String str, int index) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        if (nodeRecipe.get(index + ".out.amount") != null) return nodeRecipe.getInt(index + ".out.amount");

        return null;
    }

    public Integer getRecipeFinalXp(String str, int index) {
        ConfigurationSection nodeRecipe = facility.getConfigurationSection(str + ".recipes");

        if (nodeRecipe.get(index + ".out.xp") != null) return nodeRecipe.getInt(index + ".out.xp");

        return null;
    }

    public void dropInventory(HashMap<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> machineGUIs,
                              String worldName,
                              Location location) {
        if (!machineGUIs.containsKey(worldName)) {
            return;
        }

        HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>> worldData = machineGUIs.get(worldName);

        if (!worldData.containsKey(location)) {
            return;
        }

        Inventory inventory = worldData.get(location).getKey();

        if (inventory != null) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                return;
            }

            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() != org.bukkit.Material.AIR) {
                    world.dropItem(location, item);
                }
            }

            inventory.clear();
        }
    }
}
