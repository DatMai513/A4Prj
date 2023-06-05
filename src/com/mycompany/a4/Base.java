package com.mycompany.a4;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;

public class Base extends Fixed {
	// FIELDS************************
	// sequenceNumber is the order in which the Robot must travel incrementing numerically
	private int sequenceNumber;
	private Point top, topLeft, topRight, bottomLeft, bottomRight ;
	// CONSTRUCTORS***********************
	// When a base is created, a coordinate (x,y) must be given followed by a sequence number (for now) 

	public Base(int seq) {
		sequenceNumber = (seq);
		// the start location of a base must be within bounds of the map
		// refer to energy station for logic of setLocation
		setLocation(new Point(
				(Game.map.getX() + 7 + (rand.nextFloat()-1) + rand.nextInt(Game.map.getMapWidth()-14)),
				Game.map.getY() + 7 + (rand.nextFloat()-1)+ rand.nextInt(Game.map.getMapHeight()-14)));
		setSize(100);
		setColor(ColorUtil.rgb(43, 162, 255));
		
		// initialize all corner points in local space
		top = new Point (0, getSize()/2);
		topLeft = new Point (-getSize()/2, getSize()/2);
		topRight = new Point (getSize()/2, getSize()/2);
		bottomLeft = new Point (-getSize()/2, -getSize()/2);
		bottomRight = new Point (getSize()/2, -getSize()/2);
	}
	
	// METHODS**************************
	// Draw the Base, a filled isosceles triangle
	// The size of the base represents the base and height of the triangle
	// Must be drawn differently if it is selected
	@Override
	public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelScreen) {
		// create an displayXform to map triangle points to “display space”
		g.setColor(getColor());
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        Transform original = gXform.copy();
        
        gXform.translate((float)pCmpRelScreen.getX(), (float)pCmpRelScreen.getY());
        gXform.translate(getTranslation().getTranslateX(), getTranslation().getTranslateY());
        gXform.concatenate(getRotation());
        gXform.scale(getScale().getScaleX(), getScale().getScaleY());
        gXform.translate((float)-pCmpRelScreen.getX(), (float)-pCmpRelScreen.getY());
        
        g.setTransform(gXform);
		
		// fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
		// Create the arrays that hold the coordinates of the triangle
		// starting from the top vertex
		int[] x = {(int)top.getX() + (int)pCmpRelPrnt.getX(), 
				(int)bottomLeft.getX() + (int)pCmpRelPrnt.getX(), 
				(int)bottomRight.getX() + (int)pCmpRelPrnt.getX() };
		int[] y = {(int)top.getY() + (int)pCmpRelPrnt.getY(), 
				(int)bottomLeft.getY() + (int)pCmpRelPrnt.getY(), 
				(int)bottomRight.getY() + (int)pCmpRelPrnt.getY() };
		
		// Check if the fixed object is selected
		if (isSelected())
		{
			// Object is selected, draw unfilled
			g.drawPolygon(x, y, 3);
		}
		else
		{	// Object is not selected, draw filled
			g.fillPolygon(x, y, 3);
		}
		
		gXform = original.copy();

		// Flip the base number text
		gXform.translate((float)pCmpRelScreen.getX(), (float)pCmpRelScreen.getY());
		gXform.translate(getTranslation().getTranslateX(), getTranslation().getTranslateY());
		gXform.scale(1, -1);
		gXform.translate((float)-pCmpRelScreen.getX(), (float)-pCmpRelScreen.getY());
		g.setTransform(gXform);
		
		// Draw base number
		g.setColor(ColorUtil.BLACK);
		g.drawString(String.valueOf(getSeqNum()), (int)pCmpRelPrnt.getX()-10, (int)pCmpRelPrnt.getY()-20);
		
		g.setTransform(original);
	}

	public int getSeqNum() {
		return sequenceNumber;
	}
	
	@Override 
	public String toString() {
		return "Base: loc=" + getLocation().getX() + "," + getLocation().getY() +
				colorToString() + " size=" + getSize() + " SeqNum=" + getSeqNum();
	}

}
