package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.*;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class Property {
    public static void PropTeapan(Player p, Location loc, String n) {
        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            CustomBlock.place("grxi:teapan_wet", loc);
        }, 3L);

        MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes("&#FBB6B6" + Language.crafting_teapan));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            CustomBlock.place("grxi:teapan", loc);
        }, Resource.getTime(n));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            p.getInventory().addItem(ItemsAdder.getCustomItem("grxi:amber"));
        }, Resource.getTime(n) + 3L);

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes("&#FBB6B6" + Language.successful_teapan));
        }, Resource.getTime(n));

        MessageUtils.sendBossBarTick(p, Resource.getTime(n), BarColor.PINK, BarStyle.SEGMENTED_10);
    }
}
