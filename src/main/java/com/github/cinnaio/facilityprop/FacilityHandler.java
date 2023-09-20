package com.github.cinnaio.facilityprop;

import com.github.cinnaio.facilityprop.resource.Condition;
import com.github.cinnaio.facilityprop.resource.Property;
import com.github.cinnaio.facilityprop.resource.Resource;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FacilityHandler implements Listener {
    @EventHandler
    public void FacilityTeapan(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        for (String string : Resource.getFacility()) {
            String n = string;

            if (e.getHand() == EquipmentSlot.OFF_HAND)
                e.setCancelled(true);
            else {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (ItemsAdder.isCustomBlock(e.getClickedBlock())) {
                        ItemStack block = ItemsAdder.getCustomBlock(e.getClickedBlock());
                        if (block.getItemMeta().getCustomModelData() == Resource.getCustomModelData(n)) {
                            Location loc = e.getClickedBlock().getLocation();
                            if (e.getMaterial() == Material.PAPER) {
                                if (Condition.isEqualItemCustomModelData(n, e)) {
                                    if (Condition.isEqualConditions(n, e, p)) {
                                        Condition.changeAmount(n, e, p, loc);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void HaltBroken(BlockBreakEvent e) {
        if (ItemsAdder.isCustomBlock(e.getBlock())) {
            if (Property.getFlag()) {
                e.setCancelled(true);
            }
        }
    }
}
