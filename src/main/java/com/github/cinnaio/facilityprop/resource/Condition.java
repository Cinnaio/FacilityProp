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

        for (List<Integer> item : Resource.getAcquireItems(n)) {
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
        Boolean flag = false;

        for (List<Integer> items : Resource.getAcquireItems(n)) {
            if (items.get(0) == e.getItem().getItemMeta().getCustomModelData()) {
                flag = true;
                continue;
            }

            if (flag) {
                tmpAmount = items.get(0);
                break;
            }
        }

        if (holdAmount >= tmpAmount) {
            if (holdAmount.equals(tmpAmount)) {
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
