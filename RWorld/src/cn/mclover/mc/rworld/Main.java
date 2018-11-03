package cn.mclover.mc.rworld;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	@Override
	public void onEnable() {
		L.setLog(getLogger());
		ConfigParam.load(this);
		WorldRefreshListener refreshListener = new WorldRefreshListener(this);
		this.getServer().getPluginManager().registerEvents(refreshListener, this);
		
		L.info("成功启动");
	}

	public void reload() {
	
		L.info("重载完成");
	}

	@Override
	public void onDisable() {
		L.info("已关闭");
	}
}
