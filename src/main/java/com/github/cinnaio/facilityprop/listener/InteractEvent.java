package com.github.cinnaio.facilityprop.listener;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.handler.FunctionHandler;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;

public class InteractEvent implements Listener {
    private FunctionHandler funHandler = FacilityProp.getFunctionHandler();

    private static HashMap<Location, Block> clickedBlockList = new HashMap<>();

    @EventHandler
    public void OnInteractDetection(CustomBlockInteractEvent e) {
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            e.setCancelled(true);
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            String temp = funHandler.blockEquality(e);

            if (temp != null && funHandler.itemEquality(e, temp)) {
            }
        }
    }

    @EventHandler
    public void HaltBroken(CustomBlockBreakEvent e) {
        if (!clickedBlockList.isEmpty()) {
            if (clickedBlockList.get(e.getBlock().getLocation()) != null && clickedBlockList.get(e.getBlock().getLocation()).equals(e.getBlock())) {
                e.setCancelled(true);
            }
        }
    }

    public static HashMap<Location, Block> getClickedBlockList() {
        return clickedBlockList;
    }
}
