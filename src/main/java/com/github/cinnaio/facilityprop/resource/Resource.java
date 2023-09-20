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
            return file.getString(MAIN_HEXCODE_ACTIONBAR);
        else if (style == HexCodeUtils.HexCode.BOSSBAR)
            return file.getString(MAIN_HEXCODE_BOSSBAR);
        else
            return file.getString(MAIN_HEXCODE_NORMAL);
    }

    public static String getNameSpace(String n) {
        return file.getString(FACILITY + n + NAMESPACE);
    }
    public static String getTargetNameSpace(String n) {
        return file.getString(FACILITY + n + TARGET_NAMESPACE);
    }

    public static List<List<Object>> getAcquireItems(String n) {
        List<List<Object>> outerList = new ArrayList<>();

        if (file.isConfigurationSection(FACILITY + n + ACQUIRE_ITEMS)) {
            ConfigurationSection outerPart = file.getConfigurationSection(FACILITY + n + ACQUIRE_ITEMS);

            for (String outerKey : outerPart.getKeys(false)) {
                Boolean skipInnerList = true;

                for (String innerKey : outerPart.getKeys(true)) {
                    List<Object> innerList = new ArrayList<>();

                    if (innerKey.equals(outerKey + CUSTOM_MODEL_DATA)) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    } else if (innerKey.equals(outerKey + AMOUNT)) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    } else if (innerKey.equals(outerKey + WAITTING)) {
                        innerList.add(outerPart.getInt(innerKey));
                        skipInnerList = false;
                    } else if (innerKey.equals(outerKey + CONDITIONS)) {
                        ConfigurationSection conList = outerPart.getConfigurationSection(outerKey + CONDITIONS);

                        for (String conKeys : conList.getKeys(false)) {
                            if (conKeys.equals(WEATHER)) {
                                innerList.add(conList.getString(conKeys));
                                skipInnerList = false;
                            } else if (conKeys.equals(PERMISSIONS)) {
                                innerList.add(conList.getStringList(conKeys));
                                skipInnerList = false;
                            }
                        }
                    } else if (innerKey.equals(outerKey + RESULT_ITEMS)) {
                        ConfigurationSection conList = outerPart.getConfigurationSection(outerKey + RESULT_ITEMS);

                        for (String id : conList.getKeys(false)) {
                            List<Object> innerDoubleList = new ArrayList<>();

                            for (String resKeys : conList.getKeys(true)) {
                                if (resKeys.equals(id + RESULT_CUSTOM)) {
                                    innerDoubleList.add(conList.getBoolean(resKeys));
                                } else if (resKeys.equals(id + RESULT_MATERIAL)) {
                                    innerDoubleList.add(conList.getString(resKeys));
                                } else if (resKeys.equals(id + RESULT_NAMESPACE)) {
                                    innerDoubleList.add(conList.getString(resKeys));
                                } else if (resKeys.equals(id + RESULT_AMOUNT)) {
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
        return file.getInt(FACILITY + n + ".custom_model_data");
    }

    public static String getSoundConsistent(String n) {
        return file.getString(FACILITY + n + ".sound_consistent");
    }

    public static String getSoundEnd(String n) {
        return file.getString(FACILITY + n + ".sound_end");
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

        if (file.isConfigurationSection(RE_FACILITY)) {
            ConfigurationSection outerPart = file.getConfigurationSection(RE_FACILITY);

            for (String string : outerPart.getKeys(false)) {
                stringList.add(string);
            }
        }
        return stringList;
    }
}
