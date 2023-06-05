package com.mycompany.a4;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.UITimer;
import com.mycompany.a4.GameObjectCollection.Iterator;

public class ButtonCommand extends Command {
	// FIELDS**************************************
	private static final ButtonCommand accelerate = new ButtonCommand("Accelerate");
	private static final ButtonCommand left = new ButtonCommand("Left");
	private static final ButtonCommand changeStrategies = new ButtonCommand("Change Strategies");
	private static final ButtonCommand brake = new ButtonCommand("Brake");
	private static final ButtonCommand right = new ButtonCommand("Right");

	private static final ButtonCommand position = new ButtonCommand("Position");
	private static final ButtonCommand pause = new ButtonCommand("Pause");
	private static final ButtonCommand map = new ButtonCommand("Map");
	
	private static final ButtonCommand sound = new ButtonCommand("Sound");
	private static final ButtonCommand help = new ButtonCommand("Help");
	private static final ButtonCommand about = new ButtonCommand("About");
	private static final ButtonCommand exit  = new ButtonCommand("Exit");
	
	// CONSTRUCTOR*****************************
	private ButtonCommand(String command) {
		super(command);
	}
	
	// METHODS*****************************
	public void actionPerformed(ActionEvent ev) {
		switch(getCommandName()) {
			
			case "Accelerate":
				// accelerate the robot
				Game.gw.acceleratePlayer();
				break;
				
			case "Left":
				// change the robots steering direction by 5 degrees to the left
				Game.gw.turnLeft();
				break;
				
			case "Change Strategies":
				// switch strategies of all NPRs
				Game.gw.switchStrategies();
				System.out.println("Strategies of NPRs switched");
				break;
				
			case "Brake":
				// reduce the robot's speed
				Game.gw.brake();
				break;
				
			case "Right":
				// change the robot's steering direction by 5 degrees to the right
				Game.gw.turnRight();
				break;
				
			case "Position":
				Iterator k = Game.gw.getList().getIterator();
				while(k.hasNext())
				{
					if (k.get() instanceof Fixed)
					{
						if (((Fixed) k.get()).isSelected())
						{
							// An item is selected
							// Allow positoin to be clicked
							Game.map.setPositionClicked(true);
						}
						else
						{
							// No item is selected
							// Do not let position be clicked
							// Do nothing
						}
					}
			
					k.setCursor(k.getCursor() + 1);
				}
			
				
					
				break;
				
			case "Pause":
				break;
				
			case "Sound":
				break;
				
			case "Help":
				Game.gw.help();
				break;
				
			case "Exit":
				Game.gw.exit();
				break;
				
			case "About":
				Game.gw.about();
				break;
				
			case "Map":
				Game.gw.map();
				break;
				
			default:
				System.out.println("Default command triggered!");
		}
	}

	public static ButtonCommand getAccelerate() {
		return accelerate;
	}

	public static ButtonCommand getLeft() {
		return left;
	}

	public static ButtonCommand getChangeStrategies() {
		return changeStrategies;
	}

	public static ButtonCommand getBrake() {
		return brake;
	}

	public static ButtonCommand getRight() {
		return right;
	}

	public static ButtonCommand getPosition() {
		return position;
	}

	public static ButtonCommand getSound() {
		return sound;
	}

	public static ButtonCommand getHelp() {
		return help;
	}

	public static ButtonCommand getAbout() {
		return about;
	}

	public static ButtonCommand getExit() {
		return exit;
	}

	public static ButtonCommand getMap() {
		return map;
	}

	public static ButtonCommand getPause() {
		return pause;
	}
	
	

}
