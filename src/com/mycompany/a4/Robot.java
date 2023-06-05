package com.mycompany.a4;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;

public class Robot extends Movable implements ISteerable {
	// FIELDS*********************************
	private static final Robot instance = new Robot();
	// All robots have the same size and color of a hardcoded value
	// r g b values to change Robot color on damage taken
	int r, g, b;
	
	// Traits of a robot
	// steering direction indicates the change to it's heading, as in steering direction
	// is added to heading, can only be increments of 5 and up to a maximum of 40
	private int steeringDirection;
	// upper bound of speed, different robots can have different maximums
	private int maximumSpeed;
	// how much energy a robot has left
	private int energyLevel;
	// how fast energy depletes per second, this is a hardcoded value for all robots
	private static final int energyConsumptionRate = 2;
	// health of a robot
	private int damageLevel;
	// how much damage a robot can take, maximum speed is the ratio of current damage/maximum damage
	private int maximumDamage;
	// the last base the robot reached
	private int lastBaseReached;
	
	private int degrees, oldDeg, newDeg;
	
	private Body body;
	private Arms[] arms;
	
	private double armOffset = 0;
	private double armIncrement = 1;
	private double maxArmOffset = 10;
	
	// CONSTRUCTORS********************************
	protected Robot() {
		// starting energy is 100
		reset();
		maximumDamage = 100000;
		maximumSpeed = 15;

		setColor(ColorUtil.rgb(r, g, b)); 
		setSize(75);
		
		degrees = 0;
		oldDeg = 0;
		newDeg = 0;
		
		body = new Body(getSize());
		arms = new Arms[4];
		
		Arms a0 = new Arms(getSize());
		arms[0] = a0;
		a0.rotate(180);
		
		Arms a1 = new Arms(getSize());
		arms[1] = a1;
		a1.rotate(90);
		
		Arms a2 = new Arms(getSize());
		arms[2] = a2;
		a2.rotate(270);
		
		Arms a3 = new Arms(getSize());
		arms[3] = a3;

	}
	
	// METHODS************************************
	// Either the player took too much damage or ran out of energy
	// reset the player's life, energy, position
	public void reset() {
		setLocation(new Point(500,500));
		setEnergyLevel(50);
		setDamageLevel(0);
		setSteeringDirection(0);
		
		//setHeading(rand.nextInt(361));
		setHeading(0);
		
		setSpeed(0);
		setLastBaseReached(0);
		
		r = 255;
		g = 0;
		b = 0;
		
		setColor(ColorUtil.rgb(r, g, b));
		getRotation().setIdentity();
	}
	
	public void move(int elapsed) {
		// Update heading before moving
		oldDeg = (int)getHeading();
		setHeading(getHeading() + steeringDirection);
		steeringDirection = 0;
		
		newDeg = (int)getHeading();
		degrees = newDeg - oldDeg;
		rotate(-degrees);
		
		// adjust speed for the refresh rate of 20ms
		// 50 refreshes per 1 second
		int s = (int) ((getSpeed() * elapsed)/50);
		
		// New location is the formula (x,y) + (cos(T)*speed, sin(T)*speed)
		// where T = 90 - heading
		translate((float)Math.cos(Math.toRadians(90 - this.getHeading())) * s, 
				(float)Math.sin(Math.toRadians(90 - this.getHeading())) * s);
	}
	
	public void collision(GameObject other) {
		// increase g and b to make color lighter red
		g += 0.1;
		b += 0.1;
		setColor(ColorUtil.rgb(r, g, b)); 
		
		// damage taken depends on if it was a drone or a robot
		
		// deal damage to both objects
		if (other instanceof Robot) {
			this.setDamageLevel(this.getDamageLevel() + 10);
			((Robot)other).setDamageLevel(((Robot)other).getDamageLevel() + 10);
		}
		else {
			// drones deal half the damage of a robot
			damageLevel += 5;
		}
		// speed is adjusted based on damage
		// new speed is the ratio of health left
		// i.e 10/50 damage is taken means 80% of speed
		setSpeed(getSpeed()*(1 - damageLevel/maximumDamage));
	}

	// Player robot has collided with a base
	public void checkBase(GameObject other) {
		// check if the base is exactly 1 greater than the last base it reached
		if (((Base)other).getSeqNum() == (1+this.getLastBaseReached())) {
			// next base was reached
			this.setLastBaseReached(((Base)other).getSeqNum());
		}
	}
	
	// Player robot has collided with an energy station
	public void energize(EnergyStation e) {
		energyLevel = (getEnergyLevel() + e.getCapacity());
	}
	
