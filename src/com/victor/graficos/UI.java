package com.victor.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.victor.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(8, 5, 70, 9);
		g.setColor(Color.red);
		g.fillRect(8, 5, (int)((Game.player.life / Game.player.maxLife) * 70), 7);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 32, 12);
	}
	
}
