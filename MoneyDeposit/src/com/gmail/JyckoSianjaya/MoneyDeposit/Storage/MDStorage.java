package com.gmail.JyckoSianjaya.MoneyDeposit.Storage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Renderer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapFont.CharacterSprite;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.map.MinecraftFont;

import com.gmail.JyckoSianjaya.MoneyDeposit.MoneyDeposit;
import com.gmail.JyckoSianjaya.MoneyDeposit.Images.ImageData;
import com.gmail.JyckoSianjaya.MoneyDeposit.Images.ImageDataValue;
import com.gmail.JyckoSianjaya.MoneyDeposit.Images.ImageRunnable;
import com.gmail.JyckoSianjaya.MoneyDeposit.Images.ImageTask;
import com.gmail.JyckoSianjaya.Utilities.Utility;
import com.gmail.JyckoSianjaya.Utilities.XMaterial;
import com.gmail.JyckoSianjaya.Utilities.NBT.NBTItem;

public final class MDStorage {
	private static MDStorage instance;
	private HashMap<Message, List<String>> messages = new HashMap<Message, List<String>>();
	private ItemStack deposit;
	private ItemStack depositspecific;
	private BufferedImage templatedeposit;
	private BufferedImage templatespecificdeposit;
	private ImageData depositdata;
	private ImageData specificdepositdata;
	private int MinimumDeposit = 0;
	private int MaximumDeposit = 0;
	private MDStorage() {
		FileConfiguration config = MoneyDeposit.getInstance().getConfig();
		config.options().copyDefaults(true);
		MoneyDeposit.getInstance().saveConfig();
		loadConfig();
		
	}
	public final List<String> getMessage(final Message me) {
		return messages.get(me);
	}
	public final int getMinDeposit() { return MinimumDeposit; }
	public final int getMaxDeposit() { return MaximumDeposit; }
	public final static MDStorage getInstance() {
		if (instance == null) instance = new MDStorage();
		return instance;
	}
	public final void loadConfig() {
		FileConfiguration config = MoneyDeposit.getInstance().getConfig();
		config.options().copyDefaults(true);
		MoneyDeposit.getInstance().saveConfig();
		MoneyDeposit.getInstance().reloadConfig();
		config = MoneyDeposit.getInstance().getConfig();
		MinimumDeposit = config.getInt("minimumDeposit");
		MaximumDeposit = config.getInt("maximumDeposit");
		messages.put(Message.DEPOSIT, Utility.TransColor(config.getStringList("depositMessage")));
		messages.put(Message.DEPOSIT_SPECIFIC, Utility.TransColor(config.getStringList("depositMessageSpecific")));
		messages.put(Message.WITHDRAWN, Utility.TransColor(config.getStringList("withdrawMessage")));
		deposit = new ItemStack(XMaterial.FILLED_MAP.parseItem());
		ItemMeta depometa = deposit.getItemMeta();
		depometa.setDisplayName(Utility.TransColor(config.getString("item_look.name")));
		depometa.setLore(Utility.TransColor(config.getStringList("item_look.lores")));
		deposit.setItemMeta(depometa);
		depositspecific = deposit.clone();
		
		ItemMeta depospmeta = depositspecific.getItemMeta();
		depospmeta.setDisplayName(Utility.TransColor(config.getString("item_look_specific.name")));
		depospmeta.setLore(Utility.TransColor(config.getStringList("item_look_specific.lores")));
		depositspecific.setItemMeta(depospmeta);
		World w = null;
		for (World wld : Bukkit.getWorlds()) {
			w = wld;
			break;
		}
		MapView view = Bukkit.createMap(w);
		view.getRenderers().clear();
		view.setScale(Scale.FARTHEST);
		try {
		templatedeposit = ImageIO.read(new URL(config.getString("normal_deposit.picture")));
		
		} catch (IOException e) {
			Utility.sendConsole("&c<> &4OOPS! &7Can't read Deposit Normal Image! Is the url fine?");
		}
		templatedeposit = MapPalette.resizeImage(templatedeposit);
		try {
		templatespecificdeposit = ImageIO.read(new URL(config.getString("specific_deposit.picture")));
		
		} catch (IOException e) {
			Utility.sendConsole("&c<> &4OOPS! &7Can't read Deposit Normal Image! Is the url fine?");
		}
		templatespecificdeposit = MapPalette.resizeImage(templatespecificdeposit);
		view.addRenderer(new MapRenderer() {
			Boolean finished = false;
			@Override
			public void render(MapView arg0, MapCanvas arg1, Player arg2) {
				if (finished) return;
				arg1.drawImage(0, 0, getTemplateDepositImage());
				finished = true;
				// TODO Auto-generated method stub
			}
		});
		MapMeta mapm = (MapMeta) deposit.getItemMeta();
		mapm.setMapId(view.getId());
		deposit.setItemMeta(mapm);
		MapMeta mapmm = (MapMeta) depositspecific.getItemMeta();
		MapView specificview = Bukkit.createMap(w);
		specificview.getRenderers().clear();
		specificview.setScale(Scale.FARTHEST);
		specificview.addRenderer(new MapRenderer() {
			Boolean done = false;
			@Override
			public void render(MapView view, MapCanvas canvas, Player p) {
				if (done) return;
				canvas.drawImage(0, 0, getTemplateDepositSpecificImage());
				done = true;
			}
		});
		mapmm.setMapId(specificview.getId());
		depositspecific.setItemMeta(mapmm);
		int width = config.getInt("font.width");
		int height = config.getInt("font.height");
		int total = width * height;
		Boolean[] bool = new Boolean[total];
		for (int i = 0; i < total; i++) {
			bool[i] = false;
		}
		MinecraftFont font = MinecraftFont.Font;
		MapFont fonts = new MapFont();
/*		CharacterSprite sprite = null;
		try {
		sprite = new CharacterSprite(width, height, new boolean[]{false, false, false});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		try {
		sprite = new CharacterSprite(width, height, new boolean[]{false,false, false, false});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		try {
		sprite = new CharacterSprite(width, height, new boolean[]{false});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		char[] car = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r','s','t','u','v','w','x','y','z'};
		for (char c : car) {
			font.setChar(c, sprite);
		}
		char[] cap = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		for (char c : cap) {
			font.setChar(c, sprite);
		}
		*/
		depositdata = new ImageData(templatedeposit, font, new ImageDataValue(config.getInt("normal_deposit.amount.starting_x"), config.getInt("normal_deposit.amount.starting_y")), new ImageDataValue(config.getInt("normal_deposit.sender.starting_x"), config.getInt("normal_deposit.sender.starting_y")));
		specificdepositdata = new ImageData(templatespecificdeposit, font, new ImageDataValue(config.getInt("specific_deposit.amount.starting_x"), config.getInt("specific_deposit.amount.starting_y")), new ImageDataValue(config.getInt("specific_deposit.sender.starting_x"), config.getInt("specific_deposit.sender.starting_y")), new ImageDataValue(config.getInt("specific_deposit.receiver.starting_x"), config.getInt("specific_deposit.receiver.starting_y")));
		
	}
	public BufferedImage getTemplateDepositImage() { return templatedeposit; }
	public BufferedImage getTemplateDepositSpecificImage() { return templatespecificdeposit; }
	
