package com.gmail.JyckoSianjaya.MoneyDeposit.Images;

import java.awt.image.BufferedImage;

import org.bukkit.map.MinecraftFont;

public final class ImageData {
	private BufferedImage image;
	private MinecraftFont font;
	private ImageDataValue amount;
	private ImageDataValue owner;
	private ImageDataValue target;
	public ImageData(BufferedImage img, MinecraftFont font, ImageDataValue amount, ImageDataValue owner) {
		this.image = img;
		this.font = font;
		this.amount = amount;
		this.owner = owner;
	}
	public ImageData(BufferedImage img, MinecraftFont font, ImageDataValue amount, ImageDataValue owner, ImageDataValue target) {
		this.image = img;
		this.font = font;
		this.amount = amount;
		this.owner = owner;
		this.target = target;
	}
	public final BufferedImage getImage() {
		return this.image;
	}
	public final MinecraftFont getFont() { return font; }
	public final ImageDataValue getAmountImageDataValue() { return amount; }
	public final ImageDataValue getOwnerImageDataValue() { return owner; }
	public final boolean hasTargetImageData() { return target != null; }
	public final ImageDataValue getTargetImageDataValue() { return target; }
	
}
