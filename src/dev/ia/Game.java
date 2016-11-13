package dev.ia;

import dev.ia.display.Display;
import dev.ia.display.Drawmap;

public class Game {
	private Display display;
	Drawmap d = new Drawmap();
	
	public Game(String title, int width, int height){
		display = new Display(title, width, height);
	}
	
	public void printmap(char map[][],double energia,double custo,double arrow){
		d.draw(map,energia,custo,arrow);
		display.add(d);
	}
	
	public void printmap(char map[][], char map2[][],double energia,double custo,double arrow){
		d.draw(map,map2,energia,custo,arrow);
		display.add(d);
	}
	
}
