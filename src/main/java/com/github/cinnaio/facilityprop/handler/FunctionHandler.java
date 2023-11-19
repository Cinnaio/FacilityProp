package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class FunctionHandler {
    private ConfigurationHandler configHandler = FacilityProp.getConfigInstance();

    private HashMap<String, Object> configMap;

    private i18Handler i18Handler;

    private Economy economy;

    private JavaPlugin instance;

    private String prefix;

    static String SUN = "sun";

    static String RAIN = "rain";

    static String SNOW = "snow";

    static String STORM = "storm";

    public static String getWorldWeather(Player player) {
        World world = player.getWorld();

        String tmp = world.isClearWeather() ? SUN : RAIN;

        if (tmp.equals(RAIN)) {
            if (world.hasStorm()) {
                return world.hasStorm() ? RAIN : SNOW;
            } else {
                return STORM;
            }
        } else {
            return SUN;
        }
    }

    public FunctionHandler() {
        configMap = configHandler.getConfigMap();

        economy = FacilityProp.getEconomy();

        instance = FacilityProp.getInstance();

        i18Handler = configHandler.getI18h();

        prefix = configMap.get("basic.prefix") + " &#FBEDF6";
    }

    @SuppressWarnings("deprecation")
    public String blockEquality(CustomBlockInteractEvent e) {
        if (ItemsAdder.isCustomBlock(e.getBlockClicked())) {
            String blockId = e.getNamespacedID();

            ConfigurationSection facilitiesNode = configHandler.getFile().getConfigurationSection("facilities");

            for (String blockNode : facilitiesNode.getKeys(false)) {
                Object exisitBlockId = configMap.get(facilitiesNode.getName() + "." + blockNode + ".name-space");

                if (exisitBlockId.equals(blockId)) {
                    return blockNode;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean itemEquality(CustomBlockInteractEvent e, String string) {
        if (e.getItem() == null) {
            return false;
        } else {
            String itemId = ItemsAdder.getCustomItemName(e.getItem());

            ConfigurationSection blockNodee = configHandler.getFile().getConfigurationSection("facilities." + string);

            for (String itemsNode : blockNodee.getKeys(false)) {
                if (itemsNode.equals("exchanges")) {
                    ConfigurationSection exchangeNode = blockNodee.getConfigurationSection("exchanges");

                    for (String exchangeItem : exchangeNode.getKeys(false)) {
                        String requireNodeName = "facilities." + blockNodee.getName() + "." + exchangeNode.getName() + "." + exchangeItem;

                        String itemID = ((String) configMap.get(requireNodeName + ".name-space")).toUpperCase();

                        if (itemId.equals(configMap.get(requireNodeName + ".name-space")) || e.getItem().getType().equals(Material.getMaterial(itemID))) {

                            int rquireAmount = (Integer) configMap.get(requireNodeName + ".amount");

                            if (e.getItem().getAmount() >= rquireAmount) {
                                String innerContext = requireNodeName + ".conditions";

                                if (configMap.get(innerContext + ".weather").equals(getWorldWeather(e.getPlayer()))) {
                                    if (configMap.get(innerContext + ".permission") != null) {
                                        List permissions = (List) configMap.get(innerContext + ".permission");

                                        for (Object permission : permissions) {
                                            if (!e.getPlayer().hasPermission((String) permission)) {
                                                // 权限缺少信息
                                                e.getPlayer().sendMessage(HexCodeUtils.translateHexCodes(prefix + i18Handler.error_permission));
                                                return false;
                                            }

                                            if (configMap.get(innerContext + ".money") != null) {
                                                return conduct(e, innerContext, rquireAmount, requireNodeName);
                                            }
                                        }
                                    } else if (configMap.get(innerContext + ".money") != null) {
                                        return conduct(e, innerContext, rquireAmount, requireNodeName);
                                    }
                                }
                                // 天气不匹配信息
                                e.getPlayer().sendMessage(HexCodeUtils.translateHexCodes(prefix + i18Handler.error_weather));
                                return false;
                            }
                            // 手上物品数量不匹配信息
                            e.getPlayer().sendMessage(HexCodeUtils.translateHexCodes(prefix + i18Handler.error_amount));
                            return false;

                        }
//                        // 手上物品ID不匹配信息
//                        e.getPlayer().sendMessage(HexCodeUtils.translateHexCodes(prefix + i18Handler.error_item));
//                        return false;
                    }
                }
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean conduct(CustomBlockInteractEvent e, String innerContext, int rquireAmount, String requireNodeName) {
        try {
            double money = (double) configMap.get(innerContext + ".money");

            EconomyResponse r = economy.withdrawPlayer(e.getPlayer(), money);

            if (r.transactionSuccess()) {
                if (e.getItem().getAmount() > rquireAmount) {
                    e.getItem().setAmount(e.getItem().getAmount() - (Integer)configMap.get(requireNodeName + ".amount"));
                } else {
                    e.getItem().setAmount(0);
                }

                e.getPlayer().sendMessage(HexCodeUtils.translateHexCodes(prefix + i18Handler.success_money + " " + money + " 金币"));

                String temp = requireNodeName + ".provided";

                ConfigurationSection providedNode = configHandler.getFile().getConfigurationSection(temp);

                if (providedNode != null) {
                    for (String context : providedNode.getKeys(false)) {
                        Integer amountData = (Integer) configMap.get(temp + "." + context + ".amount");

                        if (CustomStack.isInRegistry((String) configMap.get(temp + "." + context + ".name-space"))) {
                            ItemStack itemStack = ItemsAdder.getCustomItem((String) configMap.get(temp + "." + context + ".name-space"));
                            itemStack.setAmount(amountData);

                            e.getPlayer().getInventory().addItem(itemStack);
                        } else {
                            String materialData = ((String) configMap.get(temp + "." + context + ".name-space")).substring(((String) configMap.get(temp + "." + context + ".name-space")).indexOf(":") + 1).toUpperCase();

                            ItemStack itemStack = new ItemStack(Material.getMaterial(materialData), amountData);

                            e.getPlayer().getInventory().addItem(itemStack);
                        }
                    }
                }

                return true;
            }

            e.getPlayer().sendMessage(HexCodeUtils.translateHexCodes(prefix + i18Handler.error_money));
            return false;
        } catch (ClassCastException exception) {
            instance.getLogger().severe("Exception thrown: " + exception);
            e.getPlayer().sendMessage(HexCodeUtils.translateHexCodes(prefix + i18Handler.error_toop));
        }

        return false;
    }

    public void listAllFacilities(CommandSender sender) {
        int a = 0;
        ConfigurationSection facilitiesNode = configHandler.getFile().getConfigurationSection("facilities");

        if (facilitiesNode != null) {
            for (String blockNode : facilitiesNode.getKeys(false)) {
                a++;
            }
        }

        sender.sendMessage("所有设备数量: " + a);
        a = 0;
    }
}
