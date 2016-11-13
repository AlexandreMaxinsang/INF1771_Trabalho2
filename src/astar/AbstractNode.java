package astar;


public abstract class AbstractNode {
	
    private double valueMovement=0;
    
    private int xPosition;
    private int yPosition;
    private boolean walkable;

    private AbstractNode previous;

    /** Custo do ponto inicial até o este nó. */
    private double gCosts;

    /** Custo estimado deste nó até o nó final. */
    private int hCosts;

    
    public AbstractNode(int xPosition, int yPosition,char letter) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.walkable = true;
        calculateNodeCost(letter);
    }
    
    public AbstractNode(){
    	
    }
    
    /**
     * Custo para o movimento em cada nó.
     */
    public void calculateNodeCost(char letter) {
      
    	if(letter=='K')
    	{
    		valueMovement=1000;
    	}
    	else
    		if(letter=='L')
    			valueMovement=1;
    }
    
    public void setValueMovement(double value){
    	this.valueMovement = value;
    }
    
    public double getValueMovement(){
    	return this.valueMovement;
    }

    public void setCoordinates(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    /**
     * Retorna o nó anterior a este.
     * 
     */
    public AbstractNode getPrevious() {
        return previous;
    }

    public void setPrevious(AbstractNode previous) {
        this.previous = previous;
    }


    /**
     * 
     * Retorna o custo total (Total = Custo calculado até este nó + heuristica).
     * 
     */
    public double getfCosts() {
        return gCosts + hCosts;
    }

    /**
     * 
     * Retorna o custo do início até este nó.
     * 
     */
    public double getgCosts() {
        return gCosts;
    }

    private void setgCosts(double gCosts) {
        this.gCosts = gCosts;
    }
    public void setgCosts(AbstractNode previousAbstractNode, double basicCost) {
        setgCosts(previousAbstractNode.getgCosts() + basicCost);
    }

    public void setgCosts(AbstractNode previousAbstractNode) {
      setgCosts(previousAbstractNode, valueMovement);
     
    }

    /**
     * 
     * Calcula o custo do nó inicial até este nó. MAS NAO GUARDA O VALOR NA VARIÁVEL gCosts, APENAS RETORNA.
     * 
     */
    public double calculategCosts(AbstractNode previousAbstractNode) {
    	
        return (previousAbstractNode.getgCosts()+ valueMovement );
    }

    public double calculategCosts(AbstractNode previousAbstractNode, int movementCost) {
        return (previousAbstractNode.getgCosts() + movementCost );
    }

 
    public int gethCosts() {
        return hCosts;
    }


    protected void sethCosts(int hCosts) {
        this.hCosts = hCosts;
    }
    

    public abstract void sethCosts(AbstractNode endAbstractNode);

}
