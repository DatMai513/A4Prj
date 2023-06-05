package com.mycompany.a4;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;

public class Arms {
	// FIELDS**************************************
	private Point top, bottomLeft, bottomRight ;
	private int myColor ;
	private Transform myTranslation ;
	private Transform myRotation ;
	private Transform myScale ;
	private int size;
	
	// CONSTRUCTORS**************************************
	public Arms(int s){
		size = s;
		// define a default flame with base=40, height=40, and origin in the center.
		top = new Point (0, 20);
		bottomLeft = new Point (-20, -20);
		bottomRight = new Point (20, -20);
		// initialize the transformations applied to the Flame
		myTranslation = Transform.makeIdentity();
		myRotation = Transform.makeIdentity();
		myScale = Transform.makeIdentity();
	}
	// FIELDS**************************************
	public void setColor(int iColor){
		myColor = iColor;
	}
	//...continued
	// Flame class, continued...
	public void rotate(double degrees) {
		myRotation.rotate((float) Math.toRadians(degrees), 0, 0);
	}
	
	public void scale (double sx, double sy) {
		myScale.scale((float)sx, (float)sy);
	}
	public void translate (double tx, double ty) {
		myTranslation.translate((float)tx, (float)ty);
	}
	
	public void draw (Graphics g, Point pCmpRelPrnt, Point pCmpRelScreen) {
	//append the flames’s LTs to the xform in the Graphics object (do not forget to do “local
	//origin” transformations). ORDER of LTs: Scaling LT will be applied to coordinates FIRST,
	//then Translation LT, and lastly Rotation LT. Also restore the xform at the end of draw() to
	//remove this sub-shape’s LTs from xform of the Graphics object. Otherwise, we would also
	//apply these LTs to the next sub-shape since it also uses the same Graphics object.
		
		g.setColor(ColorUtil.BLUE);
		
		Transform gXform = Transform.makeIdentity();
		g.getTransform(gXform);
		Transform gOrigXform = gXform.copy(); //save the original xform
		gXform.translate((float)pCmpRelScreen.getX(),(float)pCmpRelScreen.getY());
		gXform.concatenate(myRotation); 
		gXform.translate(myTranslation.getTranslateX() + size - 10, myTranslation.getTranslateY() + size - 10);
		gXform.scale(myScale.getScaleX(), myScale.getScaleY());
		gXform.translate((float)-pCmpRelScreen.getX(),(float)-pCmpRelScreen.getY());
		g.setTransform(gXform);
		
		//draw the lines
		int[] x = {(int)top.getX() + (int)pCmpRelPrnt.getX(), 
				(int)bottomLeft.getX() + (int)pCmpRelPrnt.getX(), 
				(int)bottomRight.getX() + (int)pCmpRelPrnt.getX() };
		int[] y = {(int)top.getY() + (int)pCmpRelPrnt.getY(), 
				(int)bottomLeft.getY() + (int)pCmpRelPrnt.getY(), 
				(int)bottomRight.getY() + (int)pCmpRelPrnt.getY() };
		
		g.fillPolygon(x, y, 3);
		
		//...[draw the rest of the lines]
		g.setTransform(gOrigXform); //restore the original xform (remove LTs)
	}
	
}
