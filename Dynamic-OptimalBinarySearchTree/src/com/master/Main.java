package com.master;
import java.util.*;

public class Main {
    static double minAvg;
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("輸入機率 用,相隔。");
        String input = sc.nextLine();
        String[] inputArray = input.split(",");
        double[] inputFrequency= new double[inputArray.length];

        for(int i = 0;i < inputFrequency.length;i++) {
            inputFrequency[i] = Float.parseFloat(inputArray[i]);
        }

        int n = inputFrequency.length ;

        int [][]R = new int[n+1][n+1];

        double A [][] = optst(n,inputFrequency,R);

        showAtable(A);
        System.out.println();
        System.out.println();
        showRtable(R);
        System.out.println();
        System.out.println();

        new BinarySearchTree().showRTableTree(R);


    }

    static double[][] optst(int n, double p[],int R[][]){
        int i,j,diagonal;
        double [][]A = new double[n+1][n+1];
        for (i=1; i<=n; i++) {
            A[i-1][i-1] = 0;
            A[i-1][i] = p[i-1];
            R[i-1][i] = i;
            R[i-1][i-1] = 0;
        }


        for(diagonal=1; diagonal<=n-1; diagonal++) {
            for(i=1; i<=n-diagonal; i++) {
                j = i + diagonal;
                double minimum = Double.MAX_VALUE;
                double sum = 0;
                int minr = 0;

                for(int k=i; k<=j; k++) {
                    if (A[i-1][k-1]+A[k][j] < minimum) {
                        minimum = A[i-1][k-1]+A[k][j];
                        minr = k;
                    }
                    sum += p[k-1];
                }
                A[i-1][j] = minimum + sum;
                R[i-1][j] = minr;
            }
        }

        return A;
    }


    //單純打印表
    static void showAtable(double a[][]) {
        for(int i=0;i<a.length;i++)
        {
            for(int z=0;z<a.length;z++) {
                System.out.printf("%.3f"+"\t",a[i][z]);

            }
            System.out.println();
        }

    }

    static void showRtable(int a[][]) {
        for(int i=0; i<a.length; i++) {
            for(int j=0; j<a[0].length;j++) {
                System.out.print(String.valueOf(a[i][j]) + "\t\t");
            }
            System.out.println();
        }
    }
}


class BinarySearchTree {

    private int maxLevel = 0;

    private LinkedList<Node> nodeList = new LinkedList<>();

    private LinkedList<LinkedList<Node>> nodeClassifyFromLevel = new LinkedList<>();

    public void showRTableTree(int R[][]) {

        Node node = new Node();
        build(R, node, 0, R.length-1, 0, -1);
        node.showTree(node);

        for(int i=0;i<=maxLevel;i++){
            nodeClassifyFromLevel.add(new LinkedList<>());
        }
        for(int j=0;j<=maxLevel;j++) {
            for(int i =0;i<nodeList.size();i++){
                if(nodeList.get(i).node == 0) {
                    nodeClassifyFromLevel.get(j).remove(nodeList.get(i));
                }else{
                    if (nodeList.get(i).level == j) {
                        nodeClassifyFromLevel.get(j).add(nodeList.get(i));
                    }
                }
            }
        }
        for(int i=0;i<nodeClassifyFromLevel.size();i++){
            LinkedList<Node> ListNode = nodeClassifyFromLevel.get(i);
            int alreadyPrint = 0;

            for(int j=0;j<ListNode.size();j++){
                Node realNode = ListNode.get(j);
                int NotYetPrint = realNode.index - alreadyPrint;
                for (int a = 0; a < NotYetPrint; a++) {
                    System.out.print("\t");
                }
                alreadyPrint = realNode.index;
                System.out.print(realNode.node+" ");
            }
            System.out.println();
        }
        nodeClassifyFromLevel.size();
    }

    Node build(int R[][], Node node, int leftpivot, int rightpivot, int level, int i) {
        if (leftpivot == rightpivot) {
            Node sub = new Node();
            if(leftpivot-1<=0) {
                return sub;
            }
            sub.node = R[leftpivot-1][rightpivot];
            sub.level = level;
            i++;
            sub.index = i;
            return sub;
        }

        int subRoot = R[leftpivot][rightpivot];
        node.node = subRoot;
        node.level = level;
        level ++;


        int new_leftpivot = subRoot-1;
        int new_rightpivot = subRoot+1;

        Node newLiftTree = new Node();
        if (new_leftpivot>=leftpivot) {
            node.leftNode = build(R, newLiftTree, leftpivot, new_leftpivot, level, i);
            i = node.leftNode.index;
        }
        i++;
        node.index = i;


        Node newRightTree = new Node();
        if (new_rightpivot<=rightpivot) {
            node.rightNode = build(R, newRightTree, new_rightpivot, rightpivot, level, i);
            i = node.rightNode.index;
        }

        if(maxLevel<=level){
            maxLevel=level;
        }
        return node;
    }


    class Node {
        Node leftNode ;
        Node rightNode;

        int node = 0;
        int level = 0;
        int index = 0;

        //純打印
        void showTree(Node node) {
            nodeList.add(node);
            if (node.leftNode != null) {
                showTree(node.leftNode);
            }

            if (node.rightNode != null) {
                showTree(node.rightNode);
            }
        }
    }


}
