package com.github.cinnaio.facilityprop.listener;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.handler.FunctionHandler;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractEvent implements Listener {
    public static boolean flag = false;

    private FunctionHandler funHandler = FacilityProp.getFunctionHandler();

    @EventHandler
    public void InteractDetection(CustomBlockInteractEvent e) {
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            e.setCancelled(true);
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            String temp = funHandler.blockEquality(e);

            if (temp != null && funHandler.itemEquality(e, temp)) {
                return;
            }
        }
    }

    @EventHandler
    public void HaltBroken(BlockBreakEvent e) {
        if (ItemsAdder.isCustomBlock(e.getBlock())) {
            if (flag) {
                e.setCancelled(true);
            }
        }
    }
}
