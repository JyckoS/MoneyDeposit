package com.gmail.JyckoSianjaya.MoneyDeposit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.JyckoSianjaya.MoneyDeposit.Commands.MDCommand;
import com.gmail.JyckoSianjaya.MoneyDeposit.Events.MDEventHandler;
import com.gmail.JyckoSianjaya.MoneyDeposit.Events.MDEventListener;
import com.gmail.JyckoSianjaya.MoneyDeposit.Images.ImageRunnable;
import com.gmail.JyckoSianjaya.MoneyDeposit.Storage.MDChecker;
import com.gmail.JyckoSianjaya.MoneyDeposit.Storage.MDStorage;
import com.gmail.JyckoSianjaya.Utilities.ActionBarAPI;
import com.gmail.JyckoSianjaya.Utilities.Utility;

import net.milkbowl.vault.economy.Economy;

public final class MoneyDeposit extends JavaPlugin {
	private static MoneyDeposit instance;
	private MDStorage strg;
	private MDEventListener listener;
	private MDEventHandler handler;
	private ActionBarAPI api;
	private MDChecker mc;
	private ImageRunnable rbb;
	private static Boolean isVault;
	private static Economy econ;
	/*
	 * 
	 * 
	 * NBT DATA(S):
	 * "MDDeposit" - Amount of moeny deposited.
	 * "MDOwner" - Name of Owner
	 * "MDTarget" - Name of Target
	 * 
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public final void onEnable() {
		instance = this;
		new Metrics(this);
		sendConsole();
		
	}
	@Override
	public final void onDisable() {
	}
	public static final MoneyDeposit getInstance() { return instance; }
	public static Economy getEconomy() { return econ; }
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault") != true) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        isVault = true;
        econ = rsp.getProvider();
        return true;
    }
	private void sendConsole() {
		String ver = this.getDescription().getVersion();
		Utility.sendConsole("&c<>&7 Running &2Money&aDeposit &7V" + ver + ".");
		Utility.sendConsole("&c<>&7 Checking Vault... ");
		if (!setupEconomy()) {
			Utility.sendConsole("&c<>&7 Vault is &cNot Installed&7, plugin disabled.");
			this.setEnabled(false);
			return;
		}
		Utility.sendConsole("&c<>&7 Vault is &aenabled!");
		Utility.sendConsole("&c<> &7Loading Commands...");
		loadCommand();
		Utility.sendConsole("&c<> &7Loading Events...");
		loadEvents();
		loadObjects();
		Utility.sendConsole("&c<> &aSuccesfully loaded everything!");
	}
	private void loadEvents() {
		handler = MDEventHandler.getInstance();
		Bukkit.getPluginManager().registerEvents(new MDEventListener(), this);
	}
	private void loadCommand() {
		MDCommand cmd = new MDCommand();
		this.getCommand("moneydeposit").setExecutor(cmd);
		this.getCommand("moneydeposit").setTabCompleter(cmd);
	}
	private void loadObjects() {
		Utility.sendConsole("&c<> &7Loading Data | Config...");
		strg = MDStorage.getInstance();
		api = ActionBarAPI.getInstance();
		mc = MDChecker.getInstance();
		handler = MDEventHandler.getInstance();
		rbb = ImageRunnable.getInstance();
	}
}
