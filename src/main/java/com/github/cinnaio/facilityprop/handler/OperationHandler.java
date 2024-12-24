package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;
import java.util.HashMap;

public class OperationHandler {
    private final HashMap<String, HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>>> machineGUIs = FacilityProp.getInstance().getMachineGUIs();

    public void createMachineGUI(Location location, int amount, String str) {
        String world = location.getWorld().getName();

        HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>> worldMachines = machineGUIs.computeIfAbsent(world, k -> new HashMap<>());

        if (!worldMachines.containsKey(location)) {
            Inventory gui = Bukkit.createInventory(null, amount, "产物箱");
            worldMachines.put(location, new AbstractMap.SimpleEntry<>(gui, str));
        }
    }

    public void addItemToMachine(Location location, ItemStack item) {
        String worldName = location.getWorld().getName();

        HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>> worldMachines = machineGUIs.get(worldName);

        if (worldMachines != null) {
            AbstractMap.SimpleEntry<Inventory, String> entry = worldMachines.get(location);
            if (entry != null) {
                Inventory gui = entry.getKey();

                if (gui != null) {
                    gui.addItem(item);
                }
            }
        }
    }


    public Inventory getMachineInventory(String worldName, Location location) {
        HashMap<Location, AbstractMap.SimpleEntry<Inventory, String>> worldMachines = machineGUIs.get(worldName);

        if (worldMachines != null) {
            AbstractMap.SimpleEntry<Inventory, String> entry = worldMachines.get(location);
            return entry != null ? entry.getKey() : null;
        }
        return null;
    }

    public void changePlayerItem(Player p, ItemStack itemStack, int amount) {
        Inventory inv = p.getInventory();
        int itemAmount = itemStack.getAmount();

        if (itemAmount > 1) {
            itemStack.setAmount(itemAmount - amount);
        } else {
            inv.remove(itemStack);
        }
    }
}
