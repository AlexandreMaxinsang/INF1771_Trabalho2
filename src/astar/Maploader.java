package astar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Maploader {
	
	private char[][] map = new char[12][12];
	
	public Maploader() throws IOException{
		int linhaMatriz = 0;
		FileReader arq = new FileReader("res\\map1.txt");
		BufferedReader lerArq = new BufferedReader(arq);

		String linha = lerArq.readLine();
		while (linha != null) {
			for (int coluna = 0; coluna < linha.length(); coluna++) {
				map[linhaMatriz][coluna] = linha.charAt(coluna);
			}
			linhaMatriz++;
			linha = lerArq.readLine();
		}
		
		arq.close();
	}
	
	public char[][] getmap(){
		return map;
	}
}
