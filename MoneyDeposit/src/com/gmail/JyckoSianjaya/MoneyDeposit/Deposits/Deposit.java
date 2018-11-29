package com.gmail.JyckoSianjaya.MoneyDeposit.Deposits;

import org.bukkit.entity.Player;

public final class Deposit {
	private int deposit = 0;
	private String ownername = "";
	private String targetname = null;
	public Deposit(Player p, int amount) {
		this.deposit = amount;
		this.ownername = p.getName();
	}
	public Deposit(String ownername, int amount) {
		this.deposit = amount;
		this.ownername = ownername;
	}
	public Deposit(Player p, int amount, String target) {
		this.deposit = amount;
		this.ownername = p.getName();
		this.targetname = target;
	}
	public Deposit(String p, int amount, String target) {
		this.deposit = amount;
		this.ownername = p;
		this.targetname = target;
	}
	public final String getOwnerName() {
		return this.ownername;
	}
	public final String getTargetName() {
		return this.targetname;
	}
	public final boolean hasTarget() {
		return targetname != null;
	}
	public final int getDepositAmount() { return this.deposit; }
	public final void setDeposit(int newamount) {
		this.deposit = newamount;
	}
}