	public ItemStack getDepositSpecificItem(String target, Player owner, int amount) {
		ItemStack clon = depositspecific.clone();
		ItemMeta dpmeta = clon.getItemMeta();
		String name = dpmeta.getDisplayName();
		name = name.replaceAll("%o", owner.getName());
		name = name.replaceAll("%m", "" + amount);
		name = name.replaceAll("%p", target);

		dpmeta.setDisplayName(name);
		ArrayList<String> lores = new ArrayList<String>();
		for (String str : dpmeta.getLore()) {
			str = str.replaceAll("%o", owner.getName());
			str = str.replaceAll("%m", "" + amount);
			str = str.replaceAll("%p", target);
			lores.add(str);
		}
		dpmeta.setLore(lores);
		clon.setItemMeta(dpmeta);
		MapView view = Bukkit.createMap(owner.getWorld());
		view.setScale(Scale.FARTHEST);
		view.getRenderers().clear();
		view.addRenderer(new MapRenderer() {
			Boolean written = false;
			@Override
			public void render(MapView view, MapCanvas arg1, Player arg2) {
				// TODO Auto-generated method stub
				if (written) return;
				written = true;
				ImageRunnable.getInstance().addTask(new ImageTask() {
					int duration = 1;
					@Override
					public void runTask() {
				ImageData specificdata = specificdepositdata;
				MinecraftFont font = specificdata.getFont();
				ImageDataValue amountvalue = specificdata.getAmountImageDataValue();
				ImageDataValue ownervalue = specificdata.getOwnerImageDataValue();
				ImageDataValue targetvalue = specificdata.getTargetImageDataValue();
				arg1.drawImage(0, 0, templatespecificdeposit);
				arg1.drawText(amountvalue.getX(), amountvalue.getY(), font, "" + amount);
				arg1.drawText(ownervalue.getX(), ownervalue.getY(), font, owner.getName());
				arg1.drawText(targetvalue.getX(), targetvalue.getY(), font, target);
					}

					@Override
					public void reduceTicks() {
						// TODO Auto-generated method stub
						duration--;
					}

					@Override
					public void reduceTicks(int toreduce) {
						// TODO Auto-generated method stub
						duration-=toreduce;
					}

					@Override
					public int getTicks() {
						// TODO Auto-generated method stub
						return duration;
					}
				});
			}
		});
		MapMeta metdd = (MapMeta) clon.getItemMeta();
		metdd.setMapId(view.getId());
		clon.setItemMeta(metdd);
		NBTItem nb = new NBTItem(clon);
		nb.setString("MDOwner", owner.getName());
		nb.setInteger("MDDeposit", amount);
		nb.setString("MDTarget", target);
		nb.setShort("map", view.getId());
		return nb.getItem();
}
	public ItemStack getDepositItem(Player owner, int amount) {
		ItemStack clon = deposit.clone();
		ItemMeta dpmeta = clon.getItemMeta();
		String name = dpmeta.getDisplayName();
		name = name.replaceAll("%o", owner.getName());
		name = name.replaceAll("%m", "" + amount);
		dpmeta.setDisplayName(name);
		ArrayList<String> lores = new ArrayList<String>();
		for (String str : dpmeta.getLore()) {
			str = str.replaceAll("%o", owner.getName());
			str = str.replaceAll("%m", "" + amount);
			lores.add(str);
		}
		dpmeta.setLore(lores);
		clon.setItemMeta(dpmeta);
		MapView view = Bukkit.createMap(owner.getWorld());
		view.setScale(Scale.FARTHEST);
		view.getRenderers().clear();
		view.addRenderer(new MapRenderer() {
			Boolean written = false;
			@Override
			public void render(MapView view, MapCanvas arg1, Player arg2) {
				// TODO Auto-generated method stub
				if (written) return;
				written = true;
				ImageRunnable.getInstance().addTask(new ImageTask() {

					int duration = 1;
					@Override
					public void runTask() {
				ImageData specificdata = depositdata;
				MinecraftFont font = specificdata.getFont();
				ImageDataValue amountvalue = specificdata.getAmountImageDataValue();
				ImageDataValue ownervalue = specificdata.getOwnerImageDataValue();
				arg1.drawImage(0, 0, templatedeposit);
				arg1.drawText(amountvalue.getX(), amountvalue.getY(), font, "" + amount);
				arg1.drawText(ownervalue.getX(), ownervalue.getY(), font, owner.getName());
					}

					@Override
					public void reduceTicks() {
						// TODO Auto-generated method stub
						duration--;
					}

					@Override
					public void reduceTicks(int toreduce) {
						// TODO Auto-generated method stub
						duration = duration-toreduce;
					}

					@Override
					public int getTicks() {
						// TODO Auto-generated method stub
						return duration;
					}
				}); 
			}
		});
		MapMeta metdd = (MapMeta) clon.getItemMeta();
		metdd.setMapId(view.getId());
		clon.setItemMeta(metdd);
		NBTItem nbt = new NBTItem(clon);
		nbt.setString("MDOwner", owner.getName());
		nbt.setInteger("MDDeposit", amount);
		nbt.setShort("map", view.getId());
		return nbt.getItem();
	}
	public enum Message {
		DEPOSIT,
		DEPOSIT_SPECIFIC,
		WITHDRAWN;
	}
}
