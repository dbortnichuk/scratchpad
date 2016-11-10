package edu.dbortnichuk.java.concurrency;

import edu.dbortnichuk.java.TestClass;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by dbort on 09.11.2016.
 */
public class TestClassMain {

    public static void main(String[] args)
            throws Exception {

        String fileName = "D:/temp/binaries/testfile";
        TestClass testWrite = new TestClass("valueOne", "valueTwo");
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(testWrite);
        oos.flush();
        oos.close();

        TestClass testRead;
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        testRead = (TestClass) ois.readObject();
        ois.close();

        System.out.println("--Serialized object--");
        System.out.println("propertyOne: " + testWrite.getPropertyOne());
        System.out.println("propertyTwo: " + testWrite.getPropertyTwo());
        System.out.println("");
        System.out.println("--Read object--");
        System.out.println("propertyOne: " + testRead.getPropertyOne());
        System.out.println("propertyTwo: " + testRead.getPropertyTwo());

    }
}
