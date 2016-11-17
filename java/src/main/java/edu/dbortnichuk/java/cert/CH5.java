package edu.dbortnichuk.java.cert;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by dbort on 17.11.2016.
 */
public class CH5 {

    public static void main(String[] args) {

        System.out.println(ZoneId.getAvailableZoneIds().size());
        Instant now = Instant.now();
        //System.out.println(now);
        Instant later = Instant.now();
        //System.out.println(later);
        Duration duration = Duration.between(now, later);
        System.out.println(duration.getNano());

        Helper h = new Helper();
        System.out.println("" + h);

        Locale locale = Locale.getDefault();
        System.out.println(locale);

        Locale l1 = new Locale.Builder()
                .setLanguage("en")
                .setRegion("US")
                .build();

        System.out.println();

        Locale us = new Locale("en", "US");
        Locale france = new Locale("fr", "FR");

        printProperties(us);
        System.out.println();
        printProperties(france);

        ResourceBundle rb = ResourceBundle.getBundle("Zoo", us);
        Set<String> keys = rb.keySet();
        keys.stream().map(k -> k + " " + rb.getString(k))
                .forEach(System.out::println);


    }

    public static void printProperties(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("Zoo", locale);
        System.out.println(rb.getString("hello"));
        System.out.println(rb.getString("open"));
    }
}

class Helper {
//    @Override
//    public String toString() {
//        return "Helper{}";
//    }
}
