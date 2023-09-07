package com.github.cinnaio.facilityprop.utils;

import com.github.cinnaio.facilityprop.FacilityProp;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageUtils {
    static Integer realTime;

    public static void sendActionBar(Player player, String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    public static void sendBossBarTick(Player p, Integer time, BarColor color, BarStyle style) {
        realTime = time / 20;

        BossBar bossbar = Bukkit.createBossBar("制作时间还有：" + realTime + " 秒", color, style);
        bossbar.addPlayer(p);

        Bukkit.getScheduler().runTaskTimer(FacilityProp.instance, () -> {
            realTime--;

            if (realTime < 0) {
                bossbar.removePlayer(p);
            } else {
                updateBossBarTitle(p, bossbar, realTime);
            }
        }, 20L, 20L);
    }

    public static void updateBossBarTitle(Player p, BossBar bossbar, Integer time) {
        bossbar.setTitle("制作时间还有：" + time + " 秒");
    }
}
