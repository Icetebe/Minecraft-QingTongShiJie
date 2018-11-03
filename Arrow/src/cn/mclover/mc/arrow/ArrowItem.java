package cn.mclover.mc.arrow;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ArrowItem {
	
	public static String name = ChatColor.GOLD+"➹➹一支穿云箭➹➹";
	public static List<String> args = new ArrayList<String>(7);
	
	public static String pname = ChatColor.GOLD+"➹➹穿云箭碎片➹➹";
	public static List<String> pargs = new ArrayList<String>(7);
	
	
	private static FireworkEffect effect = FireworkEffect.builder().flicker(true)
    							.withColor(Color.RED)
    							.withFade(Color.ORANGE)
    							.with(FireworkEffect.Type.CREEPER)
    							.trail(true)
    							.build();
	
	static{
		args.add(0,ChatColor.GREEN +     "➹➹➹➹➹➹➹➹➹➹➹");
		args.add(1,ChatColor.GOLD +      "➹【一支穿云箭】➹");
		args.add(2,ChatColor.GOLD +      "➹千军万马来相见➹");
		args.add(3,ChatColor.GREEN +     "➹➹➹➹➹➹➹➹➹➹➹");
		args.add(4,ChatColor.DARK_PURPLE+"➹右键敌军或野怪➹");
		args.add(5,ChatColor.RED +       "➹七名侠客帮你战➹");
		args.add(6,ChatColor.GREEN +     "➹➹➹➹➹➹➹➹➹➹➹");
		
		pargs.add(0,ChatColor.GREEN +     "➹➹➹➹➹➹➹➹➹➹➹");
		pargs.add(1,ChatColor.GOLD +      "➹【穿云箭碎片】➹");
		pargs.add(2,ChatColor.GOLD +      "➹请手持七枚碎片➹");
		pargs.add(2,ChatColor.GOLD +      "➹右键地板即合成➹");
		pargs.add(3,ChatColor.GREEN +     "➹➹➹➹➹➹➹➹➹➹➹");
		pargs.add(4,ChatColor.DARK_PURPLE+"➹完成穿云箭任务➹");
		pargs.add(5,ChatColor.RED +       "➹有机会获得碎片➹");
		pargs.add(6,ChatColor.GREEN +     "➹➹➹➹➹➹➹➹➹➹➹");
	}

	public static void getArrow(Player p, int num){
		ItemStack it = new ItemStack(Material.ARROW,num);
		it.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 5);
		//it.setDurability((short) 20);
		ItemMeta im = it.hasItemMeta() ? it.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.ARROW);
		im.setDisplayName(name);
		im.setLore(args);
		it.setItemMeta(im);
		p.getInventory().addItem(it);
	}
	
	public static void getPiece(Player p, int num){
		ItemStack it = new ItemStack(Material.FEATHER,num);
		it.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 5);
		//it.setDurability((short) 20);
		ItemMeta im = it.hasItemMeta() ? it.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.FEATHER);
		im.setDisplayName(pname);
		im.setLore(pargs);
		it.setItemMeta(im);
		p.getInventory().addItem(it);
	}
	
	public static void callFireworks(Player player){
		Firework fw = (Firework)player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Firework.class);
		FireworkMeta fm = fw.getFireworkMeta();
        fm.addEffect(effect);
        fm.setPower(2);
        fw.setFireworkMeta(fm);
	}
	
	public static boolean isFeacher(ItemStack is){
		if(is==null){
			return false;
		}
		if(Material.FEATHER.equals(is.getType())){
			if(pname.equals(is.getItemMeta().getDisplayName())){
				boolean flag = true;
				for(int i=0;i<pargs.size();i++){
					if(!pargs.get(i).equals(is.getItemMeta().getLore().get(i))){
						flag = false;
					}
				}
				return flag;
			}
		}
		return false;
	}
	
	public static boolean isArrow(ItemStack is){
		if(is==null){
			return false;
		}
		if(Material.ARROW.equals(is.getType())){
			if(name.equals(is.getItemMeta().getDisplayName())){
				boolean flag = true;
				for(int i=0;i<args.size();i++){
					if(!args.get(i).equals(is.getItemMeta().getLore().get(i))){
						flag = false;
					}
				}
				return flag;
			}
		}
		return false;
	}
}
