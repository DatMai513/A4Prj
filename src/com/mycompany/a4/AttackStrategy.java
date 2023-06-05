package com.mycompany.a4;

import com.codename1.util.MathUtil;

public class AttackStrategy implements IStrategy {

	@Override
	public void apply(NonPlayerRobot npr) {
		// NPRs focus on attacking the player
		// change steering direction accordingly
		
		// The formula to have heading point towards the 
		// player's location is:
		// 90 - ideal_heading = arctan(a,b)
		// B = 90 - arctan(a, b);
		
		// create variables to hold both points
		Point target_location = Game.gw.getPlayer().getLocation();
		Point start_location = npr.getLocation();
		
		// distance a is x2 - x1
		// distance b is y2 - y1
		double a = (target_location.getX() - start_location.getX());
		double b = (target_location.getY() - start_location.getY());
				
		npr.setHeading(Math.toDegrees(MathUtil.atan2(a, b)));
	}
	
	@Override
	public String toString() {
		return "Attack Strategy";
	}

}
