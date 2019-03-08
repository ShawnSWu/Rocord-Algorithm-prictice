import java.util.*;

public class Genetic {

    private static int generationCount = 0;

    private static Population population;
    private Chromosome firstFirness;
    private Chromosome secondFittest;
    private Chromosome offSpring_A,offSpring_B;

    private static double mutationRate = 0.4;
    public static int maxWeight = 30;

    public static LinkedList<Item> itemList = new LinkedList<>();
    private static Chromosome bestChromosome = new Chromosome();

    public static void main(String[] args) {

        itemList.add(new Item(1,3,9));
        itemList.add(new Item(2,2,5));
        itemList.add(new Item(3,5,14));
        itemList.add(new Item(4,4,11));
        itemList.add(new Item(5,6,16));
        itemList.add(new Item(6,3,8));
        itemList.add(new Item(7,4,13));
        itemList.add(new Item(8,1,3));
        itemList.add(new Item(9,7,15));
        itemList.add(new Item(10,8,23));
        itemList.add(new Item(11,9,29));

        Genetic genetic = new Genetic();

        //產生Population
        Random rn = new Random();
        population = new Population(200,itemList.size());
        //計算初始的fitness
        genetic.updateFitnessSort();

        System.out.println("初始population：");
        System.out.println();
        System.out.println("----------------------------------------------------");

        while (generationCount != 100){
            ++generationCount;

                genetic.selection();
                genetic.crossover();

                if (Math.random() <= Genetic.mutationRate) {
                    genetic.mutation();
                }

                genetic.addBetterToOffspring();

                genetic.updateFitnessSort();

            if(bestChromosome.getFitness() != 0) {
                System.out.println("Generation: " + generationCount + " Profit: " + bestChromosome.getFitness());
            }else{
                System.out.println("Generation: " + generationCount + " Profit: " + population.getChromosomes()[0].getFitness());
            }

            if(population.getChromosomes()[0].getFitness() > bestChromosome.getFitness()) {
                bestChromosome = new Chromosome(population.getChromosomes()[0]);
            }
        }

        System.out.print("Genes: ");
        for (int i = 0; i < bestChromosome.getGeneSequence().length; i++) {
            System.out.print(bestChromosome.getGeneSequence()[i]);
        }
        System.out.println();
        System.out.println("Profit :"+bestChromosome.getFitness());
        System.out.println("Weight :"+bestChromosome.getWeight());
    }

    private void updateFitnessSort(){
        population.calculateAllFitness();
        Arrays.sort(population.getChromosomes() ,Collections.reverseOrder());
    }

    private void selection(){
        updateFitnessSort();
        firstFirness = population.getChromosomes()[getSelection()];
        secondFittest = population.getChromosomes()[getSelection()];
    }

    private int getSelection() {
        double maxP=0;
        int selectionPoint=0;
        for(int i =0;i<population.getChromosomes().length;i++) {
            double p = Math.random()*population.getChromosomes()[i].getFitness();
            if(p > maxP){
                maxP = p;
                selectionPoint = i;
            }
        }
        return selectionPoint;
    }

    //交配,找出中斷點做交配
    private void crossover(){
        //隨機找CrossOver Point
        Random random = new Random();
        int crossOverPoint = random.nextInt(population.getChromosomes()[0].getGeneSequence().length);

        //Swap values among parents
        offSpring_A = new Chromosome(firstFirness);
        offSpring_B = new Chromosome(secondFittest);

        for (int i = 0; i < crossOverPoint; i++) {
            int temp = firstFirness.getGeneSequence()[i];
            offSpring_A.getGeneSequence()[i] = offSpring_B.getGeneSequence()[i];
            offSpring_B.getGeneSequence()[i] = temp;
        }


    }

    //突變*
    private void mutation(){

        Random rn = new Random();
        int a = rn.nextInt(population.getChromosomes().length);
        int mutationPoint = rn.nextInt(population.getChromosomes()[a].getGeneSequence().length);

        if (offSpring_A.getGeneSequence()[mutationPoint] == 0) {
            offSpring_A.getGeneSequence()[mutationPoint] = 1;
        } else {
            offSpring_A.getGeneSequence()[mutationPoint] = 0;
        }
        a = rn.nextInt(population.getChromosomes().length);
        mutationPoint = rn.nextInt(population.getChromosomes()[a].getGeneSequence().length);

        if (offSpring_B.getGeneSequence()[mutationPoint] == 0) {
            offSpring_B.getGeneSequence()[mutationPoint] = 1;
        } else {
            offSpring_B.getGeneSequence()[mutationPoint] = 0;
        }

    }

