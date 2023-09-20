package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import com.github.cinnaio.facilityprop.utils.MessageUtils;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Property {
    private static boolean flag = false;

    public static void Property(Player p, Location loc, String n, PlayerInteractEvent e) {
        flag = true;

        CustomBlock.place(Resource.getTargetNameSpace(n), loc);
        MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.CRAFTING_TEAPAN));

        Bukkit.getScheduler().runTaskTimer(FacilityProp.instance, new Runnable() {
            Integer realTime = Resource.getWaitting(n, e) / 20;
            @Override
            public void run() {
                if (realTime > 0)
                    p.playSound(p, Sound.valueOf(Resource.getSoundConsistent(n)), 10, 4);

                realTime--;
            }
        }, 0, 20L);

        int taskId = Bukkit.getScheduler().runTaskTimer(FacilityProp.instance, new Runnable() {
            Integer realTime = Resource.getWaitting(n, e) / 20;
            @Override
            public void run() {
                if (realTime > 0)
                    p.playSound(p, Sound.valueOf(Resource.getSoundConsistent(n)), 10, 4);

                realTime--;
            }
        }, 0, 20L).getTaskId();

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            Bukkit.getScheduler().cancelTask(taskId);
        }, Resource.getWaitting(n, e));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            CustomBlock.place(Resource.getNameSpace(n), loc);
            MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.SUCCESSFUL_CRAFT));
            p.playSound(p, Sound.valueOf(Resource.getSoundEnd(n)), 10, 4);

            flag = !flag;
        }, Resource.getWaitting(n, e));

        Bukkit.getScheduler().runTaskLater(FacilityProp.instance, () -> {
            Condition.sendResultItems(n, e);
        }, Resource.getWaitting(n, e) + 3L);
    }

    public static Boolean getFlag() {
        if (flag) {
            return true;
        }
        else
            return false;
    }
}
