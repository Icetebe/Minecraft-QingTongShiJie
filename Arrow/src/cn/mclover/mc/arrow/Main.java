package cn.mclover.mc.arrow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private ArrowListener al;
	private CraftListener cl;
	
	@Override
	public void onEnable() {
		L.setLog(getLogger());
		al = new ArrowListener(this);
		cl = new CraftListener();
		this.getServer().getPluginManager().registerEvents(al, this);
		this.getServer().getPluginManager().registerEvents(cl, this);
		L.info("成功启动！");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if(!sender.isOp()){
			sender.sendMessage(ChatColor.GREEN + "➹➹版本:穿云箭v1.21➹➹➹作者：锄禾当午游戏组 wahoo");
			return false;
		}
		if(args.length==0){
			sender.sendMessage(ChatColor.GREEN + "➹➹版本:穿云箭v1.21➹➹➹➹➹➹➹➹➹➹➹➹➹➹➹➹");
			sender.sendMessage(ChatColor.GOLD  + "➹/arrow give [name] <num> 给某人x支穿云箭");
			sender.sendMessage(ChatColor.GOLD  + "➹/arrow pgive [name] <num> 给某人x个穿云箭碎片");
			sender.sendMessage(ChatColor.GOLD  + "➹/arrow reset 重置插件-删除当前江湖任务");
			sender.sendMessage(ChatColor.GOLD  + "➹/arrow reload 重载插件");
			sender.sendMessage(ChatColor.GREEN + "➹➹作者:锄禾当午游戏组  wahoo➹➹➹➹➹➹➹➹➹➹➹➹➹➹");
			return true;
		}else if(args.length==1){
			if(args[0].equalsIgnoreCase("reset")){
				al.reset();
				return true;
			}else if(args[0].equalsIgnoreCase("reload")){
				onReload();
				return true;
			}
		}else if(args.length>=2){
			if(args[0].equalsIgnoreCase("give")){
				Player p = Bukkit.getPlayer(args[1]);
				int num = 1;
				if(args.length==3){
					try{
						num = Integer.parseInt(args[2]);
					}catch(Exception e){}
				}
				ArrowItem.getArrow(p,num);
				return true;
			}else if(args[0].equalsIgnoreCase("pgive")){
				Player p = Bukkit.getPlayer(args[1]);
				int num = 1;
				if(args.length==3){
					try{
						num = Integer.parseInt(args[2]);
					}catch(Exception e){}
				}
				ArrowItem.getPiece(p,num);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onDisable() {
		al.reset();
		L.info("成功关闭！");
		L.setLog(null);
	}
	
	public void onReload() {
		onDisable();
		onEnable();
		L.info("重启完毕！");
	}
}
