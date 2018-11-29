package com.gmail.JyckoSianjaya.MoneyDeposit.Storage;

import org.bukkit.inventory.ItemStack;

import com.gmail.JyckoSianjaya.MoneyDeposit.Deposits.Deposit;
import com.gmail.JyckoSianjaya.Utilities.NBT.NBTItem;

public class MDChecker {
	private static MDChecker instance;
	private MDStorage str = MDStorage.getInstance();
	private MDChecker() {
	}
	public final static MDChecker getInstance() {
		if (instance == null) instance = new MDChecker();
		return instance;
	}
	public final static Deposit getDeposit(final ItemStack item)  {
		final NBTItem nbt = new NBTItem(item);
		if (!nbt.hasKey("MDDeposit")) return null;
		final int amount = nbt.getInteger("MDDeposit");
		final String ownername = nbt.getString("MDOwner");
		if (nbt.hasKey("MDTarget")) {
			return new Deposit(ownername, amount, nbt.getString("MDTarget"));
		}
		return new Deposit(ownername, amount);
	}
}
