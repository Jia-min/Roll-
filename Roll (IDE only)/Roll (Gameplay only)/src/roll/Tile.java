package roll;

import java.awt.Image;
import java.awt.Rectangle;

public class Tile {

	private int tileX, tileY, speedX, type;
	public Image tileImage;

	private Ball ball = Main.getBall();
	private Background bg = Main.getBg1();

	private Rectangle tile;

	public Tile(int x, int y, int typeInt) {
		tileX = x * 40;
		tileY = y * 40;

		type = typeInt;
		tile = new Rectangle();

		if (type == 8) {
			tileImage = Main.tile;
		} else {
			type = 0;
		}

	}

	public void update() {
		if (tile.intersects(Ball.yellowRed) && type != 0) {
			checkVerticalCollision(Ball.top, Ball.bot);
			checkSideCollision(Ball.right);
		}
		speedX = bg.getSpeedX() * 5;
		tileX += speedX;
		tile.setBounds(tileX, tileY, 40, 40);
	}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}

	public void checkVerticalCollision(Rectangle top, Rectangle bot) {
		if (top.intersects(tile)) {
			ball.setCenterY(tileY + 80);
		}
		
		if (bot.intersects(tile)) {
			ball.setJumped(false);
			ball.setSpeedY(0);
			ball.setCenterY(tileY - 40);
		}
	}
	
    public void checkSideCollision(Rectangle right) {
        if (right.intersects(tile)) {
        	ball.setCenterY(tileY - 40);
        }         
    }
}