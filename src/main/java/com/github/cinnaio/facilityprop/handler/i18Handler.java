package com.github.cinnaio.facilityprop.handler;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

public class i18Handler {
    public String error_toop;

    public String error_weather;

    public String error_money;

    public String error_exp;

    public String error_permission;

    public String error_amount;

    public String error_item;

    public String success_money;

    public String success_reload_config;

    public String number_facility;

    public i18Handler(FileConfiguration lang) {
        error_toop = lang.getString("error_toop");

        error_weather = lang.getString("error_weather");

        error_money = lang.getString("error_money");

        error_exp = lang.getString("error_exp");

        error_permission = lang.getString("error_permission");

        error_amount = lang.getString("error_amount");

        error_item = lang.getString("error_item");

        success_money = lang.getString("success_money");

        success_reload_config = lang.getString("success_reload_config");

        number_facility = lang.getString("number_facility");
    }
}
