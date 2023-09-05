package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import com.github.cinnaio.facilityprop.utils.MessageUtils;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Property {
    public static void PropTeapan(Player p, Location loc) {
        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, new Runnable() {
            @Override
            public void run() {
                CustomBlock.place("grxi:teapan_wet", loc);
            }
        }, 3L);
        MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes("&#", "", "&#FBB6B6" + Language.successful_teapan).replace("&", "§"));
        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, new Runnable() {
            @Override
            public void run() {
                CustomBlock.place("grxi:teapan", loc);
            }
        }, 20L);
        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, new Runnable() {
            @Override
            public void run() {
                p.getInventory().addItem(ItemsAdder.getCustomItem("grxi:amber"));
            }
        }, 23L);
    }
}
