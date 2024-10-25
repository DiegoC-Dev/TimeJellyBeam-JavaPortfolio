package models;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite {
	private ArrayList<BufferedImage> sprite;
	private String name;
	public Sprite(ArrayList<BufferedImage> sprite, String name) {
		super();
		this.sprite = sprite;
		this.name = name;
	}
	public ArrayList<BufferedImage> getSprite() {
		return sprite;
	}
	public void setSprite(ArrayList<BufferedImage> sprite) {
		this.sprite = sprite;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
