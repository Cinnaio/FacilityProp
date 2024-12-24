package com.github.cinnaio.facilityprop.event;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.handler.OperationHandler;
import com.github.cinnaio.facilityprop.handler.StructionHandler;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractionListener implements Listener {
    private final StructionHandler strc = FacilityProp.getInstance().getStructionHandler();
    private final OperationHandler oper = FacilityProp.getInstance().getOperationHandler();

    @EventHandler
    public void isMachineGuiCreate(CustomBlockPlaceEvent e) {
        if (strc.isMachineId(e.getNamespacedID())) {
            String macnode = strc.getTargetMachineNode(e.getNamespacedID()).toString();

            if (strc.getIsCreateGUI(macnode)) {
                oper.createMachineGUI(e.getBlock().getLocation(), strc.getInvAmount(macnode), macnode);
            }
        }
    }

    @EventHandler
    public void isMachineActivation(CustomBlockInteractEvent e) {
        if (e.getHand().equals(EquipmentSlot.OFF_HAND))
            e.setCancelled(true);

        ItemStack stack = e.getItem();
        Player p = e.getPlayer();
        String w = e.getBlockClicked().getWorld().getName();
        String blockId = e.getNamespacedID();
        Location loc = e.getBlockClicked().getLocation();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null) {
            if (strc.isMachineId(blockId)) {
                String macnode = strc.getTargetMachineNode(blockId).toString();

                if (strc.isExistRecipeItem(macnode, e, stack)) {
                    int recipeIndex = strc.getSpecialRecipeIndex(macnode, e, stack);
                    int amountTmp = strc.getRecipeSpecialAmount(macnode, recipeIndex);

                    if (e.getItem().getAmount() >= amountTmp) {
                        String finalItemId = strc.getRecipeFinalItem(macnode, recipeIndex);
                        ItemStack finalItemStack = ItemsAdder.getCustomItem(finalItemId);
                        int finalItemAmount = strc.getRecipeFinalAmount(macnode, recipeIndex);

                        // 延迟逻辑
                        Bukkit.getScheduler().runTaskLater(FacilityProp.getInstance(), () -> {
                            if (Bukkit.getWorld(w).getBlockAt(loc).getType() != Material.AIR) {
                                if (finalItemStack == null) {
                                    ItemStack finalItem = new ItemStack(Material.getMaterial(finalItemId), finalItemAmount);

                                    oper.changePlayerItem(p, stack, amountTmp);
                                    oper.addItemToMachine(loc, finalItem);

                                    return;
                                }

                                finalItemStack.setAmount(finalItemAmount);

                                oper.changePlayerItem(p, stack, amountTmp);
                                oper.addItemToMachine(loc, finalItemStack);

                                return;
                            }
                        }, 40L);
                    }

                    System.out.println("fail");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(CustomBlockInteractEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        Location loc = e.getBlockClicked().getLocation();

        if (p.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (strc.isMachineId(e.getNamespacedID())) {
                Inventory machineInventory = oper.getMachineInventory(w.getName(), loc);

                if (machineInventory != null) {
                    p.openInventory(machineInventory);
                    e.setCancelled(true);
                }
            }
        }
    }

}
