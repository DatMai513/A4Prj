package com.mycompany.a4;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;

public class NonPlayerRobot extends Robot{
	// FIELDS************************************
	private IStrategy curStrategy;
	private Point bottomLeft, bottomRight, topLeft, topRight;
	private int degrees, oldDeg, newDeg;
	
	// CONSTRUCTORS********************************
	// a start coordinate must be given as a parameter
	// NPRs must have an intial location "near" the first base
	public NonPlayerRobot(IStrategy s) {
		
		// NPR has different stats
		// Should never run out of energy and slightly tankier
		
		// starting location of robot must be within bounds of the map view
		setLocation(new Point(
				((rand.nextFloat()-1) + rand.nextInt(Game.map.getMapWidth()-14)),
				(rand.nextFloat()-1)+ rand.nextInt(Game.map.getMapHeight()-14)));
		
		setLastBaseReached(0);
		setEnergyLevel(10000);
		setMaximumDamage(200000000);
		curStrategy = s;
		setSpeed(6);
		setSize(75);
		
		topLeft = new Point (-getSize()/2, getSize()/2);
		topRight = new Point (getSize()/2, getSize()/2);
		bottomLeft = new Point (-getSize()/2, -getSize()/2);
		bottomRight = new Point (getSize()/2, -getSize()/2);
		
		degrees = 0;
		oldDeg = 0;
		newDeg = 0;
	}
	
	// METHODS********************************
	@Override
	public void move(int elapsed) {
		// Invoke the NPR's strategy to adjust the heading for the current strat
		oldDeg = (int)getHeading();
		invokeStrategy();
		
		newDeg = (int)getHeading();
		degrees = newDeg - oldDeg;
		rotate(degrees);
		
		// adjust speed for the refresh rate of 20ms
		// 50 refreshes per 1 second
		int s = (int) ((getSpeed() * elapsed)/50);
		
		// New location is the formula (x,y) + (cos(T)*speed, sin(T)*speed)
		// where T = 90 - heading
		translate((float)Math.cos(Math.toRadians(90 - this.getHeading())) * s, 
				(float)Math.sin(Math.toRadians(90 - this.getHeading())) * s);
	}
	
	// Draw an NPR, an unfilled red square
	@Override
	public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelScreen) {
		g.setColor(getColor());
		// create an displayXform to map triangle points to “display space”
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        Transform original = gXform.copy();
        
        gXform.translate((float)pCmpRelScreen.getX(), (float)pCmpRelScreen.getY());
        gXform.translate(getTranslation().getTranslateX(), getTranslation().getTranslateY());
        gXform.concatenate(getRotation());
        gXform.scale(getScale().getScaleX(), getScale().getScaleY());
        gXform.translate((float)-pCmpRelScreen.getX(), (float)-pCmpRelScreen.getY());
        
        g.setTransform(gXform);
		
		int[] x = {(int)topLeft.getX() + (int)pCmpRelPrnt.getX(),
				(int)topRight.getX() + (int)pCmpRelPrnt.getX(), 
				(int)bottomRight.getX() + (int)pCmpRelPrnt.getX(),
				(int)bottomLeft.getX() + (int)pCmpRelPrnt.getX() };
		int[] y = {(int)topLeft.getY() + (int)pCmpRelPrnt.getY(), 
				(int)topRight.getY() + (int)pCmpRelPrnt.getY(),
				(int)bottomRight.getY() + (int)pCmpRelPrnt.getY(),
				(int)bottomLeft.getY() + (int)pCmpRelPrnt.getY() };
		
		g.drawPolygon(x, y, 4);		
		g.setTransform(original);
	}
	
	public void setStrategy(IStrategy s) {
		curStrategy = s;
	}
	
	public IStrategy getStrategy() {
		return curStrategy;
	}

	public void invokeStrategy() {
		curStrategy.apply(this);
	}
	
	@Override 
	public String toString() {
		return "NPR: loc=" + this.getLocation() + " strategy=" + this.curStrategy + 
				" heading=" + Math.round(this.getHeading()) + " speed=" + this.getSpeed() + 
				" damageLevel=" + this.getDamageLevel() + " lastBaseReached=" + 
				this.getLastBaseReached();
	}
}
