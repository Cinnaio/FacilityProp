package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.listener.InteractEvent;
import com.github.cinnaio.facilityprop.utils.MessageUtils;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionHandler {
    private ConfigurationHandler configHandler = FacilityProp.getConfigInstance();

    private HashMap<String, Object> configMap;

    private Map<Location, Block> clickedBlockList = InteractEvent.getClickedBlockList();

    private i18Handler i18Handler;

    private Economy economy;

    private JavaPlugin instance;

    static String SUN = "sun";

    static String RAIN = "rain";

    static String SNOW = "snow";

    static String STORM = "storm";

    public FunctionHandler() {
        configMap = configHandler.getConfigMap();

        economy = FacilityProp.getEconomy();

        instance = FacilityProp.getInstance();

        i18Handler = configHandler.getI18h();
    }

    public String getWorldWeather(Player player) {
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
    public boolean itemEquality(CustomBlockInteractEvent e, String facilityId) {
        Player p = e.getPlayer();

        if (e.getItem() == null) {
            return false;
        } else {
            String itemId = ItemsAdder.getCustomItemName(e.getItem());

            ConfigurationSection blockNodee = configHandler.getFile().getConfigurationSection("facilities." + facilityId);

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
                                                MessageUtils.sendMessage(p, i18Handler.error_permission);
                                                return false;
                                            }
                                            clickedBlockList.put(e.getBlockClicked().getLocation(), e.getBlockClicked());
                                            preConduct(e, innerContext, rquireAmount, requireNodeName, p, facilityId);

                                            return true;
                                        }
                                    } else {
                                        clickedBlockList.put(e.getBlockClicked().getLocation(), e.getBlockClicked());
                                        preConduct(e, innerContext, rquireAmount, requireNodeName, p, facilityId);

                                        return true;
                                    }
                                }
                                // 天气不匹配信息
                                MessageUtils.sendMessage(p, i18Handler.error_weather);
                                return false;
                            }
                            // 手上物品数量不匹配信息
                            MessageUtils.sendMessage(p, i18Handler.error_amount);
                            return false;

                        }
                    }
                }
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean preConduct(CustomBlockInteractEvent e, String innerContext, int rquireAmount, String requireNodeName, Player p, String facilityId) {
        double money = 0;

        int exp = 0;

        EconomyResponse r = null;

        if (FacilityProp.getEconomy() != null) {
            r = economy.withdrawPlayer(e.getPlayer(), money);
        }

        try {
            if (configMap.get(innerContext + ".exp") != null) {
                exp = (int) configMap.get(innerContext + ".exp");

                if (p.getTotalExperience() >= exp) {
                    p.giveExp(-exp);
                } else {
                    MessageUtils.sendMessage(p, i18Handler.error_exp);
                    return false;
                }
            }

            if (configMap.get(innerContext + ".money") != null) {
                money = (double) configMap.get(innerContext + ".money");
            }
        } catch (ClassCastException exception) {
            instance.getLogger().severe("Exception thrown: " + exception);
            MessageUtils.sendMessage(p, i18Handler.error_toop);

            return false;
        }

        if (r.transactionSuccess()) {
            if (e.getItem().getAmount() > rquireAmount) {
                e.getItem().setAmount(e.getItem().getAmount() - (Integer) configMap.get(requireNodeName + ".amount"));
            } else {
                e.getItem().setAmount(0);
            }

            if (money != 0) {
                MessageUtils.sendMessage(p, i18Handler.success_money + money);
            }

            String temp = requireNodeName + ".provided";

            ConfigurationSection providedNode = configHandler.getFile().getConfigurationSection(temp);

            if (providedNode != null) {
                for (String context : providedNode.getKeys(false)) {
                    Integer amountData = (Integer) configMap.get(temp + "." + context + ".amount");

                    try {
                        String expression = (String) configMap.get(temp + "." + context + ".expression");

                        int luckLevel = 0;

                        for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                            if (potionEffect.getType().equals(PotionEffectType.LUCK)) {
                                luckLevel = potionEffect.getAmplifier() + 1;
                            }
                        }

                        Argument level = new Argument("level", luckLevel);

                        Expression expr = new Expression(expression, level);

                        System.out.println(expr.calculate());

                        if (Math.random() >= (expr.calculate() / 100)) {
                            amountData = 0;
                        }

                    } catch (NullPointerException exception) {
                        instance.getLogger().severe("Exception thrown: " + exception);
                        MessageUtils.sendMessage(p, i18Handler.error_toop);

                        return false;
                    }

                    if (CustomStack.isInRegistry((String) configMap.get(temp + "." + context + ".name-space"))) {
                        ItemStack itemStack = ItemsAdder.getCustomItem((String) configMap.get(temp + "." + context + ".name-space"));
                        itemStack.setAmount(amountData);

                        conduct(e, p, itemStack, facilityId, requireNodeName);
                    } else {
                        String materialData = ((String) configMap.get(temp + "." + context + ".name-space")).substring(((String) configMap.get(temp + "." + context + ".name-space")).indexOf(":") + 1).toUpperCase();

                        ItemStack itemStack = new ItemStack(Material.getMaterial(materialData), amountData);

                        conduct(e, p, itemStack, facilityId, requireNodeName);
                    }
                }
            }

            return true;
        }

        MessageUtils.sendMessage(p, i18Handler.error_money);
        return false;
    }

    public void conduct(CustomBlockInteractEvent e, Player p, ItemStack itemStack, String facilityId, String requireNodeName) {
        String facilityID = "facilities." + facilityId;

        String requireDelay = requireNodeName + ".delay";

        String requireExp = requireNodeName + ".exp-gived";

        long delay = (Integer) configMap.get(requireDelay) * 20L;

        Location location = e.getBlockClicked().getLocation();

        String soundProcessing = (String) configMap.get(facilityID + ".sound-processing");
        String soundProcessed = (String) configMap.get(facilityID + ".sound-processed");
        String namespace = (String) configMap.get(facilityID + ".name-space");
        String targetNamespace = (String) configMap.get(facilityID + ".target-name-space");

        CustomBlock.place(targetNamespace, location);
        p.playSound(location, Sound.valueOf(soundProcessing), 10, 4);

        Bukkit.getScheduler().runTaskLater(instance, () -> {
            p.stopSound(Sound.valueOf(soundProcessing));

            CustomBlock.place(namespace, location);
            p.playSound(location, Sound.valueOf(soundProcessed), 10, 4);

            if (configMap.get(requireExp) != null) {
                int exp = (int) configMap.get(requireExp);

                p.giveExp(exp);
                p.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 0.1f, 1.0f);
            }

            Bukkit.getScheduler().runTaskLater(instance, () -> {
                Player player = Bukkit.getPlayer(e.getPlayer().getUniqueId());
                if (player != null && player.isOnline()) {
                    e.getPlayer().getInventory().addItem(itemStack);
                }

                clickedBlockList.remove(e.getBlockClicked().getLocation());
            }, 3L);
        }, delay);
    }

    public void listAllFacilities(CommandSender sender) {
        int a = 0;
        ConfigurationSection facilitiesNode = configHandler.getFile().getConfigurationSection("facilities");

        if (facilitiesNode != null) {
            for (String blockNode : facilitiesNode.getKeys(false)) {
                a++;
            }
        }

        MessageUtils.sendMessage(sender, i18Handler.number_facility + a);
    }
}
