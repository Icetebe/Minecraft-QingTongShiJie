package cn.mclover.mc.Water;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main extends JavaPlugin implements Listener {
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		waterLimit = this.getConfig().getInt("WaterLimit", 10);
		lavaLimit = this.getConfig().getInt("LavaLimit", 6);
		lavaNetherLimit = this.getConfig().getInt("LavaNetherLimit", 15);
		this.saveConfig();
		getServer().getLogger().log(Level.INFO, "小桥流水已开启！作者 Wahoo");
	}

	private int waterLimit = 10;
	private int lavaLimit = 5;
	private int lavaNetherLimit = 15;
	private int otherLimit = 5;
	private int otherNetherLimit = 10;
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onForbidWaterFall(BlockFromToEvent e) {
		Block to = e.getToBlock();
		Block fr = e.getBlock();
		//Bukkit.getLogger().log(Level.SEVERE,"block from:"+fr.getType().toString()+"("+fr.getX()+","+fr.getY()+","+fr.getZ()+")"
		//		+" to:"+to.getType().toString()+"("+to.getX()+","+to.getY()+","+to.getZ()+")");
		if ((fr.getType() == Material.STATIONARY_WATER || fr.getType() == Material.WATER) && (to.getType() == Material.AIR)) {
			int i = 1;
			while (i < waterLimit) {
				Material t = e.getBlock().getWorld().getBlockAt(to.getLocation().getBlockX(), to.getLocation().getBlockY() - i,
						to.getLocation().getBlockZ()).getType();
				if (t == Material.AIR||t == Material.WATER||t == Material.LAVA) {
					i++;
				} else {
					//Bukkit.getLogger().log(Level.SEVERE,"allow flow i="+i+"  block:"+t.toString());
					return;
				}
			}
			e.setCancelled(true);
		} else if ((fr.getType() == Material.STATIONARY_LAVA|| fr.getType() == Material.LAVA) && (to.getType() == Material.AIR)) {
			int i = 1;
			int limit = lavaLimit;
			if (e.getBlock().getWorld().getName().contains("nether")) {
				limit = lavaNetherLimit;
			}
			while (i < limit) {
				Material t = e.getBlock().getWorld().getBlockAt(to.getLocation().getBlockX(), to.getLocation().getBlockY() - i,
						to.getLocation().getBlockZ()).getType();
				if (t == Material.AIR||t == Material.WATER||t == Material.LAVA) {
					i++;
				}else {
					// System.out.println("allow fall i="+i);
					return;
				}
			}
			e.setCancelled(true);
		}else if((fr.getType() == Material.AIR)
				&&(to.getType() == Material.AIR)){
			e.setCancelled(true);
		}
	}
	
}