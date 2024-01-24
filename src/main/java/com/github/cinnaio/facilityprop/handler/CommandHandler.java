package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements TabExecutor {
    private ConfigurationHandler configInstance = FacilityProp.getConfigInstance();

    private FunctionHandler functionHandler = FacilityProp.getFunctionHandler();

    private i18Handler i18Handler = configInstance.getI18h();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            if (sender.hasPermission("facilityprop.admin")) {
                if (args[0].equals("reload")) {
                    this.configInstance.reload();

                    MessageUtils.sendMessage(sender, i18Handler.success_reload_config);
                    return true;
                }

                if (args[0].equals("list")) {
                    this.functionHandler.listAllFacilities(sender);
                    return true;
                }
            }

            MessageUtils.sendMessage(sender, i18Handler.error_permission);
            return false;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList();
        if (args.length == 1) {
            completions.add("reload");
            completions.add("list");
        }

        return completions;
    }
}