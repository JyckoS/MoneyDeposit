package com.gmail.JyckoSianjaya.MoneyDeposit.Images;

public class ImageDataValue {
	private int starting_x;
	private int starting_y;
	public ImageDataValue(int x, int y) {
		this.starting_x = x;
		this.starting_y = y;
	}
	public void setX(int x) {
		this.starting_x = x;
	}
	public void setY(int y) {
		this.starting_y = y;
	}
	public int getX() { return starting_x; }
	public int getY() { return starting_y; }
}
