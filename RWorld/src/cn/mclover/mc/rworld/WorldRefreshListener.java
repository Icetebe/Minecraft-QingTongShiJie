package cn.mclover.mc.rworld;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/*import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;*/
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

/*import multiworld.MultiWorldPlugin;
import multiworld.api.MultiWorldWorldData;
import multiworld.api.flag.FlagName;
import multiworld.data.InternalWorld;
import multiworld.data.WorldContainer;*/

public class WorldRefreshListener implements Listener {
	private MultiverseCore multicore;
	/*private MultiWorldPlugin multiWorld;
	private LWC lwc;*/
	private Main main;

	private String[] rule; 
	private String[] rst;
	
	public WorldRefreshListener(Main main) {
		this.main = main;
		link(main.getServer().getPluginManager().getPlugin("Multiverse-Core"));
		//link(main.getServer().getPluginManager().getPlugin("MultiWorld"));
	}
	
	private void getGameRule(World world){
		if(world==null)
			return;
		String[] gamerules = world.getGameRules();
		rule = new String[gamerules.length];
		rst = new String[gamerules.length];
		for(int i=0;i<gamerules.length;i++){
			rule[i] = gamerules[i];
			rst[i] = world.getGameRuleValue(rule[i]);
		}
	}
	
	private void setGameRule(World world){
		if(world==null)
			return;
		if(rule!=null&&rst!=null){
			for(int i=0;i<rule.length;i++){
				world.setGameRuleValue(rule[i], rst[i]);
			}
		}
	}

