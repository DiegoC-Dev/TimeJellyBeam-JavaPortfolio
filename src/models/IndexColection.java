package models;

public class IndexColection {
	private int arrayIndex;
	private int imageIndex;
	public IndexColection(int arrayIndex, int imageIndex) {
		super();
		this.arrayIndex = arrayIndex;
		this.imageIndex = imageIndex;
	}
	public int getArrayIndex() {
		return arrayIndex;
	}
	public void setArrayIndex(int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}
	public int getImageIndex() {
		return imageIndex;
	}
	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}
}
