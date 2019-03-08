package com.master;
import java.util.*;

public class Main {

    private static float maxprofit = 0;
    private static LinkedList<Job> useQueue = new LinkedList<Job>();
    public static void main(String[] args) {

        LinkedList<Job> joblist = new LinkedList<>();
        joblist.add(new Job(1,50, 4));
        joblist.add(new Job(2,45,2));
        joblist.add(new Job(3,40,1));
        joblist.add(new Job(4,35,3));
        joblist.add(new Job(5,30,1));
        joblist.add(new Job(6,25,2));
        joblist.add(new Job(7,20,3));
        joblist.add(new Job(8,15,2));
        joblist.add(new Job(9,10,1));
        joblist.add(new Job(10,5,4));

        /* example-1
        LinkedList<Job> joblist = new LinkedList<>();
        joblist.add(new Job(1,50, 3));
        joblist.add(new Job(2,30,2));
        joblist.add(new Job(3,20,2));
        joblist.add(new Job(4,10,1));
        */


        /* example-2
        LinkedList<Job> joblist = new LinkedList<>();
        joblist.add(new Job(1,30, 3));
        joblist.add(new Job(2,25,1));
        joblist.add(new Job(3,20,1));
        joblist.add(new Job(4,10,2));
        joblist.add(new Job(5,5,3));
        */

        /* example-3
        LinkedList<Job> joblist = new LinkedList<>();
        joblist.add(new Job(1,40, 3));
        joblist.add(new Job(2,35,1));
        joblist.add(new Job(3,30,1));
        joblist.add(new Job(4,25,3));
        joblist.add(new Job(5,20,1));
        joblist.add(new Job(6,15,3));
        joblist.add(new Job(7,10,2));
        */

        LinkedList<Node> queue = new LinkedList<Node>();
        queue.add(new Node());

        //依照profits排序
        Collections.sort(joblist, new Comparator<Job>() {
            @Override
            public int compare(Job s1, Job s2) {
                return (int) -(s1.profits - s2.profits);
            }
        });

        //找出最大deadline
        int maxDeadline = Collections.max(joblist, new Comparator<Job>() {
            @Override
            public int compare(Job j1, Job j2) {
                return (j1.getDeadline() - j2.getDeadline());
            }
        }).getDeadline();

        bb(queue, joblist, maxDeadline);

        System.out.println("Max profit is:"+maxprofit);
        System.out.print("Job queue:  ");
        Collections.sort(useQueue);
        for(int i = 0; i< useQueue.size(); i++) {

            if(i== useQueue.size()-1){
                System.out.print(String.valueOf(useQueue.get(i).getId()));
            }else {
                System.out.print(String.valueOf(useQueue.get(i).getId()) + " -> ");
            }
        }
    }

    private static void bb(LinkedList<Node> queue, LinkedList<Job> joblist,int maxDeadline) {
        while(!queue.isEmpty()) {
            Node node = queue.removeFirst();
            for(int i=node.jobID; i<joblist.size(); i++) {
                Node newNode = null;
                try {
                        newNode = (Node) node.clone();
                    //這joblist.get(i)傳入現在的序列裡 是否還合理
                    if (newNode.isOK(joblist.get(i))) {

                        newNode.profits += joblist.get(i).getProfit();
                        newNode.getBound(i, joblist, maxDeadline);
                        //只要profit大於maxprofit,就使其成為maxprofit
                        if (newNode.profits > maxprofit) {
                            maxprofit = newNode.profits;
                            useQueue = (LinkedList<Job>) newNode.job_queue.clone();
                        }//只要bound大於等於maxprfit就可以展開
                        if (newNode.bound >= maxprofit) {
                            newNode.jobID = i + 1;
                            queue.add(newNode);
                            queue.size();
                        }
                    }
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Node implements Cloneable{
        LinkedList<Job> job_queue = new LinkedList<Job>();

        float profits = 0;
        float bound = 0;
        int jobID = 0;

        public Object clone() throws CloneNotSupportedException {
            Node node = (Node)super.clone();
            node.job_queue = (LinkedList<Job>)job_queue.clone();
            return node;
        }

        public void getBound(int id, LinkedList<Job> joblist,int maxDeadline) {
            bound = profits;
            int n = id+2+(maxDeadline-job_queue.size()-1);
            for(int i=id+1; i<n; i++) {
                if (i < joblist.size()) {
                    bound += joblist.get(i).profits;
                }
            }
        }

        public boolean isOK(Job j) {
            job_queue.add(j);
            job_queue.sort(new Comparator<Job>() {
                @Override
                public int compare(Job o1, Job o2) {
                    return (o1.getDeadline() - o2.getDeadline());
                }
            });
            for (int i=job_queue.size()-1; i>=0; i--) {
                if (job_queue.get(i).deadline <= i) {
                    return false;
                }
            }
            return true;
        }

    }


    static class Job implements Comparable<Job>,Cloneable{
        private int id;
        private float profits;
        private int deadline;

        Job(int id, int profits, int deadline){
            this.id = id;
            this.profits = profits;
            this.deadline = deadline;
        }

        public int getId() {
            return id;
        }

        public float getProfit() {
            return profits;
        }

        public int getDeadline() {
            return deadline;
        }

        @Override
        public int compareTo(Job other) {
            return this.deadline - other.getDeadline();
        }
    }
}






