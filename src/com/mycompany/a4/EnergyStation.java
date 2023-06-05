package com.mycompany.a4;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;

public class EnergyStation extends Fixed{
	// FIELDS****************************
	// all energy stations are the same hardcoded color
	private int capacity;	
	private Point bottomLeft, bottomRight, topLeft, topRight;	
	// CONSTRUCTORS*****************************
	public EnergyStation() {
		// the size of an energy station is assigned randomly [75-125]
		setSize((75 + rand.nextInt(101)));
		// energy station must spawn within the bounds of the map view
		// the origin point of the map is (getX()+7, getY()+7) 
		// where +7 represents the red border since it has a thickness of 6
		// the limit of the map is (getWidth()-12, getHeight()-12)
		// where -14 represents both sides of the red border
		setLocation(new Point(
				(Game.map.getX() + 7 + (rand.nextFloat()-1) + rand.nextInt(Game.map.getMapWidth()-14)),
				Game.map.getY() + 7 + (rand.nextFloat()-1)+ rand.nextInt(Game.map.getMapHeight()-14)));
		
		// capacity is proportional to size
		capacity = getSize()/15;
		// make the station green
		setColor(ColorUtil.rgb(26, 178, 102));
		
		topLeft = new Point (-getSize()/2, getSize()/2);
		topRight = new Point (getSize()/2, getSize()/2);
		bottomLeft = new Point (-getSize()/2, -getSize()/2);
		bottomRight = new Point (getSize()/2, -getSize()/2);
	}
	
	// METHODS*********************************	

	// Draw the energy station, a filled circle
	// Must be drawn differently if it is selected
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
		// Check if the fixed object is selected
		if (isSelected())
		{
			// Object is selected, draw unfilled
			g.drawArc((int)pCmpRelPrnt.getX() + (int)bottomLeft.getX(), 
					(int)pCmpRelPrnt.getY() + (int)bottomLeft.getY(), 
					getSize(), getSize(), 0, 360);
		}
		else
		{
			// Object is not selected, draw filled
			g.fillArc((int)pCmpRelPrnt.getX() + (int)bottomLeft.getX(), 
					(int)pCmpRelPrnt.getY() + (int)bottomLeft.getY(),
					getSize(), getSize(), 0, 360);
		}
		
		// Flip the energy station capacity text
		gXform = original.copy();

		gXform.translate((float)pCmpRelScreen.getX(), (float)pCmpRelScreen.getY());
		gXform.translate(getTranslation().getTranslateX(), getTranslation().getTranslateY());
		gXform.scale(1, -1); 
		gXform.translate((float)-pCmpRelScreen.getX(), (float)-pCmpRelScreen.getY());
		g.setTransform(gXform);

		g.setColor(ColorUtil.BLACK);
		g.drawString(String.valueOf(getCapacity()), (int)pCmpRelPrnt.getX() - 10, (int)pCmpRelPrnt.getY() - 10);
		
		g.setTransform(original);
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapactiy(int x) {
		capacity = x;
	}
	
	@Override 
	public String toString() {
		return "EnergyStation: loc=" + (double)Math.round(getLocation().getX()) + "," + 
				(double)Math.round(getLocation().getY()) + colorToString() + " size=" + getSize() + 
				" capacity=" + getCapacity();
	}
}
