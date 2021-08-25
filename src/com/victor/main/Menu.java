package com.victor.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"Iniciar", "Sobre", "Sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			if(options[currentOption] == "Iniciar") {
				Game.gameState = "NORMAL";
			}else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, Game.WIDHT * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.orange);
		g.setFont(new Font("arial", Font.BOLD, 50));
		g.drawString("CAÇA LIXO", 230, 100);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 25));
		g.drawString("Iniciar", 310, 200);
		g.drawString("Sobre o Jogo", 280, 250);
		g.drawString("Sair", 315, 300);
		
		
		if(options[currentOption] == "Iniciar") {
			g.drawString(">", 310 - 40, 200);
		}else if(options[currentOption] == "Sobre") {
			g.drawString(">", 280 - 40, 250);
		}else if(options[currentOption] == "Sair") {
			g.drawString(">", 315 - 40, 300);
		}
		
	}
	
}
