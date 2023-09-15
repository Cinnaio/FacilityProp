package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.*;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Property {
    private static boolean flag = false;

    public static void PropTeapan(Player p, Location loc, String n) {
        CustomBlock.place("grxi:teapan_wet", loc);
        MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.crafting_teapan));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            CustomBlock.place("grxi:teapan", loc);
        }, Resource.getWaittingTime(n));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            p.getInventory().addItem(ItemsAdder.getCustomItem("grxi:amber"));
        }, Resource.getWaittingTime(n) + 3L);

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.successful_teapan));
        }, Resource.getWaittingTime(n));

        flag = true;
        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            flag = !flag;
        }, Resource.getWaittingTime(n));
    }

    public static Boolean getFlag() {
        if (flag) {
            return true;
        }
        else
            return false;
    }
}
