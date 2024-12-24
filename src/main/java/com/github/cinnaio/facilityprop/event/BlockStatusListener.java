package com.github.cinnaio.facilityprop.event;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.handler.StructionHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;

public class BlockStatusListener implements Listener {
    private final File saveDataFile = FacilityProp.getInstance().getSaveDataFile();

    private final YamlConfiguration saveDataConfig = FacilityProp.getInstance().getSaveDataConfig();

    private final StructionHandler strc = FacilityProp.getInstance().getStructionHandler();

    private final HashMap<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> machineGUIs = FacilityProp.getInstance().getMachineGUIs();

    @EventHandler
    public void deleteMachineData(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();
        Location loc = b.getLocation();

        String worldName = b.getWorld().getName();

        Bukkit.getScheduler().runTaskLater(FacilityProp.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (b.getType() == Material.AIR) {
                    if (machineGUIs.containsKey(worldName)) {
                        HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>> worldMachines = machineGUIs.get(worldName);

                        if (worldMachines.containsKey(loc)) {
                            worldMachines.remove(loc);

                            if (saveDataConfig != null && saveDataFile != null) {
                                String locKey = worldName + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
                                saveDataConfig.set(locKey, null);

                                try {
                                    saveDataConfig.save(saveDataFile);
                                    p.sendMessage(ChatColor.GREEN + "机器数据已成功删除！");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    p.sendMessage(ChatColor.RED + "无法保存删除后的机器数据！");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "保存文件未初始化，无法删除机器数据！");
                            }
                        }
                    }
                }
            }
        }, 20L);
    }

    @EventHandler
    public void onMachineBreak(BlockBreakEvent e) {
        Location loc = e.getBlock().getLocation();
        String world = e.getBlock().getWorld().getName();

        Bukkit.getScheduler().runTaskLater(FacilityProp.getInstance(), () -> {
            World bukkitWorld = Bukkit.getWorld(world);
            if (bukkitWorld != null) {
                if (bukkitWorld.getBlockAt(loc).getType() == org.bukkit.Material.AIR)
                    strc.dropInventory(machineGUIs, world, loc);
            }
        }, 20L);
    }
}
