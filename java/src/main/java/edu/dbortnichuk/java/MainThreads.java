package edu.dbortnichuk.java;

/**
 * Created by dbort on 01.07.2016.
 */
public class MainThreads {



    public static void main(String[] args) {

        DTO sharedInstance = new DTO();

        TestRunnable tr1 = new TestRunnable("tr1", sharedInstance);
        Thread t1 = new Thread(tr1);


        TestRunnable tr2 = new TestRunnable("tr2", sharedInstance);
        Thread t2 = new Thread(tr2);

        t1.start();
        t2.start();

        System.out.println(t1.isInterrupted());
        t1.interrupt();
        Thread.interrupted();
        System.out.println(t1.isInterrupted());


    }
}
