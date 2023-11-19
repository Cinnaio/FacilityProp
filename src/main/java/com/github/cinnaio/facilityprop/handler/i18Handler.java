package com.github.cinnaio.facilityprop.handler;

import org.bukkit.configuration.file.FileConfiguration;

public class i18Handler {
    public String error_toop;
    public String error_weather;
    public String error_money;
    public String error_permission;
    public String error_amount;
    public String error_item;
    public String success_money;

    public i18Handler(FileConfiguration lang) {
        error_toop = lang.getString("error_toop");
        error_weather = lang.getString("error_weather");
        error_money = lang.getString("error_money");
        error_permission = lang.getString("error_permission");
        error_amount = lang.getString("error_amount");
        error_item = lang.getString("error_item");
        success_money = lang.getString("success_money");
    }
}
