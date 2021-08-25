package com.victor.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.victor.entities.Bullet;
import com.victor.entities.Enemy;
import com.victor.entities.Entity;
import com.victor.entities.LifePack;
import com.victor.entities.Weapon;
import com.victor.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;	
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					if(pixelAtual == 0xff040404) {
						//Floor/Chão
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}else if (pixelAtual == 0xffffffff) {
						//Parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					}else if(pixelAtual == 0xff000dff) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixelAtual == 0xffc2871f) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_CAIXA);
					}else if(pixelAtual == 0xfffff200) {
						//Munição	
						Game.entities.add(new Bullet(xx*16, yy*16, 16,16, Entity.BULLET_EN));
					}else if(pixelAtual == 0xffff0000) {
						//Inimigo
						Enemy en = new Enemy(xx*16, yy*16, 16,16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enimies.add(en);
					}else if(pixelAtual == 0xffff00db) {
						//Vida
						Game.entities.add(new LifePack(xx*16, yy*16, 16,16, Entity.LIFEPACK_EN));
					}else if (pixelAtual == 0xffffa500) {
						//Arma
						Game.entities.add(new Weapon(xx*16, yy*16, 16,16, Entity.WEAPON_EN));
					}
					else {
						//Floor/Chão
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		 int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		 int y2 = yNext / TILE_SIZE;
		 
		 int x3 = xNext / TILE_SIZE;
		 int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		 
		 int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		 int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		 
		 return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
				 tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
				 tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
				 tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);
		
	}
	
	public void render(Graphics g) {
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;
		
		int xFinal = xStart + (Game.WIDHT >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		for (int xx = xStart; xx <= xFinal; xx++) {
			for (int yy = yStart; yy <= yFinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
