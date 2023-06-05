package com.mycompany.a4;

public abstract class Movable extends GameObject {
	// FIELDS************************
	// heading is the compass direction where North is 0 degrees
	private double heading;
	private double speed;
	
	// CONSTRUCTOR*************************

	// METHODS************************
	public abstract void move(int time);
	
	// getter and setter for speed
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;	
	}
	
	// getter and setter for heading
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
}
