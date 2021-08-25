package com.victor.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.victor.entities.Enemy;
import com.victor.entities.Entity;
import com.victor.entities.Player;
import com.victor.graficos.SpriteSheet;
import com.victor.graficos.UI;
import com.victor.world.World;

public class Game extends Canvas implements Runnable, KeyListener{

	/**	
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDHT = 240, HEIGHT = 160;
	public static final int SCALE = 3;
	private BufferedImage image;
	
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	
	public static List<Entity> entities;
	public static List<Enemy> enimies;
	public static SpriteSheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;
	public static String gameState = "MENU";
	public Menu menu;
	
	public UI ui;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDHT*SCALE, HEIGHT*SCALE));
		initFrame();
		//Iniciando Objeto
		ui = new UI();
		image = new BufferedImage(WIDHT, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enimies = new ArrayList<Enemy>();
		spritesheet = new SpriteSheet("/sprites.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/map.png");
		
		menu = new Menu();
	}
	
	public void initFrame() {
		frame = new JFrame("Game 01");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void update() {
		if(gameState == "NORMAL") {
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				if(e instanceof Player) {
					//Estou dando tick no player.
					
				}
				e.tick();
			}	
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 25) {
				this.framesGameOver = 0;
				if(showMessageGameOver) {
					this.showMessageGameOver = false;
				}else {
					this.showMessageGameOver = true;
				}
			}
		}else if(gameState == "MENU") {
			menu.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDHT, HEIGHT);
		
		world.render(g);
		
		//Renderização do Player
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDHT*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 22));
		g.setColor(Color.white);
		g.drawString("Lixos recolhidos: " + player.ammo, 520, 33);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDHT*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.setColor(Color.white);
			g.drawString("Game Over", 230, 220);
			g.setFont(new Font("arial", Font.BOLD, 30));
			if(showMessageGameOver)
				g.drawString(">> Pressione Enter <<", 200, 280);
		}else if (this.gameState == "MENU") {
			menu.render(g);
		}
		bs.show();		
		
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS " + frames);
				frames = 0;
				timer += 1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//Executar tal ação
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//Executar tal ação
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//Exercutar tal ação
			player.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//Executar tal ação
			player.down = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(this.gameState == "MENU") {
				menu.enter = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//Executar tal ação
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//Executar tal ação
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//Exercutar tal ação
			player.up = false;
			
			if (this.gameState == "MENU") {
				menu.up = true;
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//Executar tal ação
			player.down = false;
			
			if (this.gameState == "MENU") {
				menu.down = true;
			}
			
		}
		
	}

}
