package com.gmail.JyckoSianjaya.MoneyDeposit.Events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.JyckoSianjaya.MoneyDeposit.MoneyDeposit;
import com.gmail.JyckoSianjaya.MoneyDeposit.Deposits.Deposit;
import com.gmail.JyckoSianjaya.MoneyDeposit.Storage.MDStorage;
import com.gmail.JyckoSianjaya.MoneyDeposit.Storage.MDStorage.Message;
import com.gmail.JyckoSianjaya.Utilities.Utility;
import com.gmail.JyckoSianjaya.Utilities.XMaterial;
import com.gmail.JyckoSianjaya.Utilities.XSound;

import net.milkbowl.vault.economy.Economy;

public final class MDEventHandler {
	private static MDEventHandler hand;
	private MDEventHandler() {
	}
	public static final MDEventHandler getInstance() {
		if (hand == null) hand = new MDEventHandler();
		return hand;
	}
	private Sound sound = XSound.BLAZE_HIT.bukkitSound();
	private Sound sound2 = XSound.ANVIL_LAND.bukkitSound();
	public void handleWithdraw(Deposit depo, PlayerInteractEvent e) {
		Player p = e.getPlayer();
		String target = depo.getTargetName();
		Boolean hastarget = false;
		if (target != null) hastarget = true;
		String owner = depo.getOwnerName();
		int amount = depo.getDepositAmount();
		Economy econ = MoneyDeposit.getEconomy();
		if (hastarget) {
			String pname = p.getName();
			if (!owner.equalsIgnoreCase(pname) && !target.equalsIgnoreCase(pname)) {
				Utility.sendMsg(p, "&cThat's not yours!");
				return;
			}
			for (String str : MDStorage.getInstance().getMessage(Message.WITHDRAWN)) {
				str = str.replaceAll("%m", "" + amount);
				str = str.replaceAll("%o", owner);
				Utility.sendMsg(p, str);
			}
			econ.depositPlayer(p, amount);
			Utility.PlaySound(p, sound, 1.0F, 2.0F);
			Utility.PlaySound(p, sound2, 0.5F, 2.0F);
			ItemStack i = p.getItemInHand();
			if (i.getAmount() > 1) {
				i.setAmount(i.getAmount() - 1);
				p.setItemInHand(i);
				return;
			}
			p.getInventory().setItemInHand(XMaterial.AIR.parseItem());
			return;
		}
		for (String str : MDStorage.getInstance().getMessage(Message.WITHDRAWN)) {
			str = str.replaceAll("%m", "" + amount);
			str = str.replaceAll("%o", owner);
			Utility.sendMsg(p, str);
		}
		econ.depositPlayer(p, amount);
		Utility.PlaySound(p, sound, 1.0F, 2.0F);
		Utility.PlaySound(p, sound2, 0.5F, 2.0F);
		ItemStack i = p.getItemInHand();
		if (i.getAmount() > 1) {
			i.setAmount(i.getAmount() - 1);
			p.setItemInHand(i);
			return;
		}
		p.setItemInHand(XMaterial.AIR.parseItem());
		return;
	}
} 
