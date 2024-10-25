package models;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class GameObject {
//	protected Shape logicalImage;
	protected Area logicalArea;
	protected double diameter;
	protected Color color;
	protected int npoints;
	protected double x;
	protected double y;
	protected int[]xpoints;
	protected int[]ypoints;
	
	protected GameObject(double x,double y,double diameter,Color color) {
		super();
		this.logicalArea = new Area(new Ellipse2D.Double(x, y, diameter, diameter)) ;
		this.color = color;
		this.x = x;
		this.y = y;
		this.diameter = diameter;
	}

	protected GameObject(int npoints,int []xpoints,int[] ypoints,Color color) {
		super();
		this.logicalArea = new Area(new Polygon(xpoints, ypoints, npoints)) ;
		this.color = color;
		this.npoints =  npoints;
		this.xpoints = xpoints;
		this.ypoints = ypoints;
	}

	protected Area getLogicalArea() {
		return logicalArea;
	}

	public Point getCoordinates() {
		return new Point((int)x,(int) y);		
	}

	public int getNpoints() {
		return npoints;
	}

	public int[] getXpoints() {
		return xpoints;
	}

	public int[] getYpoints() {
		return ypoints;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDiameter() {
		return diameter;
	}
}
