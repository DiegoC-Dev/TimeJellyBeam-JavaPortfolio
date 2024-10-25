package models;

import java.awt.Point;
import java.util.ArrayList;

public class SceneOBjectGroup {
	private ArrayList<Integer> indexLlist;
	private ArrayList<Point> coordinates;
	private String[] nameList;

	public SceneOBjectGroup(ArrayList<Integer> indexLlist, ArrayList<Point> coordinates, String[] name) {
		super();
		this.indexLlist = indexLlist;
		this.coordinates = coordinates;
		this.nameList = name;
	}
	public ArrayList<Integer> getIndexLlist() {
		return indexLlist;
	}
	public void setIndexLlist(ArrayList<Integer> indexLlist) {
		this.indexLlist = indexLlist;
	}
	public ArrayList<Point> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(ArrayList<Point> coordinates) {
		this.coordinates = coordinates;
	}
	public void setIndex(int index,int element) {
		this.indexLlist.set(index, element);
	}
	public String[] getNamelist() {
		return nameList;
	}
	public void setName(String[] name) {
		this.nameList = name;
	}
}