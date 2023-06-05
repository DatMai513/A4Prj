package com.mycompany.a4;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;

import java.util.Random;

public class Drone extends Movable {
	// FIELDS*******************************
	private Point top, bottomLeft, bottomRight ;
	// CONSTRUCTORS*****************************
	public Drone() {
		// the size of a drone is assigned randomly [75-150]
		setSize(25 + rand.nextInt(101));
		// the start location of a drone must be within bounds of the map
		// refer to energy station for logic of setLocation
		setLocation(new Point(
				(Game.map.getX() + 7 + (rand.nextFloat()-1) + rand.nextInt(Game.map.getMapWidth()-14)),
				Game.map.getY() + 7 + (rand.nextFloat()-1)+ rand.nextInt(Game.map.getMapHeight()-14)));

		// spawn the drown with a random heading [0-360]
		setHeading(rand.nextInt(361));
		// random intial speed [5-10]
		setSpeed(5 + rand.nextInt(5));
		// random intial heading [1-359]
		setHeading(rand.nextInt(360));
		
		// initialize all corner points in local space
		top = new Point (0, getSize()/2);
		bottomLeft = new Point (-getSize()/2, -getSize()/2);
		bottomRight = new Point (getSize()/2, -getSize()/2);
	}
	
	// METHODS************************************
	public void move(int elapsed) {
		// Randomly adjust the heading by 5 degrees
		// The adjustment ranges from -5 to 5
		int oldDeg = (int)getHeading();
		setHeading(getHeading() + (-3 + rand.nextInt(7)));
		
		// newDegree - oldDegree should be how much we rotate our object by
		int newDeg = (int)getHeading();
		int degrees = newDeg - oldDeg;
		
		rotate(degrees);

		
		// adjust speed for the refresh rate of 20ms
		// 50 refreshes per 1 second
		int s = (int) ((getSpeed() * elapsed)/50);
		
		// New location is the formula (x,y) + (cos(T)*speed, sin(T)*speed)
		// where T = 90 - heading
		translate((float)Math.cos(Math.toRadians(90 - this.getHeading())) * s, 
				(float)Math.sin(Math.toRadians(90 - this.getHeading())) * s);
	}
	
	// Draw the drone on the map, an unfilled isosceles triangle
	// The size of the drone represents the base and height of the triangle
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
		
		int[] x = {(int)top.getX() + (int)pCmpRelPrnt.getX(), 
				(int)bottomLeft.getX() + (int)pCmpRelPrnt.getX(), 
				(int)bottomRight.getX() + (int)pCmpRelPrnt.getX() };
		int[] y = {(int)top.getY() + (int)pCmpRelPrnt.getY(), 
				(int)bottomLeft.getY() + (int)pCmpRelPrnt.getY(), 
				(int)bottomRight.getY() + (int)pCmpRelPrnt.getY() };
		
		g.drawPolygon(x, y, 3);
		
		g.setTransform(original);
		//Simulate a bounding box
		//g.drawRect((int)loc.getX(), (int)loc.getY(), getSize(), getSize());
	}
	
	
	@Override
	public void setColor(int x) {
		return;		
	}
	
	@Override 
	public String toString() {
		return "Drone: loc=" + (double)Math.round(getLocation().getX()) + "," + 
				(double)Math.round(getLocation().getY()) + colorToString() + " heading=" + 
				getHeading() + " speed=" + getSpeed() + " size=" + getSize();
	}
	
}
