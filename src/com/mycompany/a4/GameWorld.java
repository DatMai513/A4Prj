package com.mycompany.a4;
import java.util.Random;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.a4.GameObjectCollection.Iterator;

import java.util.Observable;

public class GameWorld extends Observable{
	// FIELDS*********************
	private static GameObjectCollection list;
	private Iterator iter;

	private static Robot player = Robot.getRobot();
	
	private int lives = 3;
	private int time;
	int ms = 0;
	
	// sound fields that are intialized to a empty .wav to avoid null errors
	private String isSoundOn = "OFF";
	/*private BGSound bg = new BGSound("dummy.wav");
	private Sound eat = new Sound("dummy.wav");
	private Sound oof = new Sound("dummy.wav");
	private Sound bloop = new Sound("dummy.wav");*/
	
	// METHODS**************************
	public void init() {
		//code here to create the initial game objects and state
		setList(new GameObjectCollection());
		iter = getList().getIterator();
		
		// Create a robot at base 1
		getList().add(player);
		
		// Create 3 NPRs near base 1 and ensure that not all 3 NPRs have the same strategy
		getList().add(new NonPlayerRobot(new BaseStrategy()));
		getList().add(new NonPlayerRobot(new AttackStrategy()));
		getList().add(new NonPlayerRobot(new BaseStrategy()));
			
		// Create 2 drones
		getList().add(new Drone());
		getList().add(new Drone());
		
		// Create 2 energy stations
		getList().add(new EnergyStation());
		getList().add(new EnergyStation());
		
		//Create 4 bases, and moving the player to the first base
		getList().add(new Base(1));
		getList().add(new Base(2));
		getList().add(new Base(3));
		getList().add(new Base(4));
		
		setChanged();
		notifyObservers();
	}
	
	// Player robot has collided with an energy station
	public void energy(GameObject other) {
		// find the first energy station in the collection
		// Power the player with the energy stations capacity

		// Take the ES's energy
		player.energize((EnergyStation)other);
		// change color and capacity of energy station to show it has been depleted
		other.setColor(ColorUtil.rgb(190, 255, 190));
		((EnergyStation)other).setCapactiy(0);

		// make a new energy station
		getList().add(new EnergyStation());
		
	}
	
	// show player stats
	// lives, time, highest base reached, current energy, current damage
	public void display() {
		System.out.println("Lives:" + this.lives + " Time: " + this.time + 
				" Energy:" + player.getEnergyLevel() + " Damage Taken: " + player.getDamageLevel());
	}
	
