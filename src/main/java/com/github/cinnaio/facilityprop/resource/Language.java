package com.github.cinnaio.facilityprop.resource;

import static com.github.cinnaio.facilityprop.ConfigHandler.langYaml;

public class Language {
    public static String successful_teapan = langYaml.getString("successful_teapan");

    public static String error_weather = langYaml.getString("error_weather");
    public static String error_amount = langYaml.getString("error_amount");
    public static String error_permission = langYaml.getString("error_permission");

    public static String crafting_teapan = langYaml.getString("crafting_teapan");
    public static String crafting_wait_time = langYaml.getString("crafting_wait_time");
}
