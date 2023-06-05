package com.mycompany.a4;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class PauseCommand extends Command{
    private Game g;

    public PauseCommand(Game game) {
        super("Pause");                 
        g = game;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev){
    	// Pause and unpause the game
        ((Game) g).pause();       
    }
}