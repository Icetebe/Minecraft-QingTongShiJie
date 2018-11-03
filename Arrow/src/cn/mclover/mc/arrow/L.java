package cn.mclover.mc.arrow;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;


public class L {
	private static Logger log;
	private static String prefix = ChatColor.GOLD+"➹穿云箭➹";
	public static void setLog(Logger l){
		log = l;
	}
	public static void info(String msg){
		log.log(Level.INFO, prefix+msg);
		
	}
	public static void warn(String msg){
		log.log(Level.WARNING, prefix+msg);
	}
	public static void error(String msg){
		log.severe(prefix+msg);
	}

}
