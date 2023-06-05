package com.mycompany.a4;

public class Point {
	// FIELDS**************************************
	private double x, y;
	
	// CONSTRUCTORS*******************************
	public Point (double xVal, double yVal) {
		setX(Math.round((xVal * 100)/100));
		setY(Math.round((yVal * 100)/100));
	}

	// METHODS***********************************
	// getter and setter for x
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	
	// getter and setter for y
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return x + "," + y;
	}
}
