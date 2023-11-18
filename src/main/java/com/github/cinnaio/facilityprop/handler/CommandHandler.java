package com.github.cinnaio.facilityprop.handler;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.utils.HexCodeUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private ConfigurationHandler configInstance = FacilityProp.getConfigInstance();
    private FunctionHandler functionHandler = FacilityProp.getFunctionHandler();
    private Economy economy = FacilityProp.getEconomy();

    public CommandHandler() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            if (sender.hasPermission("facilityprop.admin")) {
                if (args[0].equals("reload")) {
                    this.configInstance.reload();
                    sender.sendMessage(HexCodeUtils.translateHexCodes("&8[ &#d9fbb4群隙 &8] &#FBEDF6配置文件已经重载！"));

                    return true;
                }

                if (args[0].equals("list")) {
                    this.functionHandler.listAllFacilities(sender);

                    return true;
                }

                if (args[0].equals("test-money")) {
                    Player player = (Player)sender;
                    sender.sendMessage(String.format("You have %s", this.economy.format(this.economy.getBalance(player.getName()))));
                    EconomyResponse r = this.economy.depositPlayer(player, 1.05);
                    if (r.transactionSuccess()) {
                        sender.sendMessage(String.format("You were given %s and now have %s", this.economy.format(r.amount), this.economy.format(r.balance)));
                    } else {
                        sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
                    }

                    return true;
                }
            }

            sender.sendMessage(HexCodeUtils.translateHexCodes("&8[ &#d9fbb4群隙 &8] &#FBEDF6所需权限不足！"));
            return false;
        }
    }
}