package edu.dbortnichuk.java;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbortnichuk on 03-Oct-16.
 */
public class Person {

    private String name = "";
    private int age = 0;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }


    public static List<Person> getPeople(){
        List<Person> people = new ArrayList<>();
        people.add(new Person("Dima", 10));
        people.add(new Person("Alice", 11));
        people.add(new Person("Bob", 42));
        people.add(new Person("Taras", 22));
        people.add(new Person("Dima", 30));
        people.add(new Person("Sveta", 15));
        return people;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        return name != null ? name.equals(person.name) : person.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }
}