	private void r(){
		if (multicore != null) {
			Iterator<String> it = ConfigParam.RefleshWorld.iterator();
			while (it.hasNext()) {
				String world = it.next();
				getGameRule(Bukkit.getWorld(world));
				boolean flag = multicore.getMVWorldManager().regenWorld(world, ConfigParam.ChangeSeed, true, null);
				if (flag) {
					if(ConfigParam.ChangeSeed){
						MultiverseWorld w = multicore.getMVWorldManager().getMVWorld(world);
						w.setSpawnLocation(multicore.getSafeTTeleporter().getSafeLocation(w.getSpawnLocation(),20,20));
					}
					setGameRule(multicore.getMVWorldManager().getMVWorld(world).getCBWorld());
					ConfigParam.ReTime = new Date().getTime();
					ConfigParam.save(main);
					/*if(lwc!=null&&ConfigParam.CleanLWC){
						List<Protection> list = lwc.getPhysicalDatabase().loadProtections();
						for(Protection p:list){
							if(p.getWorld().equalsIgnoreCase(world)){
								lwc.getDatabaseThread().removeProtection(p);
							}
						}
						L.info("LWC清理完成");
					}*/
					L.info("世界:" + world + "重置成功！使用multicore.");
					if(ConfigParam.setUnDrop){
						Bukkit.getWorld(world).setGameRuleValue("keepInventory", "true");
					}
				}
			}
		}/*else if(multiWorld != null) {
			
			Iterator<String> it = ConfigParam.RefleshWorld.iterator();
			while (it.hasNext()) {
				String worldName = it.next();
				World world = main.getServer().getWorld(worldName);
				if(world==null){
					continue;
				}
				getGameRule(world);
				File file = world.getWorldFolder();
				boolean flag = multiWorld.getDataManager().getWorldManager().unloadWorld(worldName, true);

				if(flag){
					if(FileUtils.deleteFolder(file)){
						if(ConfigParam.ChangeSeed){
							MultiWorldWorldData mwwd = multiWorld.getApi().getWorld(worldName);
							WorldContainer wc = multiWorld.getDataManager().getWorldManager().getWorldMeta(worldName, false);
							InternalWorld iw = wc.getWorld();
							WorldCreator creator = WorldCreator.name(iw.getName()).seed(new Random().nextLong()).environment(iw.getEnv());
							World bukkitWorld = Bukkit.createWorld(creator);
							if(bukkitWorld!=null){
								wc.setLoaded(Bukkit.getWorld(worldName) != null);
								bukkitWorld.setDifficulty(Difficulty.getByValue(iw.getDifficulty()));
								setGameRule(bukkitWorld);
								try {
									wc.setOptionValue(FlagName.CREATIVEWORLD, mwwd.getOptionValue(FlagName.CREATIVEWORLD));
									wc.setOptionValue(FlagName.PVP, mwwd.getOptionValue(FlagName.PVP));
									wc.setOptionValue(FlagName.RECIEVECHAT, mwwd.getOptionValue(FlagName.RECIEVECHAT));
									wc.setOptionValue(FlagName.REMEMBERSPAWN, mwwd.getOptionValue(FlagName.REMEMBERSPAWN));
									wc.setOptionValue(FlagName.SAVEON, mwwd.getOptionValue(FlagName.SAVEON));
									wc.setOptionValue(FlagName.SENDCHAT, mwwd.getOptionValue(FlagName.SENDCHAT));
									wc.setOptionValue(FlagName.SPAWNANIMAL, mwwd.getOptionValue(FlagName.SPAWNANIMAL));
									wc.setOptionValue(FlagName.SPAWNMONSTER, mwwd.getOptionValue(FlagName.SPAWNMONSTER));
								} catch (Exception e) {
									L.error(e.getMessage());
								}
							}
						}else{
							//multiWorld.getApi().isWorldLoaded(worldName);
							multiWorld.getDataManager().getWorldManager().loadWorld(worldName);
						}
						if(lwc!=null&&ConfigParam.CleanLWC){
							List<Protection> list = lwc.getPhysicalDatabase().loadProtections();
							for(Protection p:list){
								if(p.getWorld().equalsIgnoreCase(world.getName())){
									lwc.getDatabaseThread().removeProtection(p);
								}
							}
							L.info("LWC清理完成");
						}
						ConfigParam.ReTime = new Date().getTime();
						ConfigParam.save(main);
						L.info("世界:" + worldName + "重置成功！使用multiWorld.");
						if(ConfigParam.setUnDrop){
							Bukkit.getWorld(world.getName()).setGameRuleValue("keepInventory", "true");
						}
					}
				}
			}
		}else{
			Iterator<String> it = ConfigParam.RefleshWorld.iterator();
			while (it.hasNext()) {
				String worldName = it.next();
				World world = main.getServer().getWorld(worldName);
				if(world==null){
					continue;
				}
				Environment env = world.getEnvironment();
				getGameRule(world);
				File file = world.getWorldFolder();
				if(Bukkit.unloadWorld(worldName, true)){
					if(FileUtils.deleteFolder(file)){
						WorldCreator creator = WorldCreator.name(worldName).seed(new Random().nextLong()).environment(env);
						World bukkitWorld = Bukkit.createWorld(creator);
						setGameRule(bukkitWorld);
					}
				}
				if(lwc!=null&&ConfigParam.CleanLWC){
					List<Protection> list = lwc.getPhysicalDatabase().loadProtections();
					for(Protection p:list){
						if(p.getWorld().equalsIgnoreCase(world.getName())){
							lwc.getDatabaseThread().removeProtection(p);
						}
					}
					L.info("LWC清理完成");
				}
				L.info("世界:" + worldName + "重置成功！使用bukkit.");
				if(ConfigParam.setUnDrop){
					Bukkit.getWorld(world.getName()).setGameRuleValue("keepInventory", "true");
				}
			}
		}*/
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Bukkit.getOnlinePlayers().size() > 1) {
			return;
		}
		long time = new Date().getTime() - ConfigParam.ReTime;
		if (time < 1000 * 60 * 60 * ConfigParam.MinHours || ConfigParam.RefleshWorld.size() < 1) {
			return;
		}
		r();
		
		
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		Plugin p = event.getPlugin();
		link(p);
	}

	private void link(Plugin p) {
		if (p != null && p.getDescription().getName().equalsIgnoreCase("Multiverse-Core")) {
			multicore = ((MultiverseCore) p);
			L.info("Multiverse 2 插件启动!");
			return;
		}
		/*if (p != null && p.getDescription().getName().equalsIgnoreCase("MultiWorld")) {
			multiWorld = MultiWorldPlugin.getInstance();
			L.info("MultiWorld 插件启动!");
		}
		if (p != null && p.getDescription().getName().equalsIgnoreCase("LWC")) {
			lwc = LWC.getInstance();
			L.info("LWC 插件关联!");
		}*/
	}

}