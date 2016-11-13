package AG;

import java.util.Random;

public class Individuo {

    private double aptidao = 0;
    double solution = 0;
    
    private int[][] gns;

    //gera um indivíduo aleatório
    public Individuo(int qtdclareira, int qtddoces) {
    	gns = new int[qtdclareira][qtddoces];
        Random r = new Random();
        
        for (int i = 0; i < qtdclareira-1; i++) {
        	for (int j = 0; j < qtddoces-1; j++) {
        		gns[i][j] = r.nextInt(2);
        	}
        }
        
        geraAptidao();        
    }

  //cria um indivíduo com os genes definidos
    public Individuo(int[][] gns) {
    	this.gns = new int[gns.length][gns[0].length];
    	
    	for (int i = 0; i < gns.length; i++) {
        	for (int j = 0; j < gns[0].length; j++) {
        		this.gns[i][j] = gns[i][j];
        	}
    	}
        
        Random r = new Random();
        //se for mutar, cria um gene aleatório
        if (r.nextDouble() <= Algoritimo.getTaxaDeMutacao()) {
        	
        	int[][] newgns = new int[gns.length][gns[0].length];
        	
            int posAleatoriai = r.nextInt(gns.length);
            int posAleatoriaj = r.nextInt(gns[0].length);
            
            for (int i = 0; i < gns.length; i++) {
            	for (int j = 0; j < gns[0].length; j++) {
            		if (i==posAleatoriai && j==posAleatoriaj){
            			newgns[i][j] = r.nextInt(2);
                    }else{
                    	newgns[i][j] = gns[i][j];
                    }
            	}
            }
            
            
            for (int i = 0; i < newgns.length; i++) {
            	for (int j = 0; j < newgns[0].length; j++) {
            		this.gns[i][j] = newgns[i][j];
            	}
        	}
        }
        geraAptidao();
    }

    //gera o valor de aptidão, será calculada pelo número de bits do gene iguais ao da solução
    private void geraAptidao() {
        int sum=0;
        double sumapreciation = 0;
        for (int i = 0; i < gns.length; i++) {
        	for (int j = 0; j < gns[0].length; j++) {
        		sum += gns[i][j];
        	}
        }
        if(sum<=24 && validate(gns)){
        	for (int i = 0; i < gns.length; i++) {
        		sumapreciation = 0;
            	for (int j = 0; j < gns[0].length; j++) {
            		if (j == 0){
            			sumapreciation += (gns[i][j]*1.5);
            		}else if(j == 1){
            			sumapreciation += (gns[i][j]*1.4);
            		}else if(j == 2){
            			sumapreciation += (gns[i][j]*1.3);
            		}else if(j == 3){
            			sumapreciation += (gns[i][j]*1.2);
            		}else if(j == 4){
            			sumapreciation += (gns[i][j]*1.1);
            		}
            	}
            	if (i == 0){
            		aptidao += 150/sumapreciation;
        		}else if(i == 1){
        			aptidao += 140/sumapreciation;
        		}else if(i == 2){
        			aptidao += 130/sumapreciation;
        		}else if(i == 3){
        			aptidao += 120/sumapreciation;
        		}else if(i == 4){
        			aptidao += 110/sumapreciation;
        		}else if(i == 5){
        			aptidao += 100/sumapreciation;
        		}else if(i == 6){
        			aptidao += 95/sumapreciation;
        		}else if(i == 7){
        			aptidao += 90/sumapreciation;
        		}else if(i == 8){
        			aptidao += 85/sumapreciation;
        		}else if(i == 9){
        			aptidao += 80/sumapreciation;
        		}
        	}
        //System.out.println("SUM: " + sumapreciation);
        //System.out.println("Aptidao: " + aptidao);
        }else{
        	aptidao=1000;
        }
    }
    
    //verifica se o indivíduo da população é válido
    public boolean validate(int[][] gem) {
    	for (int i = 0; i < gem.length; i++) {
    		int flag = 0;
        	for (int j = 0; j < gem[0].length; j++) {
        		if(gem[i][j]==1) flag=1;
        	}
        	if(flag==0){
        		return false;
        	}
    	}	
    	for (int i = 0; i < gem[0].length; i++) {
    		int sum=0;
    		for (int j = 0; j < gem.length; j++) {
    			sum += gem[j][i];
    		}
    		if(sum>5) return false;
    	}
        return true;
    }

    public double getAptidao() {
        return aptidao;
    }
    
    public int[][] getGns() {
        return gns;
    }
}