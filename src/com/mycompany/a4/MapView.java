package com.mycompany.a4;
import java.util.Observable;
import java.util.Observer;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.Transform.NotInvertibleException;
import com.codename1.ui.geom.Dimension;
import com.mycompany.a4.GameObjectCollection.Iterator;

public class MapView extends Container implements Observer {
	// FIELDS***************************
	private int mapWidth;
	private int mapHeight;
	private boolean positionClicked = false;
	
	Transform worldToND, ndToDisplay, VTM, inverseVTM;
	
	// Viewing window coordinates
	private float viewL, viewB, viewT, viewR;
	private float viewWidth, viewHeight;
	
	// coordinates to save the initial point of a drag
	private int pPrevDragLocX, pPrevDragLocY;
	// CONSTRUCTORS*********************
	public MapView() {
		worldToND = Transform.makeIdentity();
		ndToDisplay = Transform.makeIdentity();
		VTM = Transform.makeIdentity();
		inverseVTM = Transform.makeIdentity();
	}

	// METHODS**************************
	// Initializes all the variables since show() must be called first in Game
	public void setAllPoints() {
		/*worldLeft = 0;
		worldBottom = 0;
		worldRight = getWidth();
		worldTop = getHeight();*/
		
		viewL = 0;
		viewB = 0;
		viewR = this.getWidth()/2;
		viewT = this.getHeight()/2;
		
		viewWidth = viewR - viewL;
		viewHeight = viewT - viewB;
	}
	
	@Override
	protected Dimension calcPreferredSize(){
		return new Dimension(1000, 1000);
	}

	
	@Override
	public void update(Observable observable, Object data) {
		//System.out.println("MAP Height:"+mapHeight+" Width:"+mapWidth);
		//System.out.println("MAP Y:"+getY()+" X:"+getX());
		
		Game.gw.getPlayer().updateLTs();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		//origin location of the component (CustomContainer) relative to its parent container origin
		Point pCmpRelPrnt = new Point(getX(),getY());
		//origin location of the component (CustomContainer) relative to the screen origin
		Point pCmpRelScreen = new Point(getAbsoluteX(),getAbsoluteY());
		
		super.paint(g);
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);

		// construct the VTM
		updateVTM();
		g.setTransform(VTM);
		
        gXform.translate((float)pCmpRelScreen.getX(), (float)pCmpRelScreen.getY());
        gXform.concatenate(VTM);
        gXform.translate((float)-pCmpRelScreen.getX(), (float)-pCmpRelScreen.getY());//
        g.setTransform(gXform);
		
		// Iterate through the game object collection to call all objects
		// draw() method
		for (int i = 0; i < Game.gw.getList().size(); i++) {
			Game.gw.getList().getIterator().get(i).draw(g, pCmpRelPrnt, pCmpRelScreen);
		}
		
		g.resetAffine();
	}
	
	// After a pointer is pressed, calcualte if it is "inside"
	// of any object's boundaries
	// if that object has a pointer, mark it as selected
	@Override
	public void pointerPressed(int x, int y) {
		
		x = x - getAbsoluteX();
		y = y - getAbsoluteY();

		pPrevDragLocX = x;
		pPrevDragLocY = y;

		try {
		float[] pts = {x, y};
		VTM.getInverse(inverseVTM);
		inverseVTM.transformPoint(pts, pts);
		float transX = pts[0];
		float transY = pts[1];

		Iterator k = Game.gw.getList().getIterator();
		
		
		// Is position button clicked?
		if (positionClicked)
		{
			while(k.hasNext())
			{
				if (k.get() instanceof Fixed)
				{
					GameObject obj = k.get();
					if (((Fixed) k.get()).isSelected())
					{
	
						// An object is selected
						// And Position is selected
						// A click just happened at (x,y)
						// Move that object to (x,y)
						
						// Repeat the same logic for transformations done in contains
						Transform concatLTs = Transform.makeIdentity();
						concatLTs.translate(obj.getTranslation().getTranslateX(), obj.getTranslation().getTranslateY());
						concatLTs.concatenate(obj.getRotation());
						concatLTs.scale(obj.getScale().getScaleX(), obj.getScale().getScaleY());
						Transform inverseConcatLTs = Transform.makeIdentity();
						try { concatLTs.getInverse(inverseConcatLTs);
						} catch (NotInvertibleException e) {
						System.out.println("Non invertible xform!");}
						
						inverseConcatLTs.transformPoint(pts, pts);

						
						int px = (int)pts[0];
						int py = (int)pts[1]; 
						
						obj.setLocation(new Point(px, py));
						
						// uncheck Position
						setPositionClicked(false);
					}
				}
		
				k.setCursor(k.getCursor() + 1);
			}
		}
		
		else
		{
			// No object is selected currently
			// Only allow an object to be selected if the game is pasued
			if (Game.isPaused())
			{
				// Code below selects an object
				// Adjust the given pointer click coordinates to the Game Form's origin
				
				//Point cursor_loc = new Point(x, y);
				
				Iterator i = Game.gw.getList().getIterator();
				while (i.hasNext())
				{
					GameObject obj = i.get();
					if (obj instanceof Fixed)
					{
						if ((((Fixed) obj).contains(transX, transY, getAbsoluteX(), getAbsoluteY())))
						{
							((Fixed) obj).setSelected(true);
						}
						else
						{
							((Fixed) obj).setSelected(false);
						}	
					}
				
					i.setCursor(i.getCursor() + 1);
				}
			}
		}
		
		repaint();
		}catch(NotInvertibleException e) {System.out.println("ERROR");}
	}
	
	public void setPositionClicked(boolean x)
	{
		positionClicked = x;
	}
	
	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public void setMapWidth(int x) {
		this.mapWidth = x;
	}

	public void setMapHeight(int x) {
		this.mapHeight = x;
	}
	
	
	public void updateVTM() {
		VTM = Transform.makeIdentity();
		worldToND = Transform.makeScale(
		1.0f/viewWidth, 1.0f/viewHeight);
		worldToND.translate(-viewL, -viewB);
		ndToDisplay = Transform.makeTranslation(0, this.getHeight());
		ndToDisplay.scale(this.getWidth(), -this.getHeight());
		VTM.concatenate(ndToDisplay);
		VTM.concatenate(worldToND);
		
		
	}
	
	public void zoom(float factor) {
		float newWidth = viewWidth * factor;
		float newHeight = viewHeight * factor;
		
		if(newWidth<0||newHeight<0||newWidth>8192||newHeight>4320) 
			return;
		
		viewL += (viewWidth - newWidth)/2;
		viewB += (viewHeight - newHeight)/2;
		
		viewWidth = (int) newWidth; 
		viewHeight = (int) newHeight;
		
		this.repaint();
	}
	
	public void pointerDragged(int x, int y){
		
		// Pan logic given in slides
		if (pPrevDragLocX != -1)
		{
		if (pPrevDragLocX < x)
		panHorizontal(-5);
		else if (pPrevDragLocX > x)
		panHorizontal(5);
		if (pPrevDragLocY < y)
		panVertical(5);
		else if (pPrevDragLocY > y)
		panVertical(-5);
		}
		pPrevDragLocX = x;
		pPrevDragLocY = y;
		}
	
	public void panHorizontal(double delta) {
		viewL += delta;
		this.repaint();
	}
		
	public void panVertical(double delta) {
		viewB += delta;
		this.repaint();
	}

	@Override
	public boolean pinch(float scale) {
		zoom(scale);
		return true;
	}
}