	// Drain energy based off of energy rate
	public void drain() {
		setEnergyLevel(getEnergyLevel() - energyConsumptionRate);
	}
		
	// Draw the robot, a filled square
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
		
        body.draw(g, pCmpRelPrnt, pCmpRelScreen);
        
        for (int i = 0; i < 4; i++) {
        	arms[i].draw(g, pCmpRelPrnt, pCmpRelScreen);
        }
       
		
		g.setTransform(original);
	}
	
	// A collision has occurred with the player (Robot)
	// Energy Station: drain the energy station and power the player
	// Base: check if it is the right base, increment LastBaseReached
	// NPR: take damage (full damage)
	// Drone: take damage (half damage)
	@Override
	public void handleCollision(GameObject other) {
		// If the other object is an Energy station, 
		// drain energy and give it to the player
		
		if (other instanceof EnergyStation) {
			if (((EnergyStation)other).getCapacity() > 0)
			Game.gw.energy(other);
			/*if (!(this instanceof NonPlayerRobot))
			{
				if (Game.gw.getIsSoundOn() == "ON") {
					Game.gw.eatSound().play();
				}
			}*/
		}
		// If the other object is a base,
		// Update last base reached for the player 
		if (other instanceof Base)
		{
			// Check if the base has the right base number
			checkBase(other);
			/*if (!(this instanceof NonPlayerRobot))
			{
				if (Game.gw.getIsSoundOn() == "ON") {
					Game.gw.bloopSound().play();
				}
			}*/
		}
		// If the other object is a Robot,
		// deal damage to both 
		if (other instanceof Robot)
		{
			collision(other);
			/*if (!(this instanceof NonPlayerRobot))
			{
				if (Game.gw.getIsSoundOn() == "ON") {
					Game.gw.oofSound().play();
				}
			}*/

		}
		if (other instanceof Drone)
		{
			collision(other);
			/*if (!(this instanceof NonPlayerRobot))
			{
				if (Game.gw.getIsSoundOn() == "ON") {
					Game.gw.oofSound().play();
				}
			}*/

		}
	}
	
	public void updateLTs()
	{
		for (Arms f:arms) {
			f.translate ((float)0, (float)armIncrement);
		}
		armOffset += armIncrement ;
		// reverse direction of flame movement for next time if we've hit the max
		if (Math.abs(armOffset) >= maxArmOffset) {
			armIncrement *= -1 ;
		} 
	}
	
	// All steerable interface methods
	@Override
	public void turnRight() {
		// turn right on compass by 5 degrees
		// can not surpass 40 degrees steering direction
		if (steeringDirection == 40) {
			// do nothing
			return;
		}
		else {
			steeringDirection = -5;
		}
	}

	@Override
	public void turnLeft() {
		// turn left on compass by 5 degrees
		if (steeringDirection == -40) {
			// do nothing
			return;
		}
		else {
			steeringDirection = 5;
		}
	}

	@Override
	public void accelerate() {
		// can not go over speed limit, do nothing if at max speed
		if (getSpeed() == maximumSpeed) {
			return;
		}
		this.setSpeed(this.getSpeed() + 2);
	}

	@Override
	public void brake() {
		// if a player's speed is 0, do nothing
		if (this.getSpeed() == 0) {
			return;
		}
		
		// reduce speed
		this.setSpeed(this.getSpeed() - 2);	
	}
	
	public static Robot getRobot() {
		return instance;
	}
	
	public int getEnergyLevel() {
		return energyLevel;
	}
	public int getDamageLevel() {
		return damageLevel;
	}
	public int getMaximumSpeed() {
		return maximumSpeed;
	}
	public int getMaximumDamage() {
		return maximumDamage;
	}
	public int getLastBaseReached() {
		return lastBaseReached;
	}
	public int getSteeringDirection() {
		return steeringDirection;
	}
	public void setLastBaseReached(int i) {
		lastBaseReached = i;
	}
	public void setSteeringDirection(int i) {
		steeringDirection = i;
	}
	public void setEnergyLevel(int i) {
		energyLevel = i;
	}
	public void setMaximumDamage(int i) {
		maximumDamage = i;
	}
	public void setDamageLevel(int i) {
		damageLevel = i;
	}
	
	@Override
	public String toString() {
		return "Robot: loc=" + (double)Math.round(getLocation().getX()) + "," + (double)Math.round(getLocation().getY()) +
			colorToString() + " heading=" + getHeading() + " speed=" + getSpeed() + 
			" size=" + getSize() + " maxSpeed=" + getMaximumSpeed() + " steeringDirection=" +
			getSteeringDirection() + " energylevel=" + getEnergyLevel() + " damageLevel=" + 
			getDamageLevel();
	}
}
