package com.github.cinnaio.facilityprop;

import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (sender.hasPermission("facilityprop.admin")) {
            if (args[0].equals("reload")) {
                ConfigHandler.relaodConfig();
                sender.sendMessage(HexCodeUtils.translateHexCodes("&8[ &#d9fbb4群隙 &8] &#FBEDF6配置文件已经重载！"));
                return true;
            }
        }
        sender.sendMessage(HexCodeUtils.translateHexCodes("&8[ &#d9fbb4群隙 &8] &#FBEDF6所需权限不足！"));
        return false;
    }
}
