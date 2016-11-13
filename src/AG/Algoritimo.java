package AG;

import java.util.Random;

public class Algoritimo {
	
    private static double taxaDeCrossover;
    private static double taxaDeMutacao;

    public static Populacao novaGeracao(Populacao populacao, boolean elitismo) {
        Random r = new Random();
        //nova populaçãoo do mesmo tamanho da antiga
        Populacao novaPopulacao = new Populacao(populacao.getTamPopulacao());
        
        //se tiver elitismo, mantém o melhor indivíduo da geração atual
        if (elitismo) {
            novaPopulacao.setIndividuo(populacao.getIndivduo(0));
        }

        //insere novos indivíduos na nova população, até atingir o tamanho máximo
        while (novaPopulacao.getNumIndividuos() < novaPopulacao.getTamPopulacao()) {
            //seleciona os 2 pais por torneio
            Individuo[] pais = selecaoTorneio(populacao);

            Individuo[] filhos = new Individuo[2];

            //verifica a taxa de crossover, se sim realiza o crossover, se não, mantém os pais selecionados para a próxima geração
            if (r.nextDouble() <= taxaDeCrossover) {
                filhos = crossover(pais[1], pais[0]);
            } else {
                filhos[0] = new Individuo(pais[0].getGns());
                filhos[1] = new Individuo(pais[1].getGns());
            }

            //adiciona os filhos na nova geração
            novaPopulacao.setIndividuo(filhos[0]);
            novaPopulacao.setIndividuo(filhos[1]);
        }

        //ordena a nova população
        novaPopulacao.ordenaPopulacao();
        return novaPopulacao;
    }

    public static Individuo[] crossover(Individuo individuo1, Individuo individuo2) {
        Random r = new Random();

        //sorteia o ponto de corte
        int pontoCorte1 = r.nextInt((individuo1.getGns().length/2) -2) + 1;
        int pontoCorte2 = r.nextInt((individuo1.getGns().length/2) -2) + 1;
        

        Individuo[] filhos = new Individuo[2];

        //pega os genes dos pais
        int[][] genePai1 = individuo1.getGns();
        int[][] genePai2 = individuo2.getGns();

        int[][] geneFilho1 = new int[genePai1.length][genePai1[0].length];
        int[][] geneFilho2 = new int[genePai1.length][genePai1[0].length];

        //realiza o corte, 
        for (int i = 0; i < pontoCorte1; i++) {
        	for (int j = 0; j < pontoCorte2; j++) {
        		geneFilho1[i][j] = genePai1[i][j];
        	}
        }
        for (int i = pontoCorte1; i < genePai2.length; i++) {
        	for (int j = pontoCorte2; j < genePai2[0].length; j++) {
        		geneFilho1[i][j] = genePai2[i][j];
        	}
        }
        
        
        for (int i = 0; i < pontoCorte1; i++) {
        	for (int j = 0; j < pontoCorte2; j++) {
        		geneFilho2[i][j] = genePai2[i][j];
        	}
        }
        for (int i = pontoCorte1; i < genePai1.length; i++) {
        	for (int j = pontoCorte2; j < genePai1[0].length; j++) {
        		geneFilho1[i][j] = genePai1[i][j];
        	}
        }

        return filhos;
    }

    public static Individuo[] selecaoTorneio(Populacao populacao) {
        Random r = new Random();
        Populacao populacaoIntermediaria = new Populacao(3);

        //seleciona 3 indivíduos aleatoriamente na população
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(r.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(r.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(r.nextInt(populacao.getTamPopulacao())));

        //ordena a população
        populacaoIntermediaria.ordenaPopulacao();

        Individuo[] pais = new Individuo[2];

        //seleciona os 2 melhores deste população
        pais[0] = populacaoIntermediaria.getIndivduo(0);
        pais[1] = populacaoIntermediaria.getIndivduo(1);

        return pais;
    }

    public static double getTaxaDeCrossover() {
        return taxaDeCrossover;
    }

    public static void setTaxaDeCrossover(double taxaDeCrossover) {
        Algoritimo.taxaDeCrossover = taxaDeCrossover;
    }

    public static double getTaxaDeMutacao() {
        return taxaDeMutacao;
    }

    public static void setTaxaDeMutacao(double taxaDeMutacao) {
        Algoritimo.taxaDeMutacao = taxaDeMutacao;
    }
    
    
}