package com.master;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    static int maxWeight = 30;
    static int n = 10;
    static int[] p = {9,5,14,11,16,8,13,3,15,23,29};
    static int[] w = {3,2,5,4,6,3,4,1,7,8,9};
    static List<int[]> population = Stream.of(Arrays.stream(new int[n][p.length]).map(x -> IntStream.range(0, p.length).map(y -> x[y] = Math.random()<0.5?1:0).toArray()).toArray(int[][]::new)).collect(Collectors.toList());
    static Map.Entry<Integer, int[]> max_pair =  new AbstractMap.SimpleImmutableEntry<Integer, int[]>(0, new int[p.length]);
    static int run_time = 500;

    public static void main(String[] args) {
        IntStream.range(0, run_time).forEach(x -> {selection_Crossover_Mutation(population.stream().map(item -> item).collect(Collectors.toList()));});
        System.out.println("Max_Profit: " + max_pair.getKey() + "\nMax_Genes: " + Arrays.toString(max_pair.getValue()));
    }

    public static void selection_Crossover_Mutation(List<int[]> l){
        IntStream.range(0, n).forEach(x -> {System.out.println("Generation:" + x + "\t Fitness" + IntStream.range(0, l.get(x).length).filter(y -> l.get(x)[y]!=0).map(z -> p[z]).sum());});

        List<int[]> vaild = l.stream().filter(x -> IntStream.range(0, x.length).filter(y -> x[y]!=0).map(z -> w[z]).sum() <= maxWeight).collect(Collectors.toList());
        int[] sum_profit = IntStream.range(0, vaild.size()).map(x -> IntStream.range(0, x).map(q -> IntStream.range(0, vaild.get(q).length).filter(y->vaild.get(q)[y]!=0).map(z->p[z]).sum()).sum()).toArray();
        int max = vaild.stream().mapToInt(x -> IntStream.range(0, x.length).filter(y -> x[y]!=0).map(z -> p[z]).sum()).distinct().max().getAsInt();
        if (max > max_pair.getKey()) max_pair =  new AbstractMap.SimpleImmutableEntry  <Integer, int[]>(max, vaild.get(sum_profit.length-1).clone());
        IntStream.range(0, vaild.size()).forEach(x -> population.set(x, vaild.get((int) Arrays.stream(sum_profit).filter(y -> y<(int)(Math.random()*sum_profit[sum_profit.length-1])).count())));

        System.out.println(population.size());
        System.out.println("Max_Profit: " + max_pair.getKey() );
        System.out.println("Max_Genes: " + Arrays.toString(max_pair.getValue()) + "\n====================================================");

        IntStream.range(0, vaild.size()).forEach(i -> {if(Math.random() < 0.5) population.set(i, IntStream.concat(Arrays.stream(vaild.get((int)(Math.random()*vaild.size()))).limit(p.length/2), Arrays.stream(vaild.get((int)(Math.random()*vaild.size()))).skip(p.length/2)).toArray());});
        IntStream.range(vaild.size(), n).forEach(i -> population.set(i, IntStream.concat(Arrays.stream(population.get((int)(Math.random()*n))).limit(p.length/2), Arrays.stream(population.get((int)(Math.random()*n))).skip(p.length/2)).toArray()));

        IntStream.range(0, n).forEach(i -> {if(Math.random() < 0.1) population.get(i)[(int)(Math.random()*n)] ^= 1;});
    }
}