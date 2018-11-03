package cn.mclover.mc.arrow;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener{
	public String format = ChatColor.GOLD+"➹一支穿云箭➹";
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void craft(PlayerInteractEvent event) {
		if(event.isCancelled()){
			return;
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)||event.getAction().equals(Action.RIGHT_CLICK_AIR)){
			Player p = event.getPlayer();
			if(p!=null){
				if(ArrowItem.isFeacher(p.getItemInHand())){
					ItemStack is = p.getItemInHand();
					if(is.getAmount()>7){
						is.setAmount(is.getAmount()-7);
						ArrowItem.getArrow(p, 1);
						p.sendMessage(format+"你使用7枚碎片淬炼出➹一支穿云箭➹！");
					}else if(is.getAmount()==7){
						p.getInventory().remove(is);
						ArrowItem.getArrow(p, 1);
						p.sendMessage(format+"你使用7枚碎片淬炼出➹一支穿云箭➹！");
					}else{
						p.sendMessage(format+"碎片数量不足，无法淬炼！");
					}
				}
			}
		}
	}

}
