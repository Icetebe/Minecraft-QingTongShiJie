package cn.mclover.mc.rworld;


import java.util.logging.Level;
import java.util.logging.Logger;


public class L {
	private static Logger log;
	private static String prefix = "[RW]";
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
