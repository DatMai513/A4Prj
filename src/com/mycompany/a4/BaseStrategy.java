package com.mycompany.a4;

import com.codename1.util.MathUtil;

public class BaseStrategy implements IStrategy {

	@Override
	public void apply(NonPlayerRobot npr) {
		// NPRs focus on reaching bases in order
		// change steering direction accordingly
		
		// The formula to have heading point towards a
		// base's location is:
		// 90 - ideal_heading = arctan(a,b)
		// B = 90 - arctan(a, b);
		
		// create variables to hold both points
		// the target base is the next base that needs to be reached
		int target_base_number = npr.getLastBaseReached() + 1;
		
		Point target_location = Game.gw.getList().getIterator().getBaseLocation(target_base_number);
		Point start_location = npr.getLocation();
		
		// distance a is x2 - x1
		// distance b is y2 - y1
		double a = (target_location.getX() - start_location.getX());
		double b = (target_location.getY() - start_location.getY());
				
		npr.setHeading(Math.toDegrees(MathUtil.atan2(a, b)));
	}
	
	@Override
	public String toString() {
		return "Base Strategy";
	}
}
