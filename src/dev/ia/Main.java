package dev.ia;

import java.lang.*;
import java.lang.Integer;

import org.jpl7.*;

//import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import astar.ExampleNode;
import astar.Map2;
import astar.Maploader;

public class Main {

	public static void main(String[] args) throws IOException {
		
		boolean running = true;
		double energia, arrow, score;
		int X = 0, Y = 0;
		char[][] mapChar = (new Maploader()).getmap();
		char[][] mapChar_back = (new Maploader()).getmap();
		char[][] mapChar_astar = (new Maploader()).getmap();
		int tempo = (int)System.currentTimeMillis();
		String msg = null;
		Vector<String> perception = new Vector<String>();
		Vector<Pos> powerup = new Vector<Pos>(); 	//Posi��es powerup
		Vector<Pos> position = new Vector<Pos>(); 	//Posi��es saferoom
		Vector<Pos> postele = new Vector<Pos>(); 	//Posi��es possivelmente teletransporte
		Vector<Pos> pospit = new Vector<Pos>();		 	//Posi��es possivelmente buracos
		Vector<Pos> posenemy = new Vector<Pos>(); 		//Posi��es possivelmente inimigas
		Vector<Pos> posmiscelania = new Vector<Pos>(); 	//Posi��es possivelmente casas com mais de uma percep��o
		Vector<Pos> posini = new Vector<Pos>(); 		//Posi��es iniciais
		
		Random gerador = new Random();
        int aleatorio = 0;
		
		//Carrega o mapa no arquivo pl
		mkmappl(mapChar);
		
		//Def Inicio
		int x0 = 1, y0 = 1;
		mapChar[mapChar.length - y0][x0 -1] = 'I';
		posini.add(new Pos(x0-1, mapChar.length-y0));
		copymatrix(mapChar_astar, mapChar);
		
		//Constroi mapChar_astar
		mapastar(mapChar_astar);
		
		//Janela do game
		Game game = new Game("IA Prolog", 600, 600);
		game.printmap(mapChar,100,0,5);	
		
		//Astar
		Map2<ExampleNode> myMap = null;
		List<ExampleNode> path = null;
		
		//Inicializa o prolog
		Query q1 = new Query("consult", new Term[] {new Atom("prolog\\main.pl")});
	    System.out.println("consult " + (q1.hasSolution() ? "succeeded" : "failed"));
		Query q2 = new Query("initialize");
		System.out.println("Initialize " + (q2.hasSolution() ? "succeeded" : "failed"));
		
		Map<String, Term>[] solution;
		
		
		
		//Inicia as perguntas pro prolog
		while(running == true){
			

			q1 = new Query("perception_tell_KB([Fedor,Brisa,Ouro,Colisao,Teletransporte,Powerup])");
			System.out.println("Perception " + (q1.hasSolution() ? "succeeded" : "failed"));
			
			
			position.clear();
			posenemy.clear();
			postele.clear();
			powerup.clear();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			q2 = new Query("is_saferoom([X,Y])");
			solution = q2.allSolutions();
			if(solution!=null){
				for(int i=0; i<solution.length; i++){
					position.add(new Pos(Integer.parseInt(solution[i].get("X").toString())-1, mapChar.length-Integer.parseInt(solution[i].get("Y").toString())) );
					//System.out.println("is_saferoom( "+solution[i].get("X").toString() +" , "+ solution[i].get("Y").toString() +")");
				}
			}
	
			
		
			q2 = new Query("is_enemy([X,Y])");
			solution = q2.allSolutions();
			if(solution!=null){
				for(int i=0; i<solution.length; i++){
					posenemy.add(new Pos(Integer.parseInt(solution[i].get("X").toString())-1, mapChar.length-Integer.parseInt(solution[i].get("Y").toString())) );
					//System.out.println("is_enemy( "+solution[i].get("X").toString() +" , "+ solution[i].get("Y").toString() +")");
				}
			}

				
			
			q2 = new Query("is_powerup([X,Y])");
			solution = q2.allSolutions();
			if(solution!=null){
				for(int i=0; i<solution.length; i++){
					powerup.add(new Pos(Integer.parseInt(solution[i].get("X").toString())-1, mapChar.length-Integer.parseInt(solution[i].get("Y").toString())) );
					//System.out.println("is_powerup( "+solution[i].get("X").toString() +" , "+ solution[i].get("Y").toString() +")");
				}
			}
			
			
			
			q2 = new Query("ask_KB(X)");
			System.out.println("Asking " + (q2.hasSolution() ? "succeeded" : "failed"));
			solution = q2.allSolutions();
			
			msg = solution[0].get("X").toString();
		

			System.out.println("MSG = " + msg);
	
			if(msg.equals("forward")){
				//Segue em frente
				
				q2 = new Query("forward");
				System.out.println("Forward " + (q2.hasSolution() ? "succeeded" : "failed"));
				
				copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
				
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
				
				System.out.println("agent_X = " + Integer.parseInt(solution[0].get("X").toString()));
				System.out.println("agent_Y = " + Integer.parseInt(solution[0].get("Y").toString()));
				
				/*
				if(X<0 || X>=mapChar.length || Y<0 || Y>=mapChar.length){
					q2 = new Query("ask_KB(X)");
					solution = q2.allSolutions();
					if(solution[0].get("X").toString().equals("rebound"))
						System.out.println("Rebound " + (q2.hasSolution() ? "succeeded" : "failed"));
					q2 = new Query("rebound");
					solution = q2.allSolutions();
					q2 = new Query("agent_location([X,Y])");
					solution = q2.allSolutions();
					X = Integer.parseInt(solution[0].get("X").toString()) - 1;
					Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
				}
				
				System.out.println("X = " + X);
				System.out.println("Y = " + Y);
				*/
				
				//Atualiza Mapa Estrela
				refreshmapstar(mapChar_astar, Y,  X);
				
				//Atualiza a posicao
				mapChar[Y][X] = 'I';
				
				
				//Atualiza o mapa
				q2 = new Query("agent_healthy(X)");
				solution = q2.allSolutions();
				energia = Integer.parseInt(solution[0].get("X").toString());
				
				q2 = new Query("agent_score(X)");
				solution = q2.allSolutions();
				score = Integer.parseInt(solution[0].get("X").toString());
				
				q2 = new Query("agent_arrows(X)");
				solution = q2.allSolutions();
				arrow = Integer.parseInt(solution[0].get("X").toString());
				game.printmap(mapChar, mapChar_astar,energia,score,arrow);
				
				//consoleprint(mapChar);
				
			}
	
			
			if(msg.equals("die")){
				q2 = new Query("die");
				solution = q2.allSolutions();
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				System.out.println("Die " + (q2.hasSolution() ? "succeeded" : "failed"));
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_back.length - Integer.parseInt(solution[0].get("Y").toString());
				mapChar[Y][X] = 'I';
				running = false;
			}
			
			if(msg.equals("grab")){
				q2 = new Query("grab");
				solution = q2.allSolutions();
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				System.out.println("Grab " + (q2.hasSolution() ? "succeeded" : "failed"));
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_back.length - Integer.parseInt(solution[0].get("Y").toString());
				mapChar_back[Y][X] = '.';
			}
			
			if(msg.equals("shoot")){
				q2 = new Query("shoot");
				solution = q2.allSolutions();
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				System.out.println("Shoot " + (q2.hasSolution() ? "succeeded" : "failed"));
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_back.length - Integer.parseInt(solution[0].get("Y").toString());
				
			}
			
			if(msg.equals("teletransport")){
				q2 = new Query("teletransport");
				solution = q2.allSolutions();
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				System.out.println("Teletransport " + (q2.hasSolution() ? "succeeded" : "failed"));
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_back.length - Integer.parseInt(solution[0].get("Y").toString());
				
				mapChar[Y][X]='I';
				
			}
			
			/*
			astar(msg, "astar_saferoom", q2, solution, position, game, mapChar, mapChar_astar, mapChar_back);
			astar(msg, "astar_enemy", q2, solution, posenemy, game, mapChar, mapChar_astar, mapChar_back);
			astar(msg, "astar_teletransport", q2, solution, postele, game, mapChar, mapChar_astar, mapChar_back);
			astar(msg, "astar_gohome", q2, solution, posini, game, mapChar, mapChar_astar, mapChar_back);
			*/
			
			
			if(msg.equals("astar_saferoom")){
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
				
				myMap = new Map2<ExampleNode>(mapChar_astar.length, mapChar_astar.length, new ExampleNode(), mapChar_astar);
				
				System.out.println("Xinicial = " + Integer.parseInt(solution[0].get("X").toString()));
				System.out.println("Yinicial = " + Integer.parseInt(solution[0].get("Y").toString()));
				
				
				
				System.out.println(position.size());
				
				if(position.size()==0){
					path = myMap.findPath(Y, X, y0, x0);
					
					for(int l = 0; l < path.size(); l++) {
						q2 = new Query("turn([" + (path.get(l).getyPosition()+1) + "," + (mapChar_astar.length - path.get(l).getxPosition()) + "])");					
						System.out.println("Turn " + (q2.hasSolution() ? "succeeded" : "failed"));
						
						System.out.println("X = " + (mapChar_astar.length - path.get(l).getxPosition()) );
						System.out.println("Y = " + (path.get(l).getyPosition()+1) );
						
						q2 = new Query("forward");
						solution = q2.allSolutions();
						
						copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
						q2 = new Query("agent_location([X,Y])");
						solution = q2.allSolutions();
						
						X = Integer.parseInt(solution[0].get("X").toString()) - 1;
						Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
						
						//Atualiza Mapa Estrela
						refreshmapstar(mapChar_astar, Y,  X);
						
						mapChar[Y][X] = 'I';
						
						q2 = new Query("agent_healthy(X)");
						solution = q2.allSolutions();
						energia = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_score(X)");
						solution = q2.allSolutions();
						score = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_arrows(X)");
						solution = q2.allSolutions();
						arrow = Integer.parseInt(solution[0].get("X").toString());
						
						//game.printmap(mapChar, mapChar_astar,energia,score,arrow);
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						q2 = new Query("climb");
						solution = q2.allSolutions();
						
					}
					
				}
				
				if(position.size()==0){
					break;
				}
				
				//System.out.println("X = " + position.get(position.size()-1).x);
				//System.out.println("Y = " + position.get(position.size()-1).y);
				
				//aleatorio = gerador.nextInt(position.size());
				//System.out.println(position.get(aleatorio).x);
				//System.out.println(position.get(aleatorio).y);
				
				//if(position.get(aleatorio).y == Y && position.get(aleatorio).x == X){
				
				if(position.get(position.size()-1).y == Y && position.get(position.size()-1).x == X){
					
				}else{
					
					path = myMap.findPath(Y, X, position.get(position.size()-1).y, position.get(position.size()-1).x);
					
					//path = myMap.findPath(Y, X, position.get(aleatorio).y, position.get(aleatorio).x);
					
					for(int l = 0; l < path.size(); l++) {
						
						q2 = new Query("turn([" + (path.get(l).getyPosition()+1) + "," + (mapChar_astar.length - path.get(l).getxPosition()) + "])");					
						System.out.println("Turn " + (q2.hasSolution() ? "succeeded" : "failed"));
					
							
						q2 = new Query("forward");
						System.out.println("Xdestino = " + (path.get(l).getyPosition()+1) );
						System.out.println("Ydestino = " + (mapChar_astar.length - path.get(l).getxPosition()) );
						System.out.println("Forward " + (q2.hasSolution() ? "succeeded" : "failed"));
						
					
						
						copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
						
						q2 = new Query("agent_location([X,Y])");
						solution = q2.allSolutions();
						

						X = Integer.parseInt(solution[0].get("X").toString()) - 1;
						Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
						
						//Atualiza Mapa Estrela
						refreshmapstar(mapChar_astar, Y,  X);
						
						mapChar[Y][X] = 'I';
						
						q2 = new Query("agent_healthy(X)");
						solution = q2.allSolutions();
						energia = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_score(X)");
						solution = q2.allSolutions();
						score = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_arrows(X)");
						solution = q2.allSolutions();
						arrow = Integer.parseInt(solution[0].get("X").toString());
						
						game.printmap(mapChar, mapChar_astar,energia,score,arrow);
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
				position.remove(position.size()-1);
				//position.remove(aleatorio);

				consoleprint(mapChar_astar);
				System.out.println("Astar_Saferoom " + (q2.hasSolution() ? "succeeded" : "failed"));
				
			}
			
			
			if(msg.equals("astar_powerup")){
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
				
				myMap = new Map2<ExampleNode>(mapChar_astar.length, mapChar_astar.length, new ExampleNode(), mapChar_astar);
				
				System.out.println("Xinicial = " + Integer.parseInt(solution[0].get("X").toString()));
				System.out.println("Y = " + Integer.parseInt(solution[0].get("Y").toString()));
				
				System.out.println(powerup.size());
				
				System.out.println("X = " + powerup.get(powerup.size()-1).x);
				System.out.println("Y = " + powerup.get(powerup.size()-1).y);
				
				//aleatorio = gerador.nextInt(position.size());
				//System.out.println(position.get(aleatorio).x);
				//System.out.println(position.get(aleatorio).y);
				
				//if(position.get(aleatorio).y == Y && position.get(aleatorio).x == X){
				if(powerup.get(powerup.size()-1).y == Y && powerup.get(powerup.size()-1).x == X){
				}else{
					
					path = myMap.findPath(Y, X, powerup.get(powerup.size()-1).y, powerup.get(powerup.size()-1).x);
					
					//path = myMap.findPath(Y, X, position.get(aleatorio).y, position.get(aleatorio).x);
					
					for(int l = 0; l < path.size(); l++) {
						q2 = new Query("turn([" + (path.get(l).getyPosition()+1) + "," + (mapChar_astar.length - path.get(l).getxPosition()) + "])");					
						System.out.println("Turn " + (q2.hasSolution() ? "succeeded" : "failed"));
						
						System.out.println("X = " + (mapChar_astar.length - path.get(l).getxPosition()) );
						System.out.println("Y = " + (path.get(l).getyPosition() + 1) );
						
						q2 = new Query("forward");
						solution = q2.allSolutions();
						
						copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
						q2 = new Query("agent_location([X,Y])");
						solution = q2.allSolutions();
						
						//System.out.println(Integer.parseInt(solution[0].get("X").toString()));
						//System.out.println(Integer.parseInt(solution[0].get("Y").toString()));
						
						X = Integer.parseInt(solution[0].get("X").toString()) - 1;
						Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
						
						//Atualiza Mapa Estrela
						refreshmapstar(mapChar_astar, Y,  X);
						
						mapChar[Y][X] = 'I';
						
						q2 = new Query("agent_healthy(X)");
						solution = q2.allSolutions();
						energia = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_score(X)");
						solution = q2.allSolutions();
						score = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_arrows(X)");
						solution = q2.allSolutions();
						arrow = Integer.parseInt(solution[0].get("X").toString());
						
						game.printmap(mapChar, mapChar_astar,energia,score,arrow);
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						
						
					}
				}
				powerup.remove(powerup.size()-1);
				//position.remove(aleatorio);

				consoleprint(mapChar_astar);
				System.out.println("Astar_PowerUp " + (q2.hasSolution() ? "succeeded" : "failed"));
				
			}
			
			
			if(msg.equals("astar_enemy")){
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
				
				myMap = new Map2<ExampleNode>(mapChar_astar.length, mapChar_astar.length, new ExampleNode(), mapChar_astar);
				
				System.out.println("X = " + X);
				System.out.println("Y = " + Y);
				
				System.out.println(posenemy.size());
				
				//System.out.println("X = " + posenemy.get(position.size()-1).x);
				//System.out.println("Y = " + posenemy.get(position.size()-1).y);
				
				aleatorio = gerador.nextInt(posenemy.size());
				System.out.println(posenemy.get(aleatorio).x);
				System.out.println(posenemy.get(aleatorio).y);
				
				
				if(posenemy.get(aleatorio).y == Y && posenemy.get(aleatorio).x == X){
				//if(posenemy.get(posenemy.size()-1).y == Y && posenemy.get(posenemy.size()-1).x == X){
				}else{
					
					//path = myMap.findPath(Y, X, posenemy.get(posenemy.size()-1).y, posenemy.get(posenemy.size()-1).x);
					
					path = myMap.findPath(Y, X, posenemy.get(aleatorio).y, posenemy.get(aleatorio).x);
					
					for(int l = 0; l < path.size(); l++) {
						q2 = new Query("turn([" + (path.get(l).getyPosition()+1) + "," + (mapChar_astar.length - path.get(l).getxPosition()) + "])");					
						System.out.println("Turn " + (q2.hasSolution() ? "succeeded" : "failed"));
						
						System.out.println("X = " + (mapChar_astar.length - path.get(l).getxPosition()) );
						System.out.println("Y = " + (path.get(l).getyPosition()+1) );
						
						q2 = new Query("forward");
						solution = q2.allSolutions();
						
						copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
						q2 = new Query("agent_location([X,Y])");
						solution = q2.allSolutions();
						
						//System.out.println(Integer.parseInt(solution[0].get("X").toString()));
						//System.out.println(Integer.parseInt(solution[0].get("Y").toString()));
						
						X = Integer.parseInt(solution[0].get("X").toString()) - 1;
						Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
						
						//Atualiza Mapa Estrela
						refreshmapstar(mapChar_astar, Y,  X);
						
						mapChar[Y][X] = 'I';
						
						q2 = new Query("agent_healthy(X)");
						solution = q2.allSolutions();
						energia = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_score(X)");
						solution = q2.allSolutions();
						score = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_arrows(X)");
						solution = q2.allSolutions();
						arrow = Integer.parseInt(solution[0].get("X").toString());
						
						game.printmap(mapChar, mapChar_astar,energia,score,arrow);
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
				//posenemy.remove(posenemy.size()-1);
				//posenemy.remove(aleatorio);

				consoleprint(mapChar_astar);
				System.out.println("Astar_Enemy " + (q2.hasSolution() ? "succeeded" : "failed"));
				
			}
			
			
			if(msg.equals("astar_teletransport")){
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
				
				myMap = new Map2<ExampleNode>(mapChar_astar.length, mapChar_astar.length, new ExampleNode(), mapChar_astar);
				
				System.out.println("X = " + X);
				System.out.println("Y = " + Y);
				
				System.out.println(postele.size());
				
				//System.out.println("X = " + postele.get(postele.size()-1).x);
				//System.out.println("Y = " + postele.get(postele.size()-1).y);
				
				aleatorio = gerador.nextInt(postele.size());
				System.out.println(postele.get(aleatorio).x);
				System.out.println(postele.get(aleatorio).y);
				
				
				if(postele.get(aleatorio).y == Y && postele.get(aleatorio).x == X){
				//if(postele.get(postele.size()-1).y == Y && postele.get(postele.size()-1).x == X){
				}else{
					
					//path = myMap.findPath(Y, X, postele.get(postele.size()-1).y, postele.get(postele.size()-1).x);
					
					path = myMap.findPath(Y, X, postele.get(aleatorio).y, postele.get(aleatorio).x);
					
					for(int l = 0; l < path.size(); l++) {
						q2 = new Query("turn([" + (path.get(l).getyPosition()+1) + "," + (mapChar_astar.length - path.get(l).getxPosition()) + "])");					
						System.out.println("Turn " + (q2.hasSolution() ? "succeeded" : "failed"));
						
						System.out.println("X = " + (mapChar_astar.length - path.get(l).getxPosition()) );
						System.out.println("Y = " + (path.get(l).getyPosition()+1) );
						
						q2 = new Query("forward");
						solution = q2.allSolutions();
						
						copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
						q2 = new Query("agent_location([X,Y])");
						solution = q2.allSolutions();
						
						//System.out.println(Integer.parseInt(solution[0].get("X").toString()));
						//System.out.println(Integer.parseInt(solution[0].get("Y").toString()));
						
						X = Integer.parseInt(solution[0].get("X").toString()) - 1;
						Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
						
						//Atualiza Mapa Estrela
						refreshmapstar(mapChar_astar, Y,  X);
						
						mapChar[Y][X] = 'I';
						
						q2 = new Query("agent_healthy(X)");
						solution = q2.allSolutions();
						energia = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_score(X)");
						solution = q2.allSolutions();
						score = Integer.parseInt(solution[0].get("X").toString());
						
						q2 = new Query("agent_arrows(X)");
						solution = q2.allSolutions();
						arrow = Integer.parseInt(solution[0].get("X").toString());
						
						game.printmap(mapChar, mapChar_astar,energia,score,arrow);
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
				//postele.remove(postele.size()-1);

				consoleprint(mapChar_astar);
				System.out.println("Astar_teletransport " + (q2.hasSolution() ? "succeeded" : "failed"));
				
			}
			
			
			if(msg.equals("astar_gohome")){
				q2 = new Query("agent_location([X,Y])");
				solution = q2.allSolutions();
				
				X = Integer.parseInt(solution[0].get("X").toString()) - 1;
				Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
				
				myMap = new Map2<ExampleNode>(mapChar_astar.length, mapChar_astar.length, new ExampleNode(), mapChar_astar);
				path = myMap.findPath(Y, X, mapChar.length - y0, x0-1);
				
				for(int l = 0; l < path.size(); l++) {
					q2 = new Query("turn([" + (path.get(l).getyPosition()+1) + "," + (mapChar_astar.length - path.get(l).getxPosition()) + "])");					
					System.out.println("Turn " + (q2.hasSolution() ? "succeeded" : "failed"));
					
					System.out.println("X = " + (mapChar_astar.length - path.get(l).getxPosition()) );
					System.out.println("Y = " + (path.get(l).getyPosition()+1) );
					
					q2 = new Query("forward");
					solution = q2.allSolutions();
					
					copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
					q2 = new Query("agent_location([X,Y])");
					solution = q2.allSolutions();
					
					X = Integer.parseInt(solution[0].get("X").toString()) - 1;
					Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
					
					//Atualiza Mapa Estrela
					refreshmapstar(mapChar_astar, Y,  X);
					
					mapChar[Y][X] = 'I';
					
					q2 = new Query("agent_healthy(X)");
					solution = q2.allSolutions();
					energia = Integer.parseInt(solution[0].get("X").toString());
					
					q2 = new Query("agent_score(X)");
					solution = q2.allSolutions();
					score = Integer.parseInt(solution[0].get("X").toString());
					
					q2 = new Query("agent_arrows(X)");
					solution = q2.allSolutions();
					arrow = Integer.parseInt(solution[0].get("X").toString());
					
					game.printmap(mapChar, mapChar_astar,energia,score,arrow);
					
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
				}
				//postele.remove(postele.size()-1);

				consoleprint(mapChar_astar);
				System.out.println("Astar_GoHome " + (q2.hasSolution() ? "succeeded" : "failed"));
				
			}
						
			if(msg.equals("climb")){
				running = false;
			}
			
			perception.clear();
			
			consoleprint(mapChar);
		}
		
		for(int h=0;h<position.size();h++){
			System.out.println("Posicao " + h + " " + position.get(h).x + " " + position.get(h).y);
		}
		
		q2 = new Query("agent_healthy(X)");
		solution = q2.allSolutions();
		energia = Integer.parseInt(solution[0].get("X").toString());
		
		q2 = new Query("agent_score(X)");
		solution = q2.allSolutions();
		score = Integer.parseInt(solution[0].get("X").toString());
		
		q2 = new Query("agent_arrows(X)");
		solution = q2.allSolutions();
		arrow = Integer.parseInt(solution[0].get("X").toString());
		
		game.printmap(mapChar, mapChar_astar,energia,score,arrow);
		
		consoleprint(mapChar_astar);

		
	}

	static void consoleprint(char [][] map){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}
	
	static void copymatrix(char[][] dest, char[][] orig){
		for(int i=0;i<orig.length;i++){
			for(int j=0;j<orig[0].length;j++){
				dest[i][j] = orig[i][j];
			}
		}
	}
	
	//Cria o arquivo map.pl
	static void mkmappl(char [][] map) throws IOException{	 
	    FileWriter arq = new FileWriter("trabalho2_map.pl");
	    PrintWriter gravarArq = new PrintWriter(arq);
	    
	    for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j] == 'U')
					gravarArq.printf("powerup_location([" + (j+1) + "," + (map.length-i) + "]).\n");
				if(map[i][j] == 'd')
					gravarArq.printf("enemy_location(20,100,[" + (j+1) + "," + (map.length-i) + "]).\n");
				if(map[i][j] == 'D')
					gravarArq.printf("enemy_location(50,100,[" + (j+1) + "," + (map.length-i) + "]).\n");
				if(map[i][j] == 'O')
					gravarArq.printf("gold_location([" + (j+1) + "," + (map.length-i) + "]).\n");
				if(map[i][j] == 'T')
					gravarArq.printf("teletransport_location([" + (j+1) + "," + (map.length-i) + "]).\n");
				if(map[i][j] == 'P')
					gravarArq.printf("pit_location([" + (j+1) + "," + (map.length-i) + "]).\n");
			}
	    }
	 
	    arq.close();
	}
	
	static void mapastar(char [][] map){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j] != 'I'){
					map[i][j] = 'K';
				}else{
					map[i][j] = 'L';
				}
			}
		}
	}
	
	static void refreshmapstar(char[][] map, int y, int x){
		map[y][x] = 'L';
	}
	
	static boolean newpossibility(char[][] map, int y, int x){
		if(map[y][x] != 'L'){
			return true;
		}else{
			return false;
		}
	}
	
	/*
	static void astar(String msg, String compare, Query q2, Map<String, Term>[] solution, Vector<Pos> position, Game game, char[][] mapChar, char[][] mapChar_astar, char[][] mapChar_back){
		Random gerador = new Random();
        int aleatorio = 0;
        
        int tempo = (int)System.currentTimeMillis();
        
        int X, Y;
        
        Map2<ExampleNode> myMap = null;
		List<ExampleNode> path = null;
        
		if(msg.equals("compare")){
			q2 = new Query("agent_location([X,Y])");
			solution = q2.allSolutions();
			
			X = Integer.parseInt(solution[0].get("X").toString()) - 1;
			Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
			
			myMap = new Map2<ExampleNode>(mapChar_astar.length, mapChar_astar.length, new ExampleNode(), mapChar_astar);
			
			System.out.println("X = " + X);
			System.out.println("Y = " + Y);
			
			System.out.println(position.size());
			
			//System.out.println("X = " + position.get(position.size()-1).x);
			//System.out.println("Y = " + position.get(position.size()-1).y);
			
			aleatorio = gerador.nextInt(position.size());
			System.out.println(position.get(aleatorio).x);
			System.out.println(position.get(aleatorio).y);
			
			if(position.get(aleatorio).y == Y && position.get(aleatorio).x == X){
				//if(position.get(position.size()-1).y == Y && position.get(position.size()-1).x == X){
			}else{
				
				//path = myMap.findPath(Y, X, position.get(position.size()-1).y, position.get(position.size()-1).x);
				
				path = myMap.findPath(Y, X, position.get(aleatorio).y, position.get(aleatorio).x);
				
				for(int l = 0; l < path.size(); l++) {
					q2 = new Query("turn([" + (path.get(l).getyPosition()+1) + "," + (mapChar_astar.length - path.get(l).getxPosition()) + "])");					
					System.out.println("Turn " + (q2.hasSolution() ? "succeeded" : "failed"));
					
					System.out.println("X = " + (mapChar_astar.length - path.get(l).getxPosition()) );
					System.out.println("Y = " + (path.get(l).getyPosition()+1) );
					
					q2 = new Query("forward");
					solution = q2.allSolutions();
					
					copymatrix(mapChar, mapChar_back); //recebe a matriz mapa original
					q2 = new Query("agent_location([X,Y])");
					solution = q2.allSolutions();
					
					//System.out.println(Integer.parseInt(solution[0].get("X").toString()));
					//System.out.println(Integer.parseInt(solution[0].get("Y").toString()));
					
					X = Integer.parseInt(solution[0].get("X").toString()) - 1;
					Y = mapChar_astar.length - Integer.parseInt(solution[0].get("Y").toString());
					
					//Atualiza Mapa Estrela
					refreshmapstar(mapChar_astar, Y,  X);
					
					mapChar[Y][X] = 'I';
					
					q2 = new Query("agent_healthy(X)");
					solution = q2.allSolutions();
					double energia = Integer.parseInt(solution[0].get("X").toString());
					
					q2 = new Query("agent_score(X)");
					solution = q2.allSolutions();
					
					game.printmap(mapChar, mapChar_astar,energia,Integer.parseInt(solution[0].get("X").toString()));
					
					while((int)System.currentTimeMillis() - tempo < 200);
					tempo = (int)System.currentTimeMillis();	
				}
			}
			//position.remove(position.size()-1);
			position.remove(aleatorio);

			consoleprint(mapChar_astar);
			System.out.println("Astar_Saferoom " + (q2.hasSolution() ? "succeeded" : "failed"));
			
		}
	}
	*/
	
	private static void loadDLL(String location) {
	    try {
	        File dll = new File(location);
	        System.load(dll.getAbsolutePath());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
