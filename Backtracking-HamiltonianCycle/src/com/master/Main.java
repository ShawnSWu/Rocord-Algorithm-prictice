package com.master;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static int graph[][] = {
            {0, 1, 1, 0, 1},
            {1, 0, 1, 1, 1},
            {1, 1, 0, 1, 0},
            {0, 1, 1, 0, 1},
            {1, 1, 0, 1, 0},
    };

//    private static int graph[][] = {
//            //  0  1  2  3  4  5  6  7  8  9  10
//            {0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0},
//            {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
//            {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
//            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
//            {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
//            {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
//            {1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0},
//            {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
//            {0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
//            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
//            {0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0}
//    };

//    private static int homework[][] = {
//            //  0  1  2  3  4  5  6  7  8  9  10 11
//            {0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
//            {1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0},
//            {0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
//            {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
//            {1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
//            {0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
//            {0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
//            {0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0},
//            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0},
//            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
//            {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
//            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0}
//    };

    //起始點
    private static int origin = 0;
    //存著node是否已走訪過
    private static boolean[] passNode = new boolean[graph.length];
    //是否全部節點都走過
    private static boolean isFinshAll = false;

    private static List<List<Integer>> alreadyFindPath = new ArrayList<>();

    public static void main(String[] args) {
        //單次中已經經過的路徑
        List<Integer> alreadyPassNode = new ArrayList<>();

        //開始找
        hamiltonian(graph, origin,alreadyPassNode);

        if(alreadyFindPath.isEmpty()){
            System.out.println("沒找到迴路");
        }else{

            for(List<Integer> path : alreadyFindPath) {

                for(int i = 0; i < path.size(); i++) {
                    System.out.print(path.get(i) + "->");
                    if (i == path.size() - 1) {
                        System.out.print(origin);
                    }
                }
                System.out.println();
            }
        }
    }

    private static void hamiltonian(int[][] graph, int nowNode, List<Integer> alreadyPassNode){
        //剩下最後一個node 直接判斷他有無跟原點連接
        if(alreadyPassNode.size() == graph.length-1){
            //若是有 直接把它加到alreadyPassNode裡面
            if(graph[nowNode][origin] == 1) {
                //將最後的節點也加到此次路徑中
                alreadyPassNode.add(nowNode);
                isFinshAll = true;
                alreadyFindPath.add(new ArrayList<>(alreadyPassNode));
            }
        }else {
            //走訪
            for (int j = 0; j < graph[nowNode].length; j++) {
                //去判斷有沒有路,且此節點走過了沒有
                if (graph[nowNode][j] == 1 && !passNode[j]) {
                    //設為已走過
                    passNode[nowNode] = true;
                    //若沒重複走過再加到此次路徑中
                    if(!alreadyPassNode.contains(nowNode)) {
                        alreadyPassNode.add(nowNode);
                    }
                    //遞迴再找下一個node
                    hamiltonian(graph, j, alreadyPassNode);
                    //回來設為未通過
                    passNode[j] = false;
                    alreadyPassNode.remove(alreadyPassNode.size() - 1);
                    if (isFinshAll) {
                        isFinshAll = false;
                        return;
                    }

                }

            }
        }

    }
}