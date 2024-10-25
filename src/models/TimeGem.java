package models;

import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class TimeGem extends GameObject{

	protected TimeGem(double x, double y, double diameter, Color color) {
		super(x, y, diameter, color);
		// TODO Auto-generated constructor stub
	}
	public boolean consumedGem(Hero hero){
		Area ownArea = new Area(new Ellipse2D.Double(hero.getX(), hero.getY(), hero.getDiameter(), hero.getDiameter())) ;
//		for (int i = 0; i < gemList.size(); i++) {
		ownArea.intersect(getLogicalArea());			
		if(!ownArea.isEmpty()){
//			System.out.println("gema consumida");
			return true;
		}	
		ownArea = new Area(new Ellipse2D.Double(hero.getX(), hero.getY(), hero.getDiameter(), hero.getDiameter())) ;
//		}
		return false;
	}


}
