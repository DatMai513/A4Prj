package com.mycompany.a4;

import com.codename1.ui.Transform;
import com.codename1.ui.Transform.NotInvertibleException;
import com.mycompany.a4.GameObjectCollection.Iterator;

public abstract class Fixed extends GameObject implements ISelectable {
	// FIELDS**************************************
	private boolean isSelected = false;
	
	public Point topLeft = new Point (-getSize()/2, getSize()/2);
	public Point topRight = new Point (getSize()/2, getSize()/2);
	public Point bottomLeft = new Point (-getSize()/2, -getSize()/2);
	public Point bottomRight = new Point (getSize()/2, -getSize()/2);
	
	// CONSTRUCTORS*******************************
	// METHODS***********************************
	// a way to mark an object as “selected” or not
	public void setSelected(boolean b)
	{
		isSelected = b;
	}
	// a way to test whether an object is selected
	public boolean isSelected()
	{
		return isSelected;
	}

	
	public boolean contains(float x, float y, int absX, int absY) {
		float[] pts = {x, y};
		
		// Logic given within slides
		Transform concatLTs = Transform.makeIdentity();
		concatLTs.translate(getTranslation().getTranslateX(), getTranslation().getTranslateY());
		concatLTs.concatenate(getRotation());
		concatLTs.scale(getScale().getScaleX(), getScale().getScaleY());
		Transform inverseConcatLTs = Transform.makeIdentity();
		try { concatLTs.getInverse(inverseConcatLTs);
		} catch (NotInvertibleException e) {
		System.out.println("Non invertible xform!");}
		
		inverseConcatLTs.transformPoint(pts, pts);
		
		Point lowerLeftInLocalSpace = new Point(-getSize()/2, -getSize()/2);
		
		int px = (int)pts[0];
		int py = (int)pts[1]; 
		int xLoc = (int) lowerLeftInLocalSpace.getX(); //square lower left corner
		int yLoc = (int) lowerLeftInLocalSpace.getY(); //location relative to local origin
		if ( (px >= xLoc) && (px <= xLoc+getSize()) && (py >= yLoc) && (py <= yLoc+getSize()) )
			return true;
		else
			return false;

	}
	
	

}
