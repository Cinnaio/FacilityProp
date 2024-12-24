package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0)
            return false;

        switch (args[0]) {
            case "reload" : {
                FacilityProp.getInstance().reload();

                break;
            }
            case "add" : {
                break;
            }
            case "getmeta" : {
                break;
            }
            default : {
                return false;
            }
        }
        return false;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("add");
            completions.add("getmeta");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            completions.add("目录名 标签名");
        }

        return completions;
    }
}