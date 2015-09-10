package roll;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import roll.framework.Animation;

public class Main extends Applet implements Runnable, KeyListener {
	
	// Two states of game
	enum GameState {
		Running, Dead
	}
	
	// Game begins running
    GameState state = GameState.Running;
    
    // Variables
	private static Ball ball;
	private Image image, currentSprite, character, character2, character3, character4, background;
	public static Image tile;
	private Graphics second;
	private URL base;
	private static Background bg1, bg2;
	private Animation rolling;
	private ArrayList<Tile> tilearray = new ArrayList<Tile>();

	// Basic window settings
	public void init() {
		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Roll");
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Image Setups
		character = getImage(base, "data/character.png");
		character2 = getImage(base, "data/character2.png");
		character3 = getImage(base, "data/character3.png");
		character4 = getImage(base, "data/character4.png");

		background = getImage(base, "data/background.jpg");
		tile = getImage(base, "data/tile.jpg");

		rolling = new Animation();
		rolling.addFrame(character, 40);
		rolling.addFrame(character2, 40);
		rolling.addFrame(character3, 40);
		rolling.addFrame(character4, 40);

		currentSprite = rolling.getImage();
	}

	// Game start
	public void start() {
		bg1 = new Background(0, 0);
		bg2 = new Background(2560, 0);
		ball = new Ball();
		
		// Initialize Tiles
		try {
			loadMap("data/map1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread thread = new Thread(this);
		thread.start();
	}
	
	// Read tilemap
	private void loadMap(String filename) throws IOException {
		ArrayList lines = new ArrayList();
		int width = 0;
		int height = 0;

		BufferedReader reader = new BufferedReader(new FileReader(filename));
		while (true) {
			String line = reader.readLine();
			// no more lines to read
			if (line == null) {
				reader.close();
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

	// Run game
	public void run() {
		if (state == GameState.Running) {
			while (true) {
				ball.update();
				currentSprite = rolling.getImage();
				updateTiles();
				bg1.update();
				bg2.update();
				animate();
				repaint();
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ball.getCenterY() > 500 || ball.getCenterX() < 100) {
					state = GameState.Dead;
				}
			}
		}		
	}
	
	// Animates ball
	public void animate() {
		rolling.update(10);
	}

	// Updates game
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}

	@Override
	public void paint(Graphics g) {
		if (state == GameState.Running) {
			g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
			g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
			paintTiles(g);
			g.drawImage(currentSprite, ball.getCenterX() - 40, ball.getCenterY() - 40, this);
			//g.drawRect((int)ball.top.getX(), (int)ball.top.getY(), (int)ball.top.getWidth(), (int)ball.top.getHeight());
			//g.drawRect((int)ball.bot.getX(), (int)ball.bot.getY(), (int)ball.bot.getWidth(), (int)ball.bot.getHeight());
			//g.drawRect((int)ball.right.getX(), (int)ball.right.getY(), (int)ball.right.getWidth(), (int)ball.right.getHeight());
			//g.drawRect((int)ball.yellowRed.getX(), (int)ball.yellowRed.getY(), (int)ball.yellowRed.getWidth(), (int)ball.yellowRed.getHeight());
		} else if (state == GameState.Dead) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 480);
			g.setColor(Color.WHITE);
			g.drawString("Game Over", 360, 240);
		}
	}
	
	// Updates tiles
	private void updateTiles() {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}
	}
	
	// Paints tiles
	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY(), this);
		}
	}

	// Detect keypress of spacebar
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			ball.jump();
			break;
		}
	}

	// Detect release of spacebar
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			break;
		}
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public static Background getBg1() {
		return bg1;
	}

	public static Background getBg2() {
		return bg2;
	}

	public static Ball getBall() {
		return ball;
	}
}