package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import com.github.cinnaio.facilityprop.utils.MessageUtils;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class Condition {
    static String sun = "sun";
    static String rain = "rain";
    static String snow = "snow";
    static String storm = "storm";

    public static String getWorldWeather(Player p) {
        World w = p.getWorld();

        String tmp = w.isClearWeather() ? sun : rain;

        if (tmp.equals(rain)) {
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

        for (List<Object> items : Resource.getAcquireItems(n)) {

            for (Object item : items) {
                if (item instanceof Integer) {
                    if (item.equals(i.getItemMeta().getCustomModelData())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Boolean isEqualConditions(String n, PlayerInteractEvent e, Player p) {
        Boolean flag = false;

        for (List<Object> items : Resource.getAcquireItems(n)) {
            for (Object item : items) {
                if (item instanceof Integer) {
                    if (item.equals(e.getItem().getItemMeta().getCustomModelData())) {
                        flag = true;
                    }
                }

                if (item instanceof String && flag) {
                    if (item.equals(sun) || item.equals(rain) || item.equals(snow) || item.equals(storm)) {
                        if (item.equals(getWorldWeather(e.getPlayer()))) {
                            if (items.size() == 2) {
                                List<Object> tmp = items.stream().filter(s -> !(s instanceof Integer)).collect(Collectors.toList());
                                List<Object> temp = tmp.stream().skip(1).collect(Collectors.toList());

                                for (Object s : temp) {
                                    String per = s.toString();
                                    if (!e.getPlayer().hasPermission(per.replaceAll("\\[", "").replaceAll("]", ""))) {
                                        MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.error_permission + " " + per));
                                        return false;
                                    }
                                }
                            }
                        } else {
                            MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.error_weather));
                            return false;
                        }
                    }

                    flag = !flag;
                }
            }
        }

        return true;
    }

    public static void changeAmount(String n, PlayerInteractEvent e, Player p, Location loc) {
        Integer holdAmount = e.getItem().getAmount();
        Integer tmpAmount = 0;
        Boolean flag = false;

        for (List<Object> items : Resource.getAcquireItems(n)) {
            for (Object item : items) {
                if (item instanceof Integer) {
                    if (item.equals(e.getItem().getItemMeta().getCustomModelData())) {
                        flag = true;
                        continue;
                    }

                    if (flag) {
                        tmpAmount = (Integer) item;
                        flag = !flag;
                        break;
                    }
                }
            }
        }

        if (holdAmount >= tmpAmount) {
            if (holdAmount.equals(tmpAmount)) {
                e.getItem().setAmount(0);
                Property.PropTeapan(p, loc, n, e);
            } else {
                e.getItem().setAmount(e.getItem().getAmount() - tmpAmount);
                Property.PropTeapan(p, loc, n, e);
            }
        } else {
            MessageUtils.sendActionBar(p, HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.ACTIONBAR) + Language.error_amount));
        }
    }

    public static void sendResultItems(String n, PlayerInteractEvent e) {
        Boolean flag = false;
        Boolean flag1 = false;

        for (List<Object> items : Resource.getAcquireItems(n)) {
            for (Object item : items) {
                if (item instanceof Integer) {
                    if (item.equals(e.getItem().getItemMeta().getCustomModelData())) {
                        flag = true;
                        flag1 = true;
                    }
                }
            }

            if (flag) {
                if (items.size() > 1) {
                    if (flag1) {
                        flag1 = !flag1;
                        continue;
                    }
                    List<Object> tmp = items.stream().collect(Collectors.toList());

                    for (Object object : tmp) {
                        List<Object> temp = (List<Object>) object;
                        if ((Boolean) temp.get(0)) {
                            String naspaceData = (String) temp.get(1);
                            Integer amountData = (Integer) temp.get(2);

                            ItemStack itemStack = CustomStack.getInstance(naspaceData).getItemStack();
                            itemStack.setAmount(amountData);

                            e.getPlayer().getInventory().addItem(itemStack);
                        } else {
                            String materialData = ((String) temp.get(1)).toUpperCase();
                            Integer amountData = (Integer) temp.get(2);

                            ItemStack itemStack = new ItemStack(Material.getMaterial(materialData), amountData);

                            e.getPlayer().getInventory().addItem(itemStack);
                        }
                    }
                    flag = !flag;
                }
            }
        }
    }
}
