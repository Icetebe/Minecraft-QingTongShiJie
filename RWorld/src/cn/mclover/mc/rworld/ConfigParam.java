package cn.mclover.mc.rworld;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigParam {
	private static String[] worldList = { 
			"tempworld"
	};
	public static List<String> RefleshWorld = Arrays.asList(worldList);
	public static long ReTime = 0;
	public static int MinHours = 48;
	public static boolean ChangeSeed = true;
	public static boolean CleanLWC = false;
	public static boolean setUnDrop = true;

	@SuppressWarnings("unchecked")
	public static boolean load(JavaPlugin p) {
		try {
			FileConfiguration config = p.getConfig();
			RefleshWorld = (List<String>) config.getList("RefleshWorld", RefleshWorld);
			ReTime = config.getLong("ReTime",ReTime);
			MinHours = config.getInt("MinHours",MinHours);
			ChangeSeed = config.getBoolean("ChangeSeed",ChangeSeed);
			CleanLWC = config.getBoolean("CleanLWC",CleanLWC);
			setUnDrop = config.getBoolean("setUnDrop",setUnDrop);
			
			config.set("ReTime", ReTime);
			config.set("ChangeSeed", ChangeSeed);
			config.set("MinHours", MinHours);
			config.set("RefleshWorld", RefleshWorld);
			config.set("CleanLWC", CleanLWC);
			config.set("setUnDrop", setUnDrop);
			
			p.saveConfig();
			return true;
		} catch (Exception e) {
			L.error(e.getMessage());
			return false;
		}
	}
	public static boolean save(JavaPlugin p){
		try {
			FileConfiguration config = p.getConfig();
			config.set("ReTime", ReTime);
			config.set("RefleshWorld", RefleshWorld);
			p.saveConfig();
			return true;
		} catch (Exception e) {
			L.error(e.getMessage());
			return false;
		}
	}
}
