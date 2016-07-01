package edu.dbortnichuk.java;

/**
 * Created by dbort on 01.07.2016.
 */
public class DTO {

    private String strVal = "";

    public synchronized String getStrVal() {
        return strVal;
    }

    public synchronized void setStrVal(String strVal) {
        this.strVal = strVal;
    }
}
