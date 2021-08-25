package com.victor.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.Camera;
import com.victor.world.World;

public class Enemy extends Entity{

	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private double speed = 0.6;
	private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;
	private int right_dir = 0, left_dir = 1;
	private int dir = right_dir;
	
	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		
		for (int i = 0; i < rightEnemy.length; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(6 * 16 + (i * 16), 16, 16, 16);	
		}
		for (int i = 0; i < leftEnemy.length; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);	
		}
	}
	
	public void tick() {
		maskX = 5;
		maskY = 5;
		maskW = 7;
		maskH = 7;
		if(isColiddingWithPlayer() == false) {
			if(Game.rand.nextInt(100) < 50) {
				if((int)x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
						&& !isColidding((int) (x + speed), this.getY())) {
					dir = right_dir;
					x += speed;
				}else if ((int)x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
						&& !isColidding((int) (x - speed), this.getY())) {
					dir = left_dir;
					x -= speed;
				}
			
				if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed))
						&& !isColidding(this.getX(), (int)(y + speed))) {
					y += speed;
				}else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
						&& !isColidding(this.getX(), (int) (y - speed))) {
					y -= speed;
				}
			}
		}else {
			if(Game.rand.nextInt(100) < 50) {
			Game.player.life -= Game.rand.nextInt(5);
			//System.out.println("Vida: " + Game.player.life);
			if(Game.player.life <= 0) {
				//Game Over
				Game.gameState = "GAME_OVER";
			}
			}
		}
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}

	}
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xNext, int yNext){
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
		
		for(int i = 0; i < Game.enimies.size(); i++) {
			Enemy e = Game.enimies.get(i);
			if (e == this) {
				continue;
			}
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
			
		}
		return false;
	}
	
	public void render(Graphics g) {
		
		if(dir == right_dir) {
			g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);	
		}else if(dir == left_dir) {
			g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		/*
		g.setColor(Color.BLUE);
		g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH); */
	}
	
}
