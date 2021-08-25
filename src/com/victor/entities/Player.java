package com.victor.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.Camera;
import com.victor.world.World;

public class Player extends Entity{

	public boolean right, up, left, down;
	public double speed = 0.9;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved;
	private int right_dir = 0, left_dir = 1;
	private int dir = right_dir;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	public int ammo = 0;
	
	public double life = 100, maxLife = 100;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for (int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(6 * 16 + (i * 16), 0, 16, 16);	
		}
		for (int i = 0; i < leftPlayer.length; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);	
		}
		
	}
	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		}else if (left && World.isFree((int)(x - speed), (int)y)) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		
		if(up && World.isFree((int)x, (int)(y - speed))) {
			moved = true;
			y -= speed;
		}else if(down && World.isFree((int)x, (int)(y + speed))) {
			moved = true;
			y += speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		this.checkColisionLife();
		this.checkColisionAmmo();
		
		
		Camera.x = Camera.clamp(getX() - (Game.WIDHT / 2), 0, World.WIDTH * 16 - Game.WIDHT);
		Camera.y = Camera.clamp(getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
		
	}
	
	public void checkColisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Bullet){
				if(isColidding(this, atual)) {
					ammo++;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkColisionLife() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof LifePack){
				if(isColidding(this, atual)) {
					Game.player.life += 15;
					Game.entities.remove(i);
					if(Game.player.life > 100) {
						Game.player.life = 100;
					} 
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);	
		}else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
	
}
