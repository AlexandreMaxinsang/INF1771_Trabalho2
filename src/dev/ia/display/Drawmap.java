package dev.ia.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Drawmap extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private char[][] map;
	private char[][] map2 = null;
	private double energia = 0;
	private double custo = 0;
	private double arrow = 0;
	
	public void draw(char map[][],double energia, double custo, double arrow){
		this.map = new char[map.length][map[0].length];
		for(int linhas=0; linhas < map.length; linhas++){
            for(int coluna=0; coluna < map[0].length; coluna++){
            	this.map[coluna][linhas] = map[linhas][coluna];
            }
	    }
		this.energia = energia;
		this.custo = custo;
		this.arrow = arrow;
	}
	
	public void draw(char map[][], char map2[][],double energia, double custo, double arrow){
		this.map = new char[map.length][map[0].length];
		for(int linhas=0; linhas < map.length; linhas++){
            for(int coluna=0; coluna < map[0].length; coluna++){
            	this.map[coluna][linhas] = map[linhas][coluna];
            }
	    }
		
		this.map2 = new char[map.length][map[0].length];
		for(int linhas=0; linhas < map2.length; linhas++){
            for(int coluna=0; coluna < map2[0].length; coluna++){
            	this.map2[coluna][linhas] = map2[linhas][coluna];
            }
	    }
		this.energia = energia;
		this.custo = custo;
		this.arrow = arrow;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int linhas=0; linhas < map.length; linhas++){
            for(int coluna=0; coluna < map[0].length; coluna++){
            	if(map[linhas][coluna] == 'D'){
            		g.setColor(Color.BLUE);
            		try {
            			Image img = ImageIO.read(new File("images\\D.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == '.'){
            		g.setColor(Color.GREEN);
            		try {
            			Image img = ImageIO.read(new File("images\\L.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == 'P'){
            		g.setColor(Color.BLACK);
            		try {
            			Image img = ImageIO.read(new File("images\\B.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == 'N'){
            		g.setColor(Color.YELLOW);
            		try {
            			Image img = ImageIO.read(new File("images\\N.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == 'E'){
            		g.setColor(Color.YELLOW);
            		try {
            			Image img = ImageIO.read(new File("images\\E.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	else if(map[linhas][coluna] == 'S'){
            		g.setColor(Color.YELLOW);
            		try {
            			Image img = ImageIO.read(new File("images\\S.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	else if(map[linhas][coluna] == 'W'){
            		g.setColor(Color.YELLOW);
            		try {
            			Image img = ImageIO.read(new File("images\\W.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	else if(map[linhas][coluna] == 'O'){
            		g.setColor(Color.YELLOW);
            		try {
            			Image img = ImageIO.read(new File("images\\O.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == 'T'){
            		g.setColor(Color.CYAN);
            		try {
            			Image img = ImageIO.read(new File("images\\T.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	else if(map[linhas][coluna] == 'd') {
            		g.setColor(Color.MAGENTA);
            		try {
            			Image img = ImageIO.read(new File("images\\dd.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	else if(map[linhas][coluna] == 'U') {
            		g.setColor(Color.ORANGE);
            		try {
            			Image img = ImageIO.read(new File("images\\P.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	else if(map[linhas][coluna] == 'L') {
            		g.setColor(Color.GREEN);
            		g.fillRect(20*linhas, 20*coluna, 20, 20);
            	}
            	else if(map[linhas][coluna] == 'K') {
            		g.setColor(Color.BLACK);
            		g.fillRect(20*linhas, 20*coluna, 20, 20);
            	}
            }
	    }
		
		if(map2!=null){
			for(int linhas=0; linhas < map2.length; linhas++){
	            for(int coluna=0; coluna < map2[0].length; coluna++){
	            	if(map2[linhas][coluna] == 'I'){
	            		g.setColor(Color.YELLOW);
	            		try {
	            			Image img = ImageIO.read(new File("images\\I.jpg"));
	            			g.drawImage(img, 20*linhas, 20*coluna, null);
	            		} catch (IOException e) {
	            			// TODO Auto-generated catch block
	            			e.printStackTrace();
	            		}
	            	}
	            	else if(map2[linhas][coluna] == 'L') {
	            		try {
	            			Image img = ImageIO.read(new File("images\\L.jpg"));
	            			g.drawImage(img, 20*linhas, 20*coluna+250, null);
	            			//g.fillRect(20*linhas, 20*coluna, 20, 20);
	            		} catch (IOException e) {
	            			// TODO Auto-generated catch block
	            			e.printStackTrace();
	            		}
	            	}
	            	else if(map2[linhas][coluna] == 'K') {
	            		g.setColor(Color.BLACK);
	            		g.fillRect(20*linhas, 20*coluna+250, 20, 20);
	            	}
	            }
		    }
		}
		
		g.setColor(Color.BLACK);
		String resul = String.format("%.1f",  this.energia);
		g.drawString("Energia: " + resul, 400, 10);
		resul = String.format("%.1f",  this.custo);
		g.drawString("Custo: " + resul, 400, 30);
		resul = String.format("%.1f",  this.arrow);
		g.drawString("Flechas: " + resul, 400, 50);
	}
}
