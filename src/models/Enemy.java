package models;

import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JOptionPane;

public class Enemy extends GameObject{
	private static final int MAX_DISTANCE = 150;
	int fixedvalue=MAX_DISTANCE/2;
	boolean direction;
	double initX;
	double endX;
	int totaldistance;
	int nuevoInicio;
	boolean saveDistance;
	int maxDistance;
	protected Enemy(double x, double y, double diameter, Color color) {
		super(x, y, diameter, color);
		totaldistance = randomdistance();
//		totaldistance = MAX_DISTANCE;
//		totaldistance = 100;
		initX =x;
		endX =initX-totaldistance+1;
//		System.out.println("init "+initX);
//		System.out.println("end "+endX);
	}
	
	public boolean successfulAttack(Hero hero){
		Area ownArea = new Area(new Ellipse2D.Double(hero.getX(), hero.getY(), hero.getDiameter(), hero.getDiameter())) ;
		ownArea.intersect(getLogicalArea());			
		if(!ownArea.isEmpty()){
			ownArea = null;
//			System.out.println("gema consumida");
			return true;
		}	
		ownArea = null;
//		ownArea = new Area(new Ellipse2D.Double(hero.getX(), hero.getY(), hero.getDiameter(), hero.getDiameter())) ;
		return false;
	}
	public void setXposition(int time){
		int slowdown = 8;
		int gifTime;
		BufferedImage img = null;
		gifTime = (time%slowdown==0)?time/slowdown:(int)(time/slowdown);
		int distance=(gifTime %totaldistance);
		if(distance==1) {
			saveDistance=false;
		}
		if(!saveDistance) {
			x= (direction)?endX+distance:initX-distance;
			direction=(x==endX)?true:(x==initX)?false:direction;
			if(distance==totaldistance-1) 
				saveDistance=true;
			this.logicalArea = new Area(new Ellipse2D.Double(x, y, diameter, diameter)) ;
		}	
	}
	public int randomdistance() {
		return new Random().nextInt(MAX_DISTANCE-fixedvalue)+1+fixedvalue;
//		return new Random().nextInt(maxDistance-50)+1+50;
	}
	public boolean isDirection() {
		return direction;
	}
}
