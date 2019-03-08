package com.master;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Main {
    static float maxprofit = 0;
    static LinkedList<Job> run_queue = new LinkedList<Job>();

    public static void main(String[] args) throws CloneNotSupportedException {
        Job[] a= {new Job("j1", 40, 3),
                new Job("j2", 35, 1),
                new Job("j3", 30, 1),
                new Job("j4", 25, 3),
                new Job("j5", 20, 1),
                new Job("j6", 15, 3),
                new Job("j7", 10, 2)};
        LinkedList<Node> queue = new LinkedList<Node>();
        queue.add(new Node());

        Arrays.asList(a).sort(Comparator.<Job, Float>comparing(p -> p.profits).reversed());
        int n = Collections.max(Arrays.asList(a), Comparator.comparing(s -> s.deadline)).deadline;

        System.out.print("job_queue \t\t\t:");
        for(Job j: a) System.out.print(String.valueOf(j.id) + "\t");
        System.out.println();
        System.out.print("job_queue_deadline \t\t:");
        for(Job j: a) System.out.print(String.valueOf(j.deadline) + "\t");
        System.out.println("\n============================================");

        branch_and_bound(queue, a, n);

        System.out.println(maxprofit);
        System.out.print("job_queue \t:");
        for(Job j: run_queue) System.out.print(String.valueOf(j.id) + "\t");
    }

    static void branch_and_bound(LinkedList<Node> queue, Job[] a, int n) throws CloneNotSupportedException {
        while(!queue.isEmpty()) {
            Node node = queue.removeFirst();
            for(int i=node.weights; i<a.length; i++) {
                Node newnode = (Node)node.clone();

                if (newnode.valid(a[i])) {

                    newnode.profits += a[i].profits;
                    newnode.calBound(i, a, n);
                    if (newnode.profits > maxprofit) {
                        maxprofit = newnode.profits;
                        run_queue = (LinkedList<Job>) newnode.job_queue.clone();
                    }
                    if (newnode.bound >= maxprofit) {
                        newnode.weights = i+1;
                        queue.add(newnode);
                    } else break;
                };
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    static class Node implements Cloneable{
        int weights = 0;
        LinkedList<Job> job_queue = new LinkedList<Job>();
        float profits = 0;
        float bound = 0;

        public Object clone() throws CloneNotSupportedException {
            Node node = (Node)super.clone();
            node.job_queue = (LinkedList<Job>)job_queue.clone();
            return node;
        }

        public void calBound(int index, Job[] a,int n) {
            bound = profits;
            for(int i=index+1; i<index+2+(n-job_queue.size()-1); i++)
                if(i<a.length) bound += a[i].profits;


        }

        public boolean valid(Job j) {
            job_queue.add(j);
            job_queue.sort(Comparator.<Job, Integer>comparing(p -> p.deadline));
            for (int i=job_queue.size()-1; i>=0; i--)
                if (job_queue.get(i).deadline <= i) return false;
            return true;
        }
    }

    static class Job {
        String id;
        float profits;
        int deadline;

        Job(String id, int penalty, int deadline){
            this.id = id;
            this.profits = penalty;
            this.deadline = deadline;
        }
    }
}