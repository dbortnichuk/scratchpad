package edu.dbortnichuk.java.cert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbort on 09.11.2016.
 */
public class Ch3 {

//    public static void addSound(List<?> list) {
//        list.add("quack");
//    }
//
//    public static void addSound(List<? extends Object> list) {
//        list.add("quack");
//    }
//
//    public static void addSound(List<Object> list) {
//        list.add("quack");
//    }

    public static void addSound(List<? super String> list) {
        list.add("quack");
    }

    public static void main(String[] args) {

        List<String> strings = new ArrayList<String>();
        strings.add("tweet");
        List<Object> objects = new ArrayList<Object>(strings);
        addSound(strings);
        addSound(objects);


    }
}
