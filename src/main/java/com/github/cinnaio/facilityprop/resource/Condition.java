package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import com.github.cinnaio.facilityprop.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Condition {
    public enum Weather {
        SUN, RAIN, STROM, SNOW
    }

    public static String getWorldWeather(Player p) {
        World w = p.getWorld();
        String sun = "sun";
        String rain = "rain";
        String snow = "snow";
        String storm = "storm";

        String tmp = w.isClearWeather() ? sun : rain;

        if (tmp == rain) {
            if (w.hasStorm()) {
                return w.hasStorm() ? rain : snow;
            } else {
                return storm;
            }
        } else
            return sun;
    }

    public static Boolean isEqualItemCustomModelData(String n, PlayerInteractEvent e) {
        ItemStack i = e.getItem();

        Integer tmpCustomModelData = 0;

        for (List<Integer> item : Resource.getNeedItems(n)) {
            for (Integer integer : item) {
                tmpCustomModelData = integer;
            }
            if (i.getItemMeta().getCustomModelData() == tmpCustomModelData)
                return true;
        }
        return false;
    }

    public static void changeAmount(String n, PlayerInteractEvent e, Player p, Location loc) {
        Integer holdAmount = e.getItem().getAmount();

        Integer tmpAmount = 0;

        boolean flag = true;
        for (List<Integer> item : Resource.getNeedItems(n)) {
            // integer 通过遍历 获得 cmd 和 amount 首先为cmd 10022  手上为10021
            for (Integer integer : item) {
                System.out.println("origin: " + integer);
                if (e.getItem().getItemMeta().getCustomModelData() == integer) {
                    flag = !flag;
                    continue;
                }
                // 第一次不相等 出 tmp = 10022 flag 为真 结束内循环
                tmpAmount = integer;
                System.out.println("now: tmp " + tmpAmount);
                if (flag) {
                    break;
                }
            }
            if (!flag) {
                break;
            }
        }

        if (holdAmount >= tmpAmount) {
            if (holdAmount == tmpAmount) {
                e.getItem().setAmount(0);
                Property.PropTeapan(p, loc, n);
            } else {
                e.getItem().setAmount(e.getItem().getAmount() - tmpAmount);
                Property.PropTeapan(p, loc, n);
            }
        } else {
            MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.error_amount));
        }
    }
}
