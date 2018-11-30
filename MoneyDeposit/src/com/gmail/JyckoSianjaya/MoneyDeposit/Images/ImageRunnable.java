package com.gmail.JyckoSianjaya.MoneyDeposit.Images;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.JyckoSianjaya.MoneyDeposit.MoneyDeposit;

public class ImageRunnable {
	private static ImageRunnable instance;
	private ArrayList<ImageTask> todo = new ArrayList<ImageTask>();
	private ImageRunnable() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (ImageTask task : new ArrayList<ImageTask>(todo)) {
					if (task.getTicks() == 0) {
						todo.remove(task);
						continue;
					}
					task.reduceTicks();
					task.runTask();
				}
			}
			
		}.runTaskTimerAsynchronously(MoneyDeposit.getInstance(), 5L, 5L);
	}
	public static ImageRunnable getInstance() {
		if (instance == null) instance = new ImageRunnable();
		return instance;
	}
	public void addTask(ImageTask task) {
		todo.add(task);
	}
	public void clearTasks() { todo.clear(); }
}