	public void map() {
		// first location, color, size, seqNum of all bases
		for (int i = 0; i < getList().size(); i++) {
			if (iter.get(i) instanceof Base) {
				Base temp = (Base)iter.get(i);
				System.out.println(temp);
			}
		}
		
		// show all stats of player
		System.out.println(player);
		
		// show all stats of NPR
		while (iter.hasNext()) {
			// find every NPR
			if (iter.get() instanceof NonPlayerRobot) {
				System.out.println(iter.get());
			}
			iter.setCursor(iter.getCursor() + 1);
		}
		
		// show all drone stats
		for (int i = 0; i < getList().size(); i++) {
			if (iter.get(i) instanceof Drone) {
				Drone temp = (Drone)iter.get(i);
				System.out.println(temp);
			}
		}
		
		// show all energy station stats
		for (int i = 0; i < getList().size(); i++) {
			if (iter.get(i) instanceof EnergyStation) {
				EnergyStation temp = (EnergyStation)iter.get(i);
				System.out.println(temp);
			}
		}
	}
	
	
	// check if the player's energy is still above 0
	// and if the player's health is still in bounds
	// this check should happen at every tick
	public void lifeCheck() {
		if (player.getEnergyLevel() <= 0 || player.getDamageLevel() >= player.getMaximumDamage()) {
			// player loses a life and map is reset
			// if player is on last life, game over
			if (lives == 1) {
				System.out.println("Game over, you failed!");
				exit();
			}
			else {
				lives -= 1;
				getList().clear();
				player.reset();
				init();
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	// maintain the players score
	// if player reaches all 
	public void baseCheck() {	
		// first count how many bases
		int baseCount = 0;
		for (int i = 0; i < getList().size(); i++) {
			if (iter.get(i) instanceof Base) {
				baseCount += 1;			
				}
		}		
		
		// if the player has reached all bases, they win
		if (player.getLastBaseReached() >= baseCount) {
			System.out.println("Congratulations, you won! Total time: " + time);
			exit();
		}
		
		boolean hasWon = false;
		// also check if any NPR has reached every base, player loses
		while (iter.hasNext()) {
			// find every NPR
			if (iter.get() instanceof NonPlayerRobot) {
				// as of a3, there are only 2 strategies
				// so check what strategy the NPR has and switch it to the other
				if (((NonPlayerRobot) iter.get()).getLastBaseReached() >= baseCount) {
					hasWon = true;
				}				
			}
			iter.setCursor(iter.getCursor() + 1);
		}
		
		if (hasWon) {
			// NPR has won
			if (lives == 1)
			{
				// if player is on last life, then game over
				System.out.println("Game Over, a NPR won!");
				exit();
			}
			else {
				// player still has lives so just reset the board
				lives -= 1;
				System.out.println("Too much damage taken. Life lost.");

				player.reset();
				getList().clear();
				init();
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	// accelerate the player
	public void acceleratePlayer() {
		player.accelerate();
		
		// sets the change flag to true
		setChanged();
		// this will call all register observer's update() method
		// resets the flag to false
		notifyObservers();
	}
	
	// turn the player left
	public void turnLeft() {
		player.turnLeft();
		
		setChanged();
		notifyObservers();
	}
	
	// turn the player right
	public void turnRight( ){
		player.turnRight();
		
		setChanged();
		notifyObservers();
	}
	
	// decelerate the player
	public void brake() {
		player.brake();
		
		setChanged();
		notifyObservers();
	}
	
	
	// switch strategies of all NPRs, ensuring that they switch to a new one
	public void switchStrategies() {
		// while the iterator is not at the last element of the collection
		while (iter.hasNext()) {
			// find every NPR
			if (iter.get() instanceof NonPlayerRobot) {
				// as of a3, there are only 2 strategies
				// so check what strategy the NPR has and switch it to the other
				if (((NonPlayerRobot) iter.get()).getStrategy() instanceof AttackStrategy) {
					((NonPlayerRobot) iter.get()).setStrategy(new BaseStrategy());
				}
				else
				{
					((NonPlayerRobot) iter.get()).setStrategy(new AttackStrategy());
				}
				
			}
			iter.setCursor(iter.getCursor() + 1);
		}
		
		setChanged();
		notifyObservers();
	}
	
	// prompt the user with a dialog box to exit
	public void exit() {
		Boolean bOk = Dialog.show("Confirm quit?", "Are you sure you want to quit?", "OK", "Cancel");
		if (bOk) {
		Display.getInstance().exitApplication();
		}
	}
	
	// prompt the user with about information, name, course name, and app version
	public void about() {
		Dialog dlg = new Dialog("About", new BoxLayout(BoxLayout.Y_AXIS));
		TextArea ta = new TextArea("Dat Mai\nCSC133 Spring 2023");
		Button ok = new Button(new Command("OK"));
		
		dlg.add(ta);
		dlg.add(ok);
		dlg.show();
	}
	
	// Print out all key bindings ablregt
	public void help() {
		Dialog dlg = new Dialog("Help", new BoxLayout(BoxLayout.Y_AXIS));
		TextArea ta = new TextArea(
				"a - accelerate\nb - break\nl - turn left\nr - turn right\n");
		Button ok = new Button(new Command("OK"));

		
		dlg.add(ta);
		dlg.add(ok);
		dlg.show();
	}
	
	public int getTime() {
		return time;
	}

	// Time has ticked, which means all objects must move
	// Every tick should check if the game has been won or lost
	public void tick() {
		// A "tick" means 20ms(time_rate) has passed
		// for for every 1000ms that passes, increment time by 1s
		ms = ms + Game.time_rate;
		if ((ms%1000) == 0)
		{
			// 1 second has passed
			time++;
			// Drain the players energy every second
			player.drain();
			ms = 0;
		}

		// check if all bases were reached
		baseCheck();
		// update and maintain player's energy
		lifeCheck();
		
		Iterator i = getList().getIterator();
		Iterator j = getList().getIterator();
		
		// all movable objects move
		while (iter.hasNext()) {
			// check if cursor is on a Movable object
			if (iter.get() instanceof Movable) {
				// move the object
				((Movable)iter.get()).move(Game.time_rate);
				
				// Now that all objects have moved, check if any collisions have happened
				// This check happens by matching every object with every other object
				// 2 new iterators with cursor = 0
	
				i.setCursor(0);
				
				while (i.hasNext()) {
					// j's cursor must be always 1 ahead of i's
					j.setCursor(i.getCursor() + 1);
					while (j.hasNext()) {
		
						// Check if i and j are colliding
						if(i.get().collidesWith(j.get())) {
					
							// A collision has occurred, first check if the two objects 
							// are in each others CL 
							if (i.get().getCL().contains(j.get()) || j.get().getCL().contains(i.get()))
							{
								// If they are, no need to handle a collision
								// Do nothing
								System.out.println("Do nothing");
							}
							else 
							{
								// If they aren't, handle the collision and add them 
								// into each others CL
								i.get().handleCollision(j.get());
								
								i.get().getCL().add(j.get());
								j.get().getCL().add(i.get());
								
								//System.out.println("i CL "+ i.get().getCL());
								//System.out.println("j CL "+ j.get().getCL());
							}
						}
						else
						{
							// if i or j are in each other's CL but not colliding, 
							// this means they just stopped colliding
							if (i.get().getCL().contains(j.get()) || j.get().getCL().contains(i.get()))
							{
								// remove them from the collision list
								i.get().getCL().remove(j.get());
								j.get().getCL().remove(i.get());
							}
						}
						j.setCursor(j.getCursor() + 1);
					}
					i.setCursor(i.getCursor() + 1);
				}
				
				// Check if the object is within bounds after the move
				// If the object's X coordinate is less than the origin
				if (((Movable)iter.get()).getLocation().getX() < (Game.map.getX())) {
					// Set the object's X coordinate to the origin
					((Movable)iter.get()).getLocation().setX(Game.map.getX());
					// Negate the heading of the object so it doesn't run back into the wall
					((Movable)iter.get()).setHeading(-1*(((Movable)iter.get()).getHeading()));
					// Rotate the game object accordingly
					((Movable)iter.get()).rotate((float)((Movable)iter.get()).getHeading());
				}
				
				// If the object's Y coordinate is less than the origin
				if (((Movable)iter.get()).getLocation().getY() < (Game.map.getY())) {
					// Set the object's Y coordinate to the origin
					((Movable)iter.get()).getLocation().setY(Game.map.getY());
					// Negate the heading of the object so it doesn't run back into the wall
					((Movable)iter.get()).setHeading(-1*(((Movable)iter.get()).getHeading() + 180));
					// Rotate the game object accordingly
					((Movable)iter.get()).rotate((float)((Movable)iter.get()).getHeading());
				}
				
				// If the object's X coordinate is more than the map bounds
				if (((Movable)iter.get()).getLocation().getX() > (Game.map.getX()+7+Game.map.getMapWidth())) {
					// Set the object's X coordinate to the map bounds
					((Movable)iter.get()).getLocation().setX(Game.map.getX()+7+Game.map.getMapWidth());
					// Negate the heading of the object so it doesn't run back into the wall
					((Movable)iter.get()).setHeading(-1*(((Movable)iter.get()).getHeading()));
					// Rotate the game object accordingly
					((Movable)iter.get()).rotate((float)((Movable)iter.get()).getHeading());
				}
				
				// If the object's Y coordinate is more than the map bounds
				if (((Movable)iter.get()).getLocation().getY() > (Game.map.getY()+7+Game.map.getMapHeight())) {
					// Set the object's Y coordinate to the map bounds
					((Movable)iter.get()).getLocation().setY(Game.map.getY()+7+Game.map.getMapHeight());
					// Negate the heading of the object so it doesn't run back into the wall
					((Movable)iter.get()).setHeading((((Movable)iter.get()).getHeading() - 180));
					// Rotate the game object accordingly
					((Movable)iter.get()).rotate((float)((Movable)iter.get()).getHeading());
				}
			}
			// increment the cursor
			iter.setCursor(iter.getCursor() + 1);
		}
		
		/*if (getIsSoundOn() == "ON") {
			getBg().play();
		}
		else
		{
			getBg().pause();
		}*/
		
		setChanged();
		notifyObservers();
	}
	

	public Robot getPlayer() {
		return player.getRobot();
	}

	public int getLives() {
		return lives;
	}
	
	/*public void createSounds() {
		bg = new BGSound("BG.wav");
		oof = new Sound("oof.wav");
		eat = new Sound("eat.wav");
		bloop = new Sound("bloop.wav");
	}*/
	
	
	public void setIsSoundOn(boolean b) {
		if (b) {
			isSoundOn = "ON";
		}
		else {
			isSoundOn = "OFF";
		}
		
		setChanged();
		notifyObservers();
	}
	
	public String getIsSoundOn() {
		return isSoundOn;
	}

	public void setLives(int lives) {
		this.lives = lives;
		setChanged();
		notifyObservers();
	}

	public GameObjectCollection getList() {
		return list;
	}

	public void setList(GameObjectCollection list) {
		GameWorld.list = list;
	}
	
	/*public Sound eatSound() {
		return eat;
	}

	public Sound oofSound() {
		return oof;
	}
	
	public Sound bloopSound() {
		return bloop;
	}

	public BGSound getBg() {
		return bg;
	}*/

}
