package com.mycompany.a4;
import com.codename1.charts.util.ColorUtil;

import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.util.UITimer;
import com.mycompany.a4.GameObjectCollection.Iterator;
import com.codename1.ui.Toolbar;


public class Game extends Form implements Runnable{
	// FIELDS**********************************
	public static GameWorld gw;
	
	// center container
	public static MapView map;
	// north container
	private ScoreView score;
	// containers for the side menus
	private Container west;
	private Container east;
	private Container south;
	
	// create buttons and attach them to the corresponding side menu containers
	// all west buttons 
	Button wb1 = new Button();
	Button wb2 = new Button();
	Button wb3 = new Button();
	// all east buttons
	Button eb1 = new Button();
	Button eb2 = new Button();
	// all south buttons
	Button sb1 = new Button();
	Button sb2 = new Button();
	
	// toolbar
	private Toolbar tb = new Toolbar();
	public static CheckBox check = new CheckBox();
	
	// timer
	public static UITimer timer;
	// rate that the timer will "tick" in milliseconds
	public static int time_rate = 20;
	public static boolean isPaused = false;

	// CONSTRUCTORS**************************
	public Game() {
		// initialize all fields
		gw = new GameWorld();
		score = new ScoreView();
		map = new MapView();
		
		west = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		east = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		south  = new Container(new FlowLayout(Component.CENTER));
		
		// register views as observers
		gw.addObserver(score);
		gw.addObserver(map);
		
		// establish border layout for Game()
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, score);
		this.add(BorderLayout.WEST, west);
		this.add(BorderLayout.EAST, east);
		this.add(BorderLayout.SOUTH, south);
		this.add(BorderLayout.CENTER, map);
		
		// establish colored line borders on all containers per example GUI from prompt
		map.getAllStyles().setBorder(Border.createLineBorder(6, ColorUtil.rgb(255, 0, 0)));
		east.getAllStyles().setBorder(Border.createLineBorder(4, ColorUtil.BLACK));
		west.getAllStyles().setBorder(Border.createLineBorder(4, ColorUtil.BLACK));
		south.getAllStyles().setBorder(Border.createLineBorder(4, ColorUtil.BLACK));
		
		// establish toolbar layout
		setToolbar(tb);
		this.setTitle("Robo-Track Game");
		
		// left drop down menu
		Command accelerate = ButtonCommand.getAccelerate();
		Command about = ButtonCommand.getAbout();
		Command exit = ButtonCommand.getExit();
		tb.addCommandToSideMenu(accelerate);
		tb.addCommandToSideMenu(about);
		tb.addCommandToSideMenu(exit);
		
		// right help button
		Command help = ButtonCommand.getHelp();
		tb.addCommandToRightBar(help);
		
		//add the CheckBox component as a side menu item
		// hook it up to a command that will proc every time is is
		// ticked and unticked
		CheckBox check = new CheckBox("Sound");
		check.getAllStyles().setBgTransparency(255);
		check.getAllStyles().setBgColor(ColorUtil.GRAY);
		Command soundCheck = new SoundCheckCommand(gw);
		Command pause = new PauseCommand(this);
		check.setCommand(soundCheck);
		tb.addComponentToSideMenu(check);
	

		
		wb1.setCommand(ButtonCommand.getAccelerate());
		wb2.setCommand(ButtonCommand.getLeft());
		wb3.setCommand(ButtonCommand.getChangeStrategies());
		
		eb1.setCommand(ButtonCommand.getBrake());
		eb2.setCommand(ButtonCommand.getRight());
		
		sb1.setCommand(ButtonCommand.getPosition());
		sb2.setCommand(pause);
		
		// add buttons to corresponding containers
		west.add(wb1).add(wb2).add(wb3);
		east.add(eb1).add(eb2);
		south.add(sb1).add(sb2);
		
		setBlueButtons(wb1);
		setBlueButtons(wb2);
		setBlueButtons(wb3);
		setBlueButtons(eb1);
		setBlueButtons(eb2);
	
		setBlueButtons(sb2);
		
		// sb1 should be disabled originally
		colorDisableButtons(sb1);

		
		// Assign all key bindings per prompt
		addKeyListener(97, ButtonCommand.getAccelerate()); // a accelerate
		addKeyListener(98, ButtonCommand.getBrake()); // b brake
		addKeyListener(108, ButtonCommand.getLeft()); // l left
		addKeyListener(114, ButtonCommand.getRight()); // r right
		addKeyListener(109, ButtonCommand.getMap()); // m map

		show();
		
		map.setMapWidth(map.getWidth());
		map.setMapHeight(map.getHeight());
		map.setAllPoints();
		
		gw.init();
		//gw.createSounds();
		
		// set up the timer
		timer = new UITimer(this);
		timer.schedule(time_rate, true, this);
		
		revalidate();
	}

	// METHODS**************************
	private void setBlueButtons(Button button) {
		button.getStyle().setBgTransparency(255);
		button.getStyle().setBgColor(ColorUtil.BLUE);
		button.getStyle().setFgColor(ColorUtil.WHITE);
		button.getStyle().setBorder(Border.createLineBorder(3,ColorUtil.BLACK));
		button.getAllStyles().setPadding(Component.TOP, 10);
		button.getAllStyles().setPadding(Component.BOTTOM, 10);
	}
	
	private void colorDisableButtons(Button button) {
		button.getStyle().setBorder(Border.createLineBorder(3,ColorUtil.BLACK));
		button.getDisabledStyle().setBgColor(ColorUtil.GRAY);
		button.getDisabledStyle().setFgColor(ColorUtil.WHITE);
		button.getAllStyles().setPadding(Component.TOP, 10);
		button.getAllStyles().setPadding(Component.BOTTOM, 10);
	}
	
	@Override // from Runnable interface
	public void run() {
		gw.tick();
	}
	
	public static boolean isPaused()
	{
		return isPaused;
	}
	
	public void setIsPaused(boolean x)
	{
		isPaused = x;
	}
	
	public void pause() {
		if (isPaused)
		{
			// GAME IS UNPAUSED
			// prompt
			System.out.println("Game is unpaused.");
			
			// unpause game
			timer.schedule(time_rate, true, this);
			setIsPaused(false);
			// start music 
			//Game.gw.getBg().play();
			
			// re-add the key listeners f
			addKeyListener(97, ButtonCommand.getAccelerate()); // a accelerate
			addKeyListener(98, ButtonCommand.getBrake()); // b brake
			addKeyListener(108, ButtonCommand.getLeft()); // l left
			addKeyListener(114, ButtonCommand.getRight()); // r right
			addKeyListener(109, ButtonCommand.getMap()); // m map
			
			// enable pilot buttons
			wb1.setEnabled(true);
			wb2.setEnabled(true);
			wb3.setEnabled(true);
			eb1.setEnabled(true);
			eb2.setEnabled(true);
			
			// disable position button
			sb1.setEnabled(false);
			
			// re-color old buttons
			setBlueButtons(wb1);
			setBlueButtons(wb2);
			setBlueButtons(wb3);
			setBlueButtons(eb1);
			setBlueButtons(eb2);
			
			colorDisableButtons(sb1);
			
			// De-select all fixed objects
			Iterator i = Game.gw.getList().getIterator();
			while(i.hasNext())
			{
				if (i.get() instanceof Fixed)
				{
					((Fixed)i.get()).setSelected(false);
				}
				i.setCursor(i.getCursor() + 1);
			}
			
			revalidate();
		}
		else
		{
			// GAME IS PAUSED!
			// prompt
			System.out.println("Game is paused.");
			
			// pause music
			//Game.gw.getBg().pause();
			
			// pause game
			timer.cancel();
			setIsPaused(true);
			
			// remove the key listeners for now
			removeKeyListener(97, ButtonCommand.getAccelerate()); // a accelerate
			removeKeyListener(98, ButtonCommand.getBrake()); // b brake
			removeKeyListener(108, ButtonCommand.getLeft()); // l left
			removeKeyListener(114, ButtonCommand.getRight()); // r right
			removeKeyListener(109, ButtonCommand.getMap()); // m map
			
			// disable pilot buttons
			wb1.setEnabled(false);
			wb2.setEnabled(false);
			wb3.setEnabled(false);
			eb1.setEnabled(false);
			eb2.setEnabled(false);
			
			// enable position button
			sb1.setEnabled(true);
			
			// color disabled buttons
			colorDisableButtons(wb1);
			colorDisableButtons(wb2);
			colorDisableButtons(wb3);
			colorDisableButtons(eb1);
			colorDisableButtons(eb2);
					
			// color enable buttons
			setBlueButtons(sb1);
			
			// De-select all fixed objects
			Iterator i = Game.gw.getList().getIterator();
			while(i.hasNext())
			{
				if (i.get() instanceof Fixed)
				{
					((Fixed)i.get()).setSelected(false);
				}
				i.setCursor(i.getCursor() + 1);
			}
			
			revalidate();
		}
	}

}