    //找出最不適合的染色體,代替它
    private void addBetterToOffspring() {
        updateFitnessSort();
        //找出最不適合的
        if(offSpring_A.getWeight() <= maxWeight && offSpring_B.getWeight() <= maxWeight) {

            int lastOne = population.getChromosomes().length-1;
            //由新的子代代替
//            if (offSpring_A.getFitness() > population.getChromosomes()[lastOne].getFitness()) {
                population.getChromosomes()[lastOne-1] = new Chromosome(offSpring_A);
                population.getChromosomes()[lastOne] = new Chromosome(offSpring_B);
//            }
        }
    }

    Chromosome getFittestOffspring() {
        if (offSpring_A.getFitness() > offSpring_B.getFitness())
            return offSpring_A;
        return offSpring_B;
    }


    private void printPopulation(){

        for(int i = 0;i < population.getSize();i++) {
            Chromosome chromosome = population.getChromosomes()[i];
            int[] GeneOfChromosome = chromosome.getGeneSequence();
            System.out.printf("第 %d 個: ", i+1);
            for (int j = 0; j < GeneOfChromosome.length; j++) {
                System.out.print(GeneOfChromosome[j]);
            }
            System.out.println();
        }
    }
}



//Population class ,mean there are a lot of chromosomes
class Population{

    private Chromosome[] chromosomes;

    public Population(int ChromosomesSize, int GeneSequenceSize){
        chromosomes= new Chromosome[ChromosomesSize];
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new Chromosome(GeneSequenceSize);
        }
    }


    public Chromosome[] getChromosomes() {
        return chromosomes;
    }


    public int getSize(){
        return chromosomes.length;
    }

    public void calculateAllFitness() {
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i].calculateFitness();
        }
    }

}

class Chromosome implements Comparable<Chromosome>{

    private int fitness = 0;
    private int[] geneSequence;


    public Chromosome(){
        this.geneSequence = new int[Genetic.itemList.size()];
    }

    public Chromosome(Chromosome chromosome){
        this.geneSequence = new int[Genetic.itemList.size()];
        for(int i =0;i< chromosome.getGeneSequence().length;i++){
            this.geneSequence[i] = chromosome.getGeneSequence()[i];
        }
        calculateFitness();
    }

    //初始化Chromosome
    public Chromosome(int GeneSequenceSize){
        Random random = new Random();
        geneSequence = new int[GeneSequenceSize];
        int[] tempArray = new int[GeneSequenceSize];
        do {
            for (int i = 0; i < tempArray.length; i++) {
                tempArray[i] = random.nextInt(2);
            }
        }while (isMoreThanBigW(tempArray));
        geneSequence = tempArray;
    }

    //驗證是否合理
    public static boolean isMoreThanBigW(int[] tempArray){
        int countWeight = 0;
        for(int i =0 ;i< tempArray.length;i++) {
            if(tempArray[i] == 1) {
                countWeight += Genetic.itemList.get(i).getWeight();
            }
        }
        return countWeight > Genetic.maxWeight;
    }


    public void calculateFitness(){
        //若重新計算要先歸零
        fitness = 0;
        for(int i=0;i<geneSequence.length;i++){
            if(geneSequence[i] == 1){
                fitness += Genetic.itemList.get(i).getProfit();
            }
        }
    }

    public int getFitness() {
        calculateFitness();
        return fitness;
    }


    public int getWeight() {
        int weight = 0;
        for(int i =0 ;i< geneSequence.length;i++) {
            if(geneSequence[i] == 1) {
                weight += Genetic.itemList.get(i).getWeight();
            }
        }
        return weight;
    }

    public int[] getGeneSequence() {
        return geneSequence;
    }

    @Override
    public int compareTo(Chromosome o) {
        return this.fitness - o.getFitness();
    }
}



class Item {

    private int Id;
    private int weight;
    private int profit;

    public Item(int id, int weight, int profit) {
        Id = id;
        this.weight = weight;
        this.profit = profit;
    }

    public int getId() {
        return Id;
    }

    public int getWeight() {
        return weight;
    }

    public int getProfit() {
        return profit;
    }

}