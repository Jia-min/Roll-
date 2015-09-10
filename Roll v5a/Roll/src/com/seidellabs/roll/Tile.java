package com.seidellabs.roll;

import android.graphics.Rect;

import com.seidellabs.framework.Image;

public class Tile {

	private int tileX, tileY, speedX;
	public int type;
	public Image tileImage;

	private Ball ball = GameScreen.getBall();
	private Background bg = GameScreen.getBg1();

	private Rect r;

	public Tile(int x, int y, int typeInt) {
		tileX = x * 40;
		tileY = y * 40;

		type = typeInt;

		r = new Rect();

		if (type == 8) {
			tileImage = Assets.tile;
		} else {
			type = 0;
		}

	}

		public void update() {
			speedX = bg.getSpeedX() * 5;
			tileX += speedX;
			r.set(tileX, tileY, tileX+40, tileY+40);		
			
			if (Rect.intersects(r, Ball.yellowRed) && type != 0) {
				checkVerticalCollision(Ball.top, Ball.bot);
				checkSideCollision(Ball.right);
			}
	
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

	public void checkVerticalCollision(Rect top, Rect bot) {
		if (Rect.intersects(top, r)) {
			ball.setCenterY(tileY + 80);
		}

		if (Rect.intersects(bot, r) && type == 8) {
			ball.setJumped(false);
			ball.setSpeedY(0);
			ball.setCenterY(tileY - 40);
		}
	}

	public void checkSideCollision(Rect right) {
			if (Rect.intersects(right, r)) {
				ball.setCenterX(tileX - 40);
				ball.setSpeedX(0);
			}	
	}
}