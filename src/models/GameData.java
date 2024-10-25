package models;

public class GameData {
	private long x; 
	private long y;
	private String adhered;
	private long xBeforeJump;
	private long yBeforeJump;
	private String fall;
	private String image;
	private long xSpriteCoords;
	private long ySpriteCoords;
	private long widthSpriteCoords;
	private long heightSpriteCoords;
	public GameData(long x, long y, String adhered, long xBeforeJump, long yBeforeJump, String fall, String image,
			long xSpriteCoords, long ySpriteCoords, long widthSpriteCoords, long heightSpriteCoords) {
		super();
		this.x = x;
		this.y = y;
		this.adhered = adhered;
		this.xBeforeJump = xBeforeJump;
		this.yBeforeJump = yBeforeJump;
		this.fall = fall;
		this.image = image;
		this.xSpriteCoords = xSpriteCoords;
		this.ySpriteCoords = ySpriteCoords;
		this.widthSpriteCoords = widthSpriteCoords;
		this.heightSpriteCoords = heightSpriteCoords;
	}
	public long getX() {
		return x;
	}
	public long getY() {
		return y;
	}
	public String getAdhered() {
		return adhered;
	}
	public long getxBeforeJump() {
		return xBeforeJump;
	}
	public long getyBeforeJump() {
		return yBeforeJump;
	}
	public String getFall() {
		return fall;
	}
	public String getImage() {
		return image;
	}
	public long getxSpriteCoords() {
		return xSpriteCoords;
	}
	public long getySpriteCoords() {
		return ySpriteCoords;
	}
	public long getWidthSpriteCoords() {
		return widthSpriteCoords;
	}
	public long getHeightSpriteCoords() {
		return heightSpriteCoords;
	}
}
