package edu.dbortnichuk.java.cert;

import javax.xml.bind.SchemaOutputResolver;
import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by dbort on 14.11.2016.
 */
public class CH4 {

    public static void main(String[] args) {

//        Supplier<LocalDate> s1 = LocalDate::now;
//        Supplier<LocalDate> s2 = () -> LocalDate.now();

//        LocalDate d1 = s1.get();
//        LocalDate d2 = s2.get();

        //System.out.println(d1);
        //System.out.println(d2);

        Predicate<String> egg = s -> s.contains("egg");
        Predicate<String> brown = s -> s.contains("brown");

        Predicate<String> brownEggs = egg.and(brown);
        Predicate<String> otherEggs = egg.and(brown.negate());

        //System.out.println(brownEggs.test("brown funny "));

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> pred = s -> s.equalsIgnoreCase("pred");
        Predicate<String> complex = isEmpty.and(pred).or(egg).and(brown);

        Stream<String> stream = Stream.of("w", "O", "l", "f", "");
        TreeSet<String> streamRes = stream.filter(s -> !s.isEmpty()).collect(Collectors.toCollection(TreeSet::new));

        //System.out.println(streamRes);

        List<Integer> storage = new ArrayList<>();
        Consumer<Integer> consumer = storage::add;

//        Stream<Integer> s = Stream.iterate(1, n -> n + 1);
//        s.skip(3).limit(2).forEach(consumer);

        //System.out.println(storage);

//        Stream<String> str1 = Stream.of("monkey", "gorilla", "bonobo");
//        str1.parallel().map(
//                string -> {
//                    System.out.println("thread: " + Thread.currentThread().getId());
//                    return string;
//                }
//
//        ).forEach(System.out::println);


        String text = "i want more of this soft tasty french cookies , and don't give anything sweet to Buster, especially cookies and brownies, cause I want them all for myself.";


        Map<String, Integer> wordFreq = Arrays.stream(text.split(" "))
                .filter(str -> !(str.isEmpty() || str.equals(",") || str.equals(".")))
                .map(String::toLowerCase)
                .map(str -> {
                    if (str.endsWith(".") || str.endsWith(",")) {
                        return str.substring(0, str.length() - 1);
                    } else {
                        return str;
                    }
                })
                .collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));


        //System.out.println(wordFreq);

//        DoubleStream random = DoubleStream.generate (Math::random);
//        random.forEach(dbl -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(dbl);
//        });

        DoubleStream streamLong = DoubleStream.of(1.0, 2.0, 3.0);

        DoubleUnaryOperator doubleUnaryOp = d -> d * 2;
        DoubleStream streamDouble = streamLong.map(doubleUnaryOp);

        //streamDouble.forEach(System.out::println);


        Stream.of(100).findAny().map(n -> "" + n)
                .filter(str -> str.length() == 3)
                .ifPresent(System.out::println);

        //Stream<String> ohMy = Stream.of("lions", "tigers", "bears");
        //Stream<Integer> ohMy = Stream.of(1, 1, 2);
//        Double result = ohMy.collect(Collectors.averagingInt(String::length));
//        System.out.println(result);

        TreeMap<Integer, String> map = Stream.of("lions", "tigers", "bears").collect(Collectors.toMap(
                String::length, k -> k, (s1, s2) -> s1 + "," + s2, TreeMap::new));
        System.out.println(map);

        Map<Integer, List<String>> map1 = Stream.of("lions", "tigers", "bears").collect(Collectors.groupingBy(String::length));
        System.out.println(map1);

        Map<Integer, Set<String>> map2 = Stream.of("lions", "tigers", "bears").collect(
                Collectors.groupingBy(String::length, Collectors.toSet()));
        System.out.println(map2);

        TreeMap<Integer, Set<String>> map3 = Stream.of("lions", "tigers", "bears").collect(
                Collectors.groupingBy(String::length, TreeMap::new, Collectors.toSet()));
        System.out.println(map3);

        Map<Boolean, Set<String>> map4 = Stream.of("lions", "tigers", "bears").collect(
                Collectors.partitioningBy(s -> s.length() <= 5, Collectors.toSet()));
        System.out.println(map4); // {false=[tigers], true=[lions, bears]}

        Map<Integer, Long> map5 = Stream.of("lions", "tigers", "bears").collect(Collectors.groupingBy(
                String::length, Collectors.counting()));
        System.out.println(map5);


    }



}

class Tuple<F, S> {
    F first;
    S second;
}


