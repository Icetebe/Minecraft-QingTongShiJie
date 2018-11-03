/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   InterestMain.java

package cn.mclover.mc.bank;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

// Referenced classes of package com.chuhedangwu.bank:
//            L

public class InterestMain extends JavaPlugin
    implements Listener
{

    public InterestMain()
    {
        cooldown = new HashMap();
    }

    public void onEnable()
    {
        L.setLog(getLogger());
        if(!setupEconomy())
        {
            L.info("未找到Vault经济插件，插件关闭！");
            onDisable();
            return;
        } else
        {
            config = getConfig();
            getServer().getPluginManager().registerEvents(this, this);
            L.info("成功启动！");
            return;
        }
    }

    public void onDisable()
    {
        saveConfig();
        cooldown.clear();
        L.info("成功关闭！");
        L.setLog(null);
    }

    public void onReload()
    {
        onDisable();
        onEnable();
        L.info("重启完毕！");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e)
    {
    	final Player player = e.getPlayer();
    	Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(player==null || !player.isOnline())return;
				long now = (new Date()).getTime();
		        long preLogin = config.getLong((new StringBuilder("its.")).append(player.getName().toLowerCase()).append(".time").toString(), 0L);
		        if(now - preLogin > 79200000L)
		        {
		            String ip = player.getAddress().getHostString();
		            Long time = (Long)cooldown.get(ip);
		            if(time != null)
		            {
		                if(now - time.longValue() < 900000L)
		                {
		                    player.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("[银行]您今天已领过利息！").toString());
		                    return;
		                }
		                cooldown.remove(ip);
		            }
		            double money = config.getDouble((new StringBuilder("its.")).append(player.getName().toLowerCase()).append(".money").toString(), 0.0D);
		            if(money > 0.0D)
		            {
		                earnInterests(player, money);
		                config.set((new StringBuilder("its.")).append(player.getName().toLowerCase()).append(".time").toString(), Long.valueOf(now));
		                saveConfig();
		            } else
		            {
		                player.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("[银行]无存款记录,今天无利息发放！").toString());
		            }
		        }
			}
		}, 400l);
    }

    public void playerQuitListen(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        config.set((new StringBuilder("its.")).append(player.getName().toLowerCase()).append(".money").toString(), Double.valueOf(economy.getBalance(player.getName())));
        saveConfig();
        return;
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if(economyProvider != null)
            economy = (Economy)economyProvider.getProvider();
        return economy != null;
    }

    public EconomyResponse earnInterests(Player p, double m)
    {
        if(p == null)
            return null;
        int ints = 0;
        double money = m;
        for(int i = border.length - 1; i >= 0; i--)
            if(money - (double)border[i] > 0.0D)
            {
                ints = (int)((double)ints + (money - (double)border[i]) * rate[i]);
                money -= money - (double)border[i];
            }

        money = m;
        int fees = 0;
        for(int i = tborder.length - 1; i >= 0; i--)
            if(money - (double)tborder[i] > 0.0D)
            {
                fees = (int)((double)fees + (money - (double)tborder[i]) * tax[i]);
                money -= money - (double)tborder[i];
            }

        p.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("[银行]你的存款").append(m).append("元，本次获得利息收益").append(ChatColor.RED).append(ints).append(ChatColor.YELLOW).append("元,缴纳手续费").append(ChatColor.GRAY).append(fees).append(ChatColor.YELLOW).append("元.").toString());
        p.sendMessage((new StringBuilder()).append(ChatColor.GRAY).append("[银行]你的存款在每天登录时会产生收益~记得登录哦~").toString());
        if(fees < ints)
            return economy.bankDeposit(p.getName(), ints - fees);
        if(fees > ints)
            return economy.bankWithdraw(p.getName(), fees - ints);
        else
            return economy.bankBalance(p.getName());
    }

    private Economy economy;
    private FileConfiguration config;
    private HashMap cooldown;
    public static final int border[] = {
        0, 10000, 100000, 200000, 500000, 800000, 1000000, 2000000
    };
    public static final double rate[] = {
        0.01D, 0.0040000000000000001D, 0.002D, 0.001D, 0.00080000000000000004D, 0.00040000000000000002D, 0.00020000000000000001D, 0.0001D
    };
    public static final int tborder[] = {
        10000, 100000, 200000, 500000, 800000, 1000000, 1500000, 2000000, 2500000
    };
    public static final double tax[] = {
        0.0001D, 0.00020000000000000001D, 0.00059999999999999995D, 0.00080000000000000004D, 0.00080000000000000004D, 0.001D, 0.00050000000000000001D, 0.0011999999999999999D, 0.0030000000000000001D
    };

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\Users\Leon\Desktop\BankInterest.jar
	Total time: 88 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/