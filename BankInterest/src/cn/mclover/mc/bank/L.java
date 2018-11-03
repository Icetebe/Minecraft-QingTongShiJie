/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package cn.mclover.mc.bank;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;

public class L
{

    public L()
    {
    }

    public static void setLog(Logger l)
    {
        log = l;
    }

    public static void info(String msg)
    {
        log.log(Level.INFO, (new StringBuilder(String.valueOf(prefix))).append(msg).toString());
    }

    public static void warn(String msg)
    {
        log.log(Level.WARNING, (new StringBuilder(String.valueOf(prefix))).append(msg).toString());
    }

    public static void error(String msg)
    {
        log.severe((new StringBuilder(String.valueOf(prefix))).append(msg).toString());
    }

    private static Logger log;
    private static String prefix;

    static 
    {
        prefix = (new StringBuilder()).append(ChatColor.GOLD).append("[银行]").toString();
    }
}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\Users\Leon\Desktop\BankInterest.jar
	Total time: 91 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/