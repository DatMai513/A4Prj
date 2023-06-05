package com.mycompany.a4;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class SoundCheckCommand extends Command{
	private GameWorld game;
	
	public SoundCheckCommand (GameWorld gw) {
	super("Sound");
	game = gw;
	}

	public void actionPerformed(ActionEvent evt) {
		
		CheckBox cb = (CheckBox) evt.getComponent();
		if(cb.isSelected()) {
			game.setIsSoundOn(true);
		}
		else
		{
			game.setIsSoundOn(false);
		}
	}
}
