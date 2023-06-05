package com.mycompany.a4;

import java.util.Observable;
import java.util.Observer;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;

public class ScoreView extends Container implements Observer {
	// FIELDS***************************
	Label l1, l2, l3, l4, l5, l6;
	Label r1, r2, r3, r4, r5, r6;
	
	// CONSTRUCTORS*********************
	// Add labels to the score using built-in CN1's built in Label class
	public ScoreView() {
		setLayout(new FlowLayout(Component.CENTER));
		
		/* time = "Time: " + Game.gw.getTime();
		lives = "Lives: " + Game.gw.getLives();
		base = "Last Base Reached: " + Game.gw.getPlayer().getLastBaseReached();
		energy = "Energy Level: " + Game.gw.getPlayer().getEnergyLevel();
		dmg = "Damage Level: " + Game.gw.getPlayer().getDamageLevel();
		sound = "Sound: "; */
	
		l1 = new Label("Time:");
		r1 = new Label("" + Game.gw.getTime());
		
		l2 = new Label("Lives:");
		r2 = new Label("" + Game.gw.getLives());
		
		l3 = new Label("Last Base Reached: ");
		r3 = new Label("" + Game.gw.getPlayer().getLastBaseReached());
		
		l4 = new Label("Energy Level:");
		r4 = new Label("" + Game.gw.getPlayer().getEnergyLevel());
		
		l5 = new Label("Damage Level:");
		r5 = new Label("" + Game.gw.getPlayer().getDamageLevel());
		
		l6 = new Label("Sound:"); 
		r6 = new Label("Off");
		
	
		
		addAll(l1, r1, l2, r2, l3, r3, l4, r4, l5, r5, l6, r6);
		
		rightBlue(r1);
		rightBlue(r2);
		rightBlue(r3);
		rightBlue(r4);
		rightBlue(r5);
		rightBlue(r6);
		
		leftBlue(l1);
		leftBlue(l2);
		leftBlue(l3);
		leftBlue(l4);
		leftBlue(l5);
		leftBlue(l6);
		
		revalidate();
		repaint();
	}
	
	// METHODS**************************
	@Override
	public void update(Observable o, Object data) {
		
		int time = ((GameWorld)o).getTime();
		r1.setText(Integer.toString(time));	
		r2.setText("" + ((GameWorld)o).getLives());
		int base = ((GameWorld)o).getPlayer().getLastBaseReached();
		r3.setText(Integer.toString(base));		
		r4.setText("" + ((GameWorld)o).getPlayer().getEnergyLevel());
		r5.setText("" + ((GameWorld)o).getPlayer().getDamageLevel());
		r6.setText("" + ((GameWorld)o).getIsSoundOn());
		
		revalidate();
	}
	
	private void rightBlue(Label label) {
		label.getStyle().setFgColor(ColorUtil.BLUE);
	}
	
	private void leftBlue(Label label) {
		label.getStyle().setFgColor(ColorUtil.BLUE);
		label.getStyle().setPaddingRight(5);
		label.getStyle().setPaddingLeft(5);
	}

}
