package com.seidellabs.roll;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.graphics.Color;
import android.graphics.Paint;

import com.seidellabs.framework.Game;
import com.seidellabs.framework.Graphics;
import com.seidellabs.framework.Image;
import com.seidellabs.framework.Screen;
import com.seidellabs.framework.Input.TouchEvent;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver
	}

	GameState state = GameState.Ready;

	// Variable Setup

	private static Background bg1, bg2;
	private static Ball ball;
	private Image currentSprite, character, character2, character3, character4; 
	private Animation rolling;
	private ArrayList<Tile> tilearray = new ArrayList<Tile>();

	int livesLeft = 1;
	Paint paint, paint2;

	public GameScreen(Game game) {
		super(game);

		// Initialize game objects

		bg1 = new Background(0, 0);
		bg2 = new Background(2560, 0);
		ball = new Ball();

		character = Assets.character;
		character2 = Assets.character2;
		character3 = Assets.character3;
		character4 = Assets.character4;

		rolling = new Animation();
		rolling.addFrame(character, 40);
		rolling.addFrame(character2, 40);
		rolling.addFrame(character3, 40);
		rolling.addFrame(character4, 40);

		currentSprite = rolling.getImage();

		loadMap();

		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		paint2 = new Paint();
		paint2.setTextSize(100);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);
	}

	private void loadMap() {
		ArrayList lines = new ArrayList();
		int width = 0;
		int height = 0;

		Scanner scanner = new Scanner(SampleGame.map);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			// no more lines to read
			if (line == null) {
				break;
			}

			if (!line.startsWith("!")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		height = lines.size();

		for (int j = 0; j < 12; j++) {
			String line = (String) lines.get(j);
			for (int i = 0; i < width; i++) {

				if (i < line.length()) {
					char ch = line.charAt(i);
					Tile t = new Tile(i, j, Character.getNumericValue(ch));
					tilearray.add(t);
				}
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == GameState.Ready)
			updateReady(touchEvents);
		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);
	}

	private void updateReady(List<TouchEvent> touchEvents) {
		if (touchEvents.size() > 0)
			state = GameState.Running;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

		// Touch input
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 35, 35, 480, 800)) {
					ball.jump();
					currentSprite = rolling.getImage();
				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 415, 65, 65)) {
					currentSprite = rolling.getImage();
				}
				if (inBounds(event, 0, 0, 35, 35)) {
					pause();
				}
			}
		}

		if (livesLeft == 0) {
			state = GameState.GameOver;
		}

		ball.update();
		currentSprite = rolling.getImage();
		
		updateTiles();
		bg1.update();
		bg2.update();
		animate();

		if (ball.getCenterY() > 500) {
			state = GameState.GameOver;
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 800, 240)) {

					if (!inBounds(event, 0, 0, 35, 35)) {
						resume();
					}
				}

				if (inBounds(event, 0, 240, 800, 240)) {
					nullify();
					goToMenu();
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 0, 0, 800, 480)) {
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	private void updateTiles() {

		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawImage(Assets.background, bg1.getBgX(), bg1.getBgY());
		g.drawImage(Assets.background, bg2.getBgX(), bg2.getBgY());
		paintTiles(g);

		g.drawImage(currentSprite, ball.getCenterX() - 40, ball.getCenterY() - 40);
		//g.drawRect((int)ball.top.left(), (int)ball.top.getY(), (int)ball.top.getWidth(), (int)ball.top.getHeight());
		//g.drawRect((int)ball.bot.getX(), (int)ball.bot.getY(), (int)ball.bot.getWidth(), (int)ball.bot.getHeight());
		//g.drawRect((int)ball.right.getX(), (int)ball.right.getY(), (int)ball.right.getWidth(), (int)ball.right.getHeight());
		
		
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();
	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			if (t.type != 0) {
				g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
			}
		}
	}

	public void animate() {
		rolling.update(10);
	}

	private void nullify() {
		// Set all variables to null
		paint = null;
		bg1 = null;
		bg2 = null;
		ball = null;
		currentSprite = null;
		character = null;
		character2 = null;
		character3 = null;
		character4 = null;
		rolling = null;

		// Call garbage collector to clean up memory.
		System.gc();
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap to Start.", 400, 240, paint);

	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.button, 0, 285, 0, 0, 65, 65);
		g.drawImage(Assets.button, 0, 350, 0, 65, 65, 65);
		g.drawImage(Assets.button, 0, 415, 0, 130, 65, 65);
		g.drawImage(Assets.button, 0, 0, 0, 195, 35, 35);

	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Resume", 400, 165, paint2);
		g.drawString("Menu", 400, 360, paint2);

	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.BLACK);
		g.drawString("GAME OVER.", 400, 240, paint2);
		g.drawString("Tap to return.", 400, 290, paint);

	}

	public void pause() {
		if (state == GameState.Running)
			state = GameState.Paused;

	}

	public void resume() {
		if (state == GameState.Paused)
			state = GameState.Running;
	}

	public void dispose() {

	}

	public void backButton() {
		pause();
	}

	private void goToMenu() {
		// TODO Auto-generated method stub
		game.setScreen(new MainMenuScreen(game));

	}

	public static Background getBg1() {
		// TODO Auto-generated method stub
		return bg1;
	}

	public static Background getBg2() {
		// TODO Auto-generated method stub
		return bg2;
	}

	public static Ball getBall() {
		// TODO Auto-generated method stub
		return ball;
	}

}