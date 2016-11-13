package astar;

import java.util.LinkedList;
import java.util.List;

public class Map2<T extends AbstractNode> {

	/** Matriz com os nós. */
	private T[][] nodes;

	/** Matriz com os valores dos nós (caracteres) */
	private char[][] valueNodes;

	protected int width;
	protected int height;

	private ExampleNode exampleNode;

	
	public Map2(int width, int height, ExampleNode exampleNode, char[][] valueNodes) {
		this.exampleNode = exampleNode;
		nodes = (T[][]) new AbstractNode[width][height];
		this.width = width - 1;
		this.height = height - 1;
		this.valueNodes = valueNodes;
		initEmptyNodes();
	}

	/**
	 * Inicia a matriz de nós.
	 */
	private void initEmptyNodes() {
		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= height; j++) {

				nodes[i][j] = (T) exampleNode.createNode(i, j, valueNodes[i][j]);
			}
		}
	}

	/**
	 * Retorna o nó da posição (x,y).
	 */
	public final T getNode(int x, int y) {
		return nodes[x][y];
	}

	

	/* Variáveis para o algoritmo A* */

	/** Lista dos nós ainda não visitados. */
	private List<T> openList;
	
	/** Nós já visitados. */
	private List<T> closedList;
	
	private boolean done = false;

	/**
	 * Algoritmo A*
	 * 
	 */
	public final List<T> findPath(int oldX, int oldY, int newX, int newY) {
		openList = new LinkedList<T>();
		closedList = new LinkedList<T>();
		openList.add(nodes[oldX][oldY]); // adiciona o nó inicial à lista dos ainda não visitados.
		int clareiraValor = 160;
		int clareira = 1;

		done = false;
		T current;
		while (!done) {
			current = lowestFInOpen(); // pega o nó de menor custo da lista dos ainda não visitados.
			closedList.add(current); // adiciona o nó corrente à lista dos nós visitados.
			openList.remove(current); // remove o nó corrente da lista dos ainda não visitados.

			if (valueNodes[current.getxPosition()][current.getyPosition()] == 'C') {

				if (clareira >= 7) {
					clareiraValor -= 5;
				} else {
					clareiraValor -= 10;
				}

				clareira++;

				nodes[current.getxPosition()][current.getyPosition()].setValueMovement(clareiraValor);
			}

			if ((current.getxPosition() == newX) && (current.getyPosition() == newY)) { // achou o caminho.
				return calcPath(nodes[oldX][oldY], current);
			}

			// Nós adjacentes ao nó corrente:
			List<T> adjacentNodes = getAdjacent(current);
			for (int i = 0; i < adjacentNodes.size(); i++) {
				T currentAdj = adjacentNodes.get(i);
				if (!openList.contains(currentAdj)) { // nó adjacente não está na lista.
					currentAdj.setPrevious(current); // nó corrente é antecessor ao seu adjacente.
					currentAdj.sethCosts(nodes[newX][newY]); // calcula a heurística do nó até o final.
					currentAdj.setgCosts(current); // calcula o custo do nó inicial até o nó adjacente.
					openList.add(currentAdj);
				} else { // nó adjacente já está na lista.
					if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) {
						currentAdj.setPrevious(current); // se o custo já calculado do início até este nó adjacente for maior
														 // que o atual, então troca o nó antecessor.
						currentAdj.setgCosts(current);
					}
				}
			}

			if (openList.isEmpty()) {
				return new LinkedList<T>();
			}
		}
		return null;
	}

	/**
	 * Retorna a lista com o caminho do início até o fim.
	 * 
	 */
	private List<T> calcPath(T start, T goal) {
		LinkedList<T> path = new LinkedList<T>();

		T curr = goal;
		boolean done = false;
		while (!done) {
			path.addFirst(curr);
			curr = (T) curr.getPrevious();

			if (curr.equals(start)) {
				done = true;
			}
		}
		return path;
	}

	/**
	 * Função que retorna o nó com menor custo total (Calculado + Heurística) da lista dos nós ainda não visitados.
	 */
	private T lowestFInOpen() {
		T cheapest = openList.get(0);
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i).getfCosts() < cheapest.getfCosts()) {
				cheapest = openList.get(i);
			}
		}
		return cheapest;
	}

	/**
	 * Retorna uma lista com os nós adjacentes ao nó desejado.
	 */
	private List<T> getAdjacent(T node) {
		int x = node.getxPosition();
		int y = node.getyPosition();
		List<T> adj = new LinkedList<T>();

		T temp;
		if (x > 0) {
			temp = this.getNode((x - 1), y);
			if (temp.isWalkable() && !closedList.contains(temp)) {

				adj.add(temp);
			}
		}

		if (x < width) {
			temp = this.getNode((x + 1), y);
			if (temp.isWalkable() && !closedList.contains(temp)) {

				adj.add(temp);
			}
		}

		if (y > 0) {
			temp = this.getNode(x, (y - 1));
			if (temp.isWalkable() && !closedList.contains(temp)) {

				adj.add(temp);
			}
		}

		if (y < height) {
			temp = this.getNode(x, (y + 1));
			if (temp.isWalkable() && !closedList.contains(temp)) {
				adj.add(temp);
			}
		}

		return adj;
	}

}
