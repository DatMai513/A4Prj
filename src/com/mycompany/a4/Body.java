package com.mycompany.a4;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;


public class Body {
	// FIELDS**************************************
	private Point bottomLeft, bottomRight, topLeft, topRight;
	private Transform myTranslation, myRotation, myScale ;
	private int size;
	
	// CONSTRUCTORS**************************************
	public Body (int s) {
		size = s;
		
		topLeft = new Point (-size/2, size/2);
		topRight = new Point (size/2, size/2);
		bottomLeft = new Point (-size/2, -size/2);
		bottomRight = new Point (size/2, -size/2);
		
		myTranslation = Transform.makeIdentity();
		myRotation = Transform.makeIdentity();
		myScale = Transform.makeIdentity();
	}
	
	// METHODS**********************************************
	public void draw (Graphics g, Point pCmpRelPrnt, Point pCmpRelScrn) {
		g.setColor(Game.gw.getPlayer().getColor());
		Transform gXform = Transform.makeIdentity();
		g.getTransform(gXform);
		Transform gOrigXform = gXform.copy(); //save the original xform
		gXform.translate((float)pCmpRelScrn.getX(),(float)pCmpRelScrn.getY());
		gXform.translate(myTranslation.getTranslateX(), myTranslation.getTranslateY());
		gXform.concatenate(myRotation);
		gXform.scale(myScale.getScaleX(), myScale.getScaleY());
		gXform.translate((float)-pCmpRelScrn.getX(),(float)-pCmpRelScrn.getY());
		g.setTransform(gXform);
		
		int[] x = {(int)topLeft.getX() + (int)pCmpRelPrnt.getX(),
			(int)topRight.getX() + (int)pCmpRelPrnt.getX(), 
			(int)bottomRight.getX() + (int)pCmpRelPrnt.getX(),
			(int)bottomLeft.getX() + (int)pCmpRelPrnt.getX() };
		int[] y = {(int)topLeft.getY() + (int)pCmpRelPrnt.getY(), 
			(int)topRight.getY() + (int)pCmpRelPrnt.getY(),
			(int)bottomRight.getY() + (int)pCmpRelPrnt.getY(),
			(int)bottomLeft.getY() + (int)pCmpRelPrnt.getY() };

		g.fillPolygon(x, y, 4);
		
		g.setTransform(gOrigXform);
	}
}

