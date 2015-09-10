package roll;

import java.awt.Rectangle;

public class Ball {

	// Constants
	final int JUMPSPEED = -15;

	private int centerX = 100;
	private int centerY = 440;
	private boolean jumped = false;
	private int speedX = 0;
	private int speedY = 0;
	
	public static Rectangle top = new Rectangle(0, 0, 0, 0);
	public static Rectangle bot = new Rectangle(0, 0, 0, 0);
	public static Rectangle right = new Rectangle(0, 0, 0, 0);
	public static Rectangle yellowRed = new Rectangle(0, 0, 0, 0);

	private Background bg1 = Main.getBg1();
	private Background bg2 = Main.getBg2();


	public void update() {
		// Updates Y Position
		centerY += speedY;
		
		// Handles Jumping
		speedY += 1;
		
		if (speedY > 3){
			jumped = true;
		}
		
		top.setRect(centerX - 10, centerY - 40, 20, 40);
		bot.setRect(top.getX(), top.getY() + 40, 40, 40);
		right.setRect(top.getX() + 10, top.getY() + 30, 40, 20);
		yellowRed.setRect(centerX - 110, centerY - 110, 180, 180);
	}

	public void jump() {
		if (jumped == false) {
			speedY = JUMPSPEED;
			jumped = true;
		}	
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public boolean isJumped() {
		return jumped;
	}
	
	public int getSpeedX() {
		return speedX;
	}
	
	public int getSpeedY() {
		return speedY;
	}
	
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}
	
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}
	
	public void setSpeedX(int speedX) {
		this.speedY = speedX;
	}
	
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
}
