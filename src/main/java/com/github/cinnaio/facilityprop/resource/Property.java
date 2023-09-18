package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import com.github.cinnaio.facilityprop.utils.MessageUtils;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Property {
    private static boolean flag = false;

    public static void PropTeapan(Player p, Location loc, String n, PlayerInteractEvent e) {
        CustomBlock.place(Resource.getTargetNameSpace(n), loc);
        MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.crafting_teapan));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            CustomBlock.place(Resource.getNameSpace(n), loc);
        }, Resource.getWaitting(n, e));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            Condition.sendResultItems(n, e);
        }, Resource.getWaitting(n, e) + 3L);

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.successful_teapan));
        }, Resource.getWaitting(n, e));

        flag = true;
        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            flag = !flag;
        }, Resource.getWaitting(n, e));
    }

    public static Boolean getFlag() {
        if (flag) {
            return true;
        }
        else
            return false;
    }
}
