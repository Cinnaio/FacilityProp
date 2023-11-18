package com.github.cinnaio.facilityprop.handler;

import org.bukkit.configuration.file.FileConfiguration;

public class i18Handler {
    public final String tets;

    public i18Handler(FileConfiguration lang) {
        this.tets = lang.getString("success");
    }
}
