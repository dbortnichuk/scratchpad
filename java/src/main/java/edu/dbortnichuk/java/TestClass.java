package edu.dbortnichuk.java;

/**
 * Created by dbort on 09.11.2016.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestClass implements Serializable {

    private static final long serialVersionUID = -2518143671167959230L;

    private String propertyOne;
    private String propertyTwo;

    public TestClass(String propertyOne, String propertyTwo) {
        this.propertyOne = propertyOne;
        this.propertyTwo = propertyTwo;
        validate();
    }

    private void writeObject(ObjectOutputStream o)
            throws IOException {

        o.writeObject(propertyOne);
        o.writeObject(propertyTwo);
    }

    private void readObject(ObjectInputStream o)
            throws IOException, ClassNotFoundException {

        propertyOne = (String) o.readObject();
        propertyTwo = (String) o.readObject();
        validate();
    }

    private void validate(){
        if(propertyOne == null ||
                propertyOne.length() == 0 ||
                propertyTwo == null ||
                propertyTwo.length() == 0){

            throw new IllegalArgumentException();
        }
    }

    public String getPropertyOne() {
        return propertyOne;
    }

    public String getPropertyTwo() {
        return propertyTwo;
    }

}
