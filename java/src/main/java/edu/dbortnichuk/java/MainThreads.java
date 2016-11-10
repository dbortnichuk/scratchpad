package edu.dbortnichuk.java;

/**
 * Created by dbort on 01.07.2016.
 */
public class MainThreads {



    public static void main(String[] args) {

        print1();

    }


    public static synchronized void print1(){
        System.out.println("println1");
        print2();
    }

    public static synchronized void print2(){
        System.out.println("println2");
    }
}
