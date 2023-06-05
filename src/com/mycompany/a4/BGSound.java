package com.mycompany.a4;

import java.io.InputStream;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

public class BGSound implements Runnable{
	// FIELDS********************************
	private Media m;
	// CONSTRUCTORS********************************
	public BGSound(String fileName) {
		while (m == null) {
			try 
			{
				InputStream is = Display.getInstance().getResourceAsStream(
						getClass(), "/" + fileName);
				m = MediaManager.createMedia( is , "audio/wav", this);
			} 
			catch (Exception err)
			{ 
				err.printStackTrace(); 
			}
		}
	}
	// METHODS********************************
	public void play() {
		m.play();
	}
	
	public void pause() {
		m.pause();
	}
	
	@Override
	public void run() {
		m.setTime(0);
		play();
	}
}
