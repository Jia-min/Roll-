package com.seidellabs.roll;

import com.seidellabs.framework.Game;
import com.seidellabs.framework.Graphics;
import com.seidellabs.framework.Screen;
import com.seidellabs.framework.Graphics.ImageFormat;

public class LoadingScreen extends Screen {
	public LoadingScreen(Game game) {
		
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
		Assets.background = g.newImage("background.jpg", ImageFormat.RGB565);
		Assets.character = g.newImage("character.png", ImageFormat.ARGB4444);
		Assets.character2 = g.newImage("character2.png", ImageFormat.ARGB4444);
		Assets.character3  = g.newImage("character3.png", ImageFormat.ARGB4444);
		Assets.character4  = g.newImage("character4.png", ImageFormat.ARGB4444);
		
		Assets.tile = g.newImage("tile.jpg", ImageFormat.RGB565);
		
		Assets.button = g.newImage("button.png", ImageFormat.RGB565);

		//This is how you would load a sound if you had one.
		//Assets.click = game.getAudio().createSound("explode.ogg");

		
		game.setScreen(new MainMenuScreen(game));

	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.splash, 0, 0);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}
}