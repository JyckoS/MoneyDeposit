package com.gmail.JyckoSianjaya.MoneyDeposit.Images;

public interface ImageTask {
	int duration = 0;
	public void runTask();
	public void reduceTicks();
	public void reduceTicks(int toreduce);
	public int getTicks();
}
