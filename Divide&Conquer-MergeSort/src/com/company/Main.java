package com.company;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {


    static LinkedList divide (LinkedList list) {


        if (list.size()<2)
            return list;

        LinkedList left = new LinkedList();
        LinkedList right = new LinkedList();

        int middle = list.size()/2;

        for (int i = 0; i < middle; i++) {
            left.add(list.get(i));
        }
        for(int i = middle; i < list.size(); i++) {
            right.add(list.get(i));
        }

        return merge(divide(left), divide(right));
    }


    static LinkedList merge(LinkedList<Double> left, LinkedList<Double> right) {
        LinkedList<Double> sortList = new LinkedList();
        while(!left.isEmpty() || !right.isEmpty()) {

            if(!left.isEmpty()) {
                if (!right.isEmpty()){
                    if (right.get(0) < left.get(0)) {
                        sortList.add(right.remove(0));
                        sortList.add(left.remove(0));

                    } else {
                        sortList.add(left.remove(0));
                        sortList.add(right.remove(0));
                    }
            }else{
                    sortList.add(left.remove(0));
                }
            }
            else {
                sortList.add(right.remove(0));
            }
        }



        return sortList;
    }

    public static void main(String[] args) {

        LinkedList<Double> list = new LinkedList();

        Scanner sc = new Scanner(System.in);
        System.out.println("請輸入欲加入的長度:");
        int listlong=0;

        try {
            listlong = sc.nextInt();
        }
        catch(InputMismatchException e) {
            System.out.println("輸入格式錯誤:");
        }

        for(int i=0; i < listlong; i++ ) {
            try {
                System.out.println("加入的數值:");
                double s1 = sc.nextDouble();
                list.add(s1);
            }
            catch(InputMismatchException e) {
                System.out.println("輸入格式錯誤:");
            }
        }

        LinkedList printlist = divide(list);
        for(int i = 0; i < printlist.size(); i++) {
            System.out.print(printlist.get(i));
            System.out.print(",");
        }

    }

}