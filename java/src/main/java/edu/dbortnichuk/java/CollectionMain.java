package edu.dbortnichuk.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by dbort on 03.10.2016.
 */
public class CollectionMain {

    public static void main(String[] args) {


        Map<String, Long> counting = Person.getPeople().parallelStream()
                .filter(p -> {
                    System.out.println(Thread.currentThread().getName());
                    return p.getAge() > 20;
                })
                //.map(p -> p.getName().toUpperCase())
                //.sorted()
                .collect(
                        Collectors.groupingBy(Person::getName, Collectors.counting()));
        counting.forEach((k, v) -> System.out.println(k + v));

    }

}
