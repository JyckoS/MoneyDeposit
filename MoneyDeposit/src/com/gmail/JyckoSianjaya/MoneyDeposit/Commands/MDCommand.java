package com.gmail.JyckoSianjaya.MoneyDeposit.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.JyckoSianjaya.MoneyDeposit.MoneyDeposit;
import com.gmail.JyckoSianjaya.MoneyDeposit.Storage.MDStorage;
import com.gmail.JyckoSianjaya.MoneyDeposit.Storage.MDStorage.Message;
import com.gmail.JyckoSianjaya.Utilities.Utility;

import net.milkbowl.vault.economy.Economy;

public class MDCommand implements	TabExecutor {

	@Override
	public final List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int length = args.length;
		if (length == 0 && !sender.hasPermission("moneydeposit.admin")) {
			Utility.sendMsg(sender, "&7Use &f\"/moneydeposit help\" &7for help!");
			return true;
		}
		else if (length == 0 && sender.hasPermission("moneydeposit.admin")) {
			String ver = MoneyDeposit.getInstance().getDescription().getVersion();
			Utility.sendMsg(sender, "&a&l   Money&2&lDeposit &7V" + ver + " by &a&oGober");
			Utility.sendMsg(sender, "&7Please use &f\"/moneydeposit help\" for help.");
			return true;
		}
		// TODO Auto-generated method stub
		recheck(sender, cmd, args);
		return true;
	}
	private void recheck(CommandSender snd, Command cmd, String[] args) {
		Player p = null;
		if (snd instanceof Player) p = (Player) snd;
		switch (args[0].toLowerCase()) {
		case "reload":
			if (!snd.hasPermission("moneydeposit.admin")) return;
			MoneyDeposit.getInstance().reloadConfig();
			MDStorage.getInstance().loadConfig();
			Utility.sendMsg(snd, "&c<> &7Config reloaded succesfully!");
			return;
		case "help":
		default:
		{
			int wanted = 0;
			try {
				wanted = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
			Utility.sendMsg(snd, "&8&m &7 &a&lMoney&2&lDeposit &8&m ");
			Utility.sendMsg(snd, "  &a> &7/deposit &f<Amount> &8&o<Target>");
			if (snd.hasPermission("moneydeposit.admin")) Utility.sendMsg(snd, "  &c> &7/deposit reload");
			return;
			}
			if (p == null) {
				Utility.sendMsg(snd, "&cPlayer only!");
				return;
			}
			if (wanted > MDStorage.getInstance().getMaxDeposit()) {
				Utility.sendMsg(snd, "&cThat's too much to deposit!");
				return;
			}
			if (wanted < MDStorage.getInstance().getMinDeposit()) {
				Utility.sendMsg(snd, "&cThat's too low to deposit!");
				return;
			}
			if (!snd.hasPermission("moneydeposit.deposit")) {
				Utility.sendMsg(snd, "&cYou can't make any deposits!");
				return;
			}
			Economy econ = MoneyDeposit.getEconomy();
			if (econ.getBalance(p) < wanted) {
				Utility.sendMsg(snd, "&cOops! &7You don't have enough money!");
				return;
			}
			if (args.length < 2) {
				ItemStack item = MDStorage.getInstance().getDepositItem(p, wanted);
				if (Utility.isEmpty(p.getOpenInventory().getBottomInventory())) {
					p.getInventory().addItem(item);
					econ.withdrawPlayer(p, wanted);
					List<String> msg = MDStorage.getInstance().getMessage(Message.DEPOSIT);
					for (String str : msg) {
						Utility.sendMsg(p, str.replaceAll("%m", wanted + ""));
					}
					return;
				}
				Utility.sendMsg(p, "&cPlease empty your inventory first!");
				return;
			}
			if (!snd.hasPermission("moneydeposit.target")) {
				Utility.sendMsg(snd, "&cYou can't have any private deposits!");
				return;
			}
			String str = args[1];
			ItemStack item = MDStorage.getInstance().getDepositSpecificItem(str, p, wanted);
			if (Utility.isEmpty(p.getOpenInventory().getBottomInventory())) {
				p.getInventory().addItem(item);
				econ.withdrawPlayer(p, wanted);
				List<String> msg = MDStorage.getInstance().getMessage(Message.DEPOSIT_SPECIFIC);
				for (String stra : msg) {
					Utility.sendMsg(p, stra.replaceAll("%m", wanted + "").replaceAll("%p", str));
				}
				return;
			}
			Utility.sendMsg(p, "&cPlease empty your inventory first!");
			return;
			
		}
		}
	}
	
}
