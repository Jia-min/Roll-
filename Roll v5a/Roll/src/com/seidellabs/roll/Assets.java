package com.seidellabs.roll;

import com.seidellabs.framework.Image;
import com.seidellabs.framework.Music;
import com.seidellabs.framework.Sound;

public class Assets {
	
	public static Image menu, splash, background, character, character2, character3, character4; 
	public static Image tile;
	public static Image button;
	public static Sound click;
	public static Music theme;
	
	public static void load(SampleGame sampleGame) {
		// TODO Auto-generated method stub
		theme = sampleGame.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();
	}
	
}
