package com.mycompany.a4;
import java.util.ArrayList;
import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.mycompany.a4.Point;

public abstract class GameObject implements IDrawable, ICollider{
	//FIELDS***********************
	//Attributes that all game objects will have
	
	// size is the length of the bounding square of an object
	private int size;
	
	// (x,y) is center coordinate of an object
	// the range of x is [0.0, 1024.0]
	// the range of y is [0.0, 768.0]
	private ArrayList<GameObject> collisionList = new ArrayList<GameObject>();
	
	// the rgb value of an object
	private int color;
	
	// random number generator
	static Random rand = new Random();
	
	// Transform objects
	private Transform myRotation = Transform.makeIdentity();
	private Transform myTranslation = Transform.makeIdentity();
	private Transform myScale = Transform.makeIdentity();
	// CONSTRUCTORS**********************

	// METHODS**************************
	// Sets up a box boundary on the main object and the other object
	// Then checks if these boundaries overlap and returns if overlap has happened
	public boolean collidesWith(GameObject other) {
		// The location of any given object is its center
		// So adjust the given center location to the origin used in draw()
		Point O1 = new Point(this.getLocation().getX() - getSize()/2, this.getLocation().getY() - getSize()/2);
		Point O2 = new Point(other.getLocation().getX() - getSize()/2, other.getLocation().getY() - getSize()/2);

		// Get both left sides of the objects
		int L1 = (int)O1.getX();
		int L2 = (int)O2.getX();
		
		// Get both right sides of the objects
		int R1 = (int)O1.getX() + this.getSize();
		int R2 = (int)O2.getX() + other.getSize();
		
		// Get both top sides of the objects
		int T1 = (int)O1.getY();
		int T2 = (int)O2.getY();	
				
		// Get both bottom sides of the objects
		int B1 = (int)O1.getY() + this.getSize();
		int B2 = (int)O2.getY() + other.getSize();
		
		// Logic was given in slides
		if ( (R1 < L2) || (R2 < L1) || (T2 > B1) || (T1 > B2) ) {
			return false;	
		}
		else {
			return true;
		}
	}

	public void handleCollision(GameObject other) {
		
	}
	
	// Setters for the transform matrixes
	public void translate(float tx, float ty) {
		myTranslation.translate(tx, ty);
	}
	
	public void rotate(float degrees) {
		//pivot point (rotation origin) is (0,0), this means the rotation will be applied about
		//the screen origin
		myRotation.rotate((float)Math.toRadians(degrees),0,0);
	}
	
	public void scale(float sx, float sy) {
		//remember that like other transformation methods, scale() is also applied relative to
		//screen origin
		myScale.scale(sx, sy);
	}
	
	// Getters for the transform matrixes
	public Transform getRotation() {
		return myRotation;
	}
	
	public Transform getTranslation() {
		return myTranslation;
	}
	
	public Transform getScale() {
		return myScale;
	}
	
	// Reset the TF to the identity matrix
	public void resetTransform() {
		myRotation.setIdentity();
		myTranslation.setIdentity();
		myScale.setIdentity();
	}
	
	/*public void newDraw(Graphics g) {
		Transform xForm = Transform.makeIdentity();
		Transform oldOne = Transform.makeIdentity();
		
		g.getTransform(oldOne);
		g.getTransform(xForm);
		
		xForm.concatenate(localXform);
		g.setTransform(xForm);
		
		for (int i = 0; i < Game.gw.getList().size(); i++) {
			Game.gw.getList().getIterator().get(i).newDraw(g);
		}
		
		//draw(g, new Point(0,0));
		g.setTransform(oldOne);
	}*/
	
	// Override equals to be used in ArrayList contains()
	// So it only searches for an exact object 
	// since an objects location is unique
    @Override
    public boolean equals(Object o){
        if(o instanceof GameObject){
             GameObject p = (GameObject) o;
             return this.getLocation().equals(p.getLocation());
        } else
             return false;
    }
	
	// getter for size, no setter
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	// getter and setter for location
	public Point getLocation() {
       float x = myTranslation.getTranslateX();
       float y = myTranslation.getTranslateY();
       return new Point(x, y);
       //return location;
	}
	public void setLocation(Point loc) {
		translate((float)loc.getX(), (float)loc.getY());
		//location = loc;
	}
	
	// getter for color, no setter
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	public ArrayList<GameObject> getCL() {
		return collisionList;
	}
	
	// convert rgb color into a useable string
	public String colorToString() {
		String colorString = " color: " + "[" + ColorUtil.red(getColor()) + ","
								+ ColorUtil.green(getColor()) + ","
								+ ColorUtil.blue(getColor()) + "]";
		return colorString;
	}
}
