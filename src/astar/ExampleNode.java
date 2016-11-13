package astar;


public class ExampleNode extends AbstractNode {

	public ExampleNode(int xPosition, int yPosition, char letter) {
		super(xPosition, yPosition, letter);

	}
	
	public ExampleNode() {
		
	}

	public AbstractNode createNode(int x, int y, char value) {

		return new ExampleNode(x, y, value);
	}

	@Override
	public void sethCosts(AbstractNode endNode) {

		this.sethCosts((int) Math.sqrt(Math.pow(this.getxPosition() - endNode.getxPosition(), 2)
				+ Math.pow(this.getyPosition() - endNode.getyPosition(), 2)));
	}

}
