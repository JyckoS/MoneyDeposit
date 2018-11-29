package com.gmail.JyckoSianjaya.MoneyDeposit.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gmail.JyckoSianjaya.MoneyDeposit.Deposits.Deposit;
import com.gmail.JyckoSianjaya.MoneyDeposit.Storage.MDChecker;

public final class MDEventListener implements Listener {
	private MDEventHandler hand = MDEventHandler.getInstance();
	@EventHandler
	public final void onRightClick(final PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}
		Deposit depos = MDChecker.getDeposit(e.getPlayer().getItemInHand());
		if (depos == null) return;
		hand.handleWithdraw(depos, e);
	}
}
