package edu.dbortnichuk.java;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dbort on 03.10.2016.
 */
public class CollectionMain {

    public static void main(String[] args) {

        List<String> collection = new ArrayList<>();
        collection.add("alice");
        collection.add("bob");
        collection.add("vasyl");
        List<String> transfColl = collection.stream().filter(s -> s.length() > 3).map(s -> s.toUpperCase()).collect(Collectors.toList());
        transfColl.forEach(System.out::println);

    }

}
