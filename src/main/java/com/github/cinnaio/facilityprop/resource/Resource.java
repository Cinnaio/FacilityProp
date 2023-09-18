package com.github.cinnaio.facilityprop.resource;

import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

import static com.github.cinnaio.facilityprop.ConfigHandler.file;
import static com.github.cinnaio.facilityprop.resource.Configuration.*;


public class Resource {
    public static String getHexCode(HexCodeUtils.HexCode style) {
        if (style == HexCodeUtils.HexCode.ACTIONBAR)
            return file.getString(main_hexcode_actionbar);
        else if (style == HexCodeUtils.HexCode.BOSSBAR)
            return file.getString(main_hexcode_bossbar);
        else
            return file.getString(main_hexcode_normal);
    }

    public static String getNameSpace(String n) {
        return file.getString(facility + n + namespace);
    }
    public static String getTargetNameSpace(String n) {
        return file.getString(facility + n + target_namespace);
    }

    public static List<List<Object>> getAcquireItems(String n) {
        List<List<Object>> outerList = new ArrayList<>();

        if (file.isConfigurationSection(facility + n + acquire_items)) {
            ConfigurationSection outerPart = file.getConfigurationSection(facility + n + acquire_items);

            for (String outerKey : outerPart.getKeys(false)) {
                Boolean skipInnerList = true;

                for (String innerKey : outerPart.getKeys(true)) {
                    List<Object> innerList = new ArrayList<>();

                    if (innerKey.equals(outerKey + custom_model_data)) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    } else if (innerKey.equals(outerKey + amount)) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    } else if (innerKey.equals(outerKey + waitting)) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    } else if (innerKey.equals(outerKey + conditions)) {
                        ConfigurationSection conList = outerPart.getConfigurationSection(outerKey + conditions);

                        for (String conKeys : conList.getKeys(false)) {
                            if (conKeys.equals(weather)) {
                                innerList.add(conList.getString(conKeys));
                                skipInnerList = false;
                            } else if (conKeys.equals(permissions)) {
                                innerList.add(conList.getStringList(conKeys));
                                skipInnerList = false;
                            }
                        }
                    } else if (innerKey.equals(outerKey + result_items)) {
                        ConfigurationSection conList = outerPart.getConfigurationSection(outerKey + result_items);

                        for (String id : conList.getKeys(false)) {
                            List<Object> innerDoubleList = new ArrayList<>();

                            for (String resKeys : conList.getKeys(true)) {
                                if (resKeys.equals(id + result_custom)) {
                                    innerDoubleList.add(conList.getBoolean(resKeys));
                                } else if (resKeys.equals(id + result_material)) {
                                    innerDoubleList.add(conList.getString(resKeys));
                                } else if (resKeys.equals(id + result_namespace)) {
                                    innerDoubleList.add(conList.getString(resKeys));
                                } else if (resKeys.equals(id + result_amount)) {
                                    innerDoubleList.add(conList.getInt(resKeys));
                                }
                            }
                            innerList.add(innerDoubleList);
                            skipInnerList = false;
                        }
                    }

                    if (!skipInnerList) {
                        outerList.add(innerList);
                    }
                }
            }
        }
        outerList.removeIf(List::isEmpty);

        return outerList;
    }

    public static Integer getCustomModelData(String n) {
        return file.getInt(facility + n + ".custom_model_data");
    }

    public static Integer getWaitting(String n, PlayerInteractEvent e) {
        Boolean flag1 = false;
        Boolean flag = false;

        for (List<Object> items : getAcquireItems(n)) {
            for (Object item : items) {
                if (item instanceof Integer) {
                    if (item.equals(e.getItem().getItemMeta().getCustomModelData())) {
                        flag1 = true;
                        continue;
                    }

                    if (flag1) {
                        flag1 = !flag1;
                        flag = true;
                        continue;
                    }

                    if (flag) {
                        flag = !flag;
                        return ((Integer) item).intValue() * 20;
                    }
                }
            }
        }
        return 0;
    }

    public static List<String> getFacility() {
        List<String> stringList = new ArrayList<>();

        if (file.isConfigurationSection(re_facility)) {
            ConfigurationSection outerPart = file.getConfigurationSection(re_facility);

            for (String string : outerPart.getKeys(false)) {
                stringList.add(string);
            }
        }
        return stringList;
    }
}
