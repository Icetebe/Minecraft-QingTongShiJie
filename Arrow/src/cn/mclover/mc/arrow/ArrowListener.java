package cn.mclover.mc.arrow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArrowListener implements Listener{
	private Main main;
	private static Random r = new Random(new Date().getTime());
	public boolean waiting = false;
	private boolean success = false;
	private Entity e = null;
	private Player p = null;
	private String targetName = null;
	public LinkedList<PlayerAndLoc> list = new LinkedList<PlayerAndLoc>();
	public Location l;
	public String format = ChatColor.GOLD+"[➹一支穿云箭➹]";
	public String target;
	private PotionEffect effect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15*20, 4);
	private Integer id;
	
	public ArrowListener(Main main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void arrow(PlayerInteractEntityEvent event) {
		if(event.isCancelled()){
			return;
		}
		p = event.getPlayer();
		if(p==null){
			return;
		}
		ItemStack it = p.getItemInHand();
		if(it==null){
			return;
		}
		if (!ArrowItem.isArrow(it)) {
			return;
		}
		if (it.getAmount()<1){
			return;
		}
		//fail
		if(waiting){
			p.sendMessage(format+ChatColor.RED+"你的令箭发射的太晚了，江湖中已无人回应！");
			return;
		}
		//deal
		if (it.getAmount() > 1) {
			it.setAmount(it.getAmount() - 1);
		} else {
			p.getInventory().remove(it);
		}
		//success
		waiting = true;
		success = false;
		
		
		e = event.getRightClicked();
		if(e instanceof Player){
			target = "集火攻击："+ChatColor.RED+((Player)e).getName().toString();
			targetName = ((Player)e).getName().toString();
			l = e.getLocation();
		}else if(e instanceof LivingEntity){
			target = "斩杀目标："+ChatColor.DARK_PURPLE+e.getType().toString();
			l = e.getLocation();
		}else{
			target = "";
			e = null;
			l = p.getLocation();
		}
	
		list = random(7);
		ArrowItem.callFireworks(p);
		Bukkit.broadcastMessage(format+p.getDisplayName()+"发出了江湖救援令➹一支穿云箭,千军来相见➹\n请收到提示的玩家准备前去支援，成功击杀目标可获奖励~");
		for(PlayerAndLoc pal:list){
			pal.p.sendMessage("");
			pal.p.sendMessage("");
			pal.p.sendMessage(format+"你收到了来自 ["+p.getDisplayName()+"] 的江湖任务，你将在5秒内被传送至任务附近，成功击杀可获得奖励。"+target);
			pal.p.sendMessage(format+"请注意"+target);
			pal.p.sendMessage("");
			pal.p.sendMessage("");
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				LinkedList<PlayerAndLoc> temp = new LinkedList<PlayerAndLoc>();
				for(int i=0;i<list.size();i++){
					PlayerAndLoc pal = list.get(i);
					if(pal.p.isOnline()){
						if(pal.p.isDead()){
							temp.add(pal);
							pal.p.sendMessage(format+"你在支援路上意外身亡，丧失任务资格。");
						}else{
							pal.p.sendMessage(format+"你已被附加强大BUFF");
							pal.p.sendMessage(format+target+ChatColor.GOLD+"    重复一遍"+target);
							pal.p.sendMessage(format+"成功击杀可获得奖励。15秒后支援结束，你将被传送回到原位置。");
							pal.p.teleport(l);
							pal.p.addPotionEffect(effect);
						}
					}
				}
				p.sendMessage(format+list.size()+"位侠客正在披星戴月的赶来."+(temp.size()>0?""+temp.size()+"位侠客在途中不治身亡！":""));
				while(!temp.isEmpty()){
					list.remove(temp.poll());
				}
				
			}
		}, 4*20);
		
		id = Integer.valueOf(Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			public void run() {
				if(e.isDead()){
					success = true;
					stop();
				}
				if(!p.isOnline()||p.isDead()){
					stop();
				}
			}
		}, 5*20,1*20));
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				stop();
				while(!list.isEmpty()){
					PlayerAndLoc pal = list.poll();
					
					if(pal.p.isOnline()){
						if(success){
							int ran = r.nextInt(100);
							if(ran>70){
								ArrowItem.getPiece(pal.p, 1);
								pal.p.sendMessage(format+"江湖支援任务胜利完成，你获得穿云箭碎片一枚！");
							}else{
								int exp = r.nextInt(14)+16;
								pal.p.giveExp(exp);
								pal.p.sendMessage(format+"江湖支援任务胜利完成，你获得经验"+exp+"点！");
							}
						}else{
							pal.p.sendMessage(format+"江湖支援任务失败！");
						}
						if(pal.p.isDead()){
							pal.p.sendMessage(format+"啊哦，你在支援战斗中身亡。");
						}else{
							pal.p.teleport(pal.l);
							pal.p.sendMessage(format+"千军来相见➹江湖支援任务结束➹");
						}
					}
				}
				if(success){
					p.sendMessage(format+"主力支援已胜利凯旋。");
				}else{
					p.sendMessage(format+"主力支援没能帮到你，打道回府了。");
				}
				reset();
			}
		}, 18*20);
	}
	
	public LinkedList<PlayerAndLoc> random(int num){
		if(num<1){
			return new LinkedList<PlayerAndLoc>(); 
		}
		LinkedList<PlayerAndLoc> tlist = new LinkedList<PlayerAndLoc>();
		//players[] = null;
		Collection<? extends Player> collection = Bukkit.getOnlinePlayers();
		Player[] players = collection.toArray(new Player[collection.size()]);
		//Player players[] = (Player[])Bukkit.getOnlinePlayers().toArray();
		if(players.length<=num+1){
			for(int i=0;i<players.length;i++){
				if(!players[i].isOp()){
					if(targetName!=null){
						if(players[i].getName().toString().equalsIgnoreCase(targetName)){
							continue;
						}
					}
					if(players[i].getName().toString().equalsIgnoreCase(p.getName().toString())){
						continue;
					}
					tlist.add(new PlayerAndLoc(players[i], players[i].getLocation()));
				}
			}
		}else{
			List<String> temp = new ArrayList<>();
			while(temp.size()<num){
				Player player;
				do{
					player = players[r.nextInt(players.length)];
				}while(temp.contains(player.getName()));
				temp.add(player.getName());
				if(!player.isOp() && !player.getName().toString().equalsIgnoreCase(targetName)&& !player.getName().toString().equalsIgnoreCase(p.getName())){
					tlist.add(new PlayerAndLoc(player, player.getLocation()));
				}
			}
		}
		return tlist;
	}
	
	public void stop(){
		if(id!=null){
			Bukkit.getScheduler().cancelTask(id.intValue());
            id = null;
		}
	}
	
	public void reset(){
		stop();
		waiting = false;
		success = false;
		e = null;
		p = null;
		targetName = null;
		list = new LinkedList<PlayerAndLoc>();
		l = null;
	}
	
	class PlayerAndLoc{
		public Player p;
		public Location l;
		public PlayerAndLoc(Player p,Location l){
			this.p = p;
			this.l = l;
		}
	}
}
