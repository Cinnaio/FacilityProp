package com.github.cinnaio.facilityprop.utils;

import com.github.cinnaio.facilityprop.FacilityProp;
import com.github.cinnaio.facilityprop.resource.Resource;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class MessageUtils {
    public static void sendActionBar(Player player, String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    public static void sendBossBarTick(Player p, Integer time, BarColor color, BarStyle style) {
        BossBar bossbar = Bukkit.createBossBar("制作时间还有：", color, style);
        bossbar.addPlayer(p);

        Bukkit.getScheduler().runTaskTimer(FacilityProp.instance, new Runnable() {
            Integer realTime = time / 20;
            @Override
            public void run() {
                if (realTime < 0) {
                    bossbar.removePlayer(p);
                } else {
                    updateBossBarTitle(bossbar, realTime, time / 20);
                }

                realTime--;
            }
        }, 0, 15L);
    }

    public static void updateBossBarTitle(BossBar bossbar, Integer time, Integer total) {
        bossbar.setTitle(HexCodeUtils.translateHexCodes(Resource.getHexCode(HexCodeUtils.HexCode.BOSSBAR) + "制作时间还有："  + time + " 秒"));
        bossbar.setProgress((float) time / (float) total);
        FacilityProp.instance.getLogger().info(String.valueOf((float) time / (float) total));
    }
}
