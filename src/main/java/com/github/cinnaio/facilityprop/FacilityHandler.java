package com.github.cinnaio.facilityprop;

import com.github.cinnaio.facilityprop.resource.Property;
import com.github.cinnaio.facilityprop.resource.Resource;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FacilityHandler implements Listener {
    @EventHandler
    public void FacilityTeapan(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String n = "teapan";

        if (e.getHand() == EquipmentSlot.OFF_HAND)
            e.setCancelled(true);
        else {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (ItemsAdder.isCustomBlock(e.getClickedBlock())) {
                    ItemStack block = ItemsAdder.getCustomBlock(e.getClickedBlock());

                    if (block.getItemMeta().getCustomModelData() == Resource.getCustomModelData(n)) {
                        Location loc = e.getClickedBlock().getLocation();

                        if (e.getMaterial() == Material.PAPER) {
                            for (Integer integer : Resource.getNeedItems(n)) {
                                if (e.getItem().getItemMeta().getCustomModelData() == integer) {
                                    if (e.getItem().getAmount() >= 8) {
                                        if (e.getItem().getAmount() == 8) {
                                            e.getItem().setAmount(0);
                                            Property.PropTeapan(p, loc);
                                        } else {
                                            e.getItem().setAmount(e.getItem().getAmount() - 8);
                                            Property.PropTeapan(p, loc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
