package edu.dbortnichuk.java.cert;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by d.bortnichuk on 2/9/17.
 */
public class GenParser {

    public static void main(String[] args) throws IOException {

//
//            List<String> lines = Files.lines(Paths.get("/Users/d.bortnichuk/Downloads/temp/ReportingAPI (prod) - top reports (1).csv"))
//                        .map(s -> s + )
//                         .filter(line -> line.contains("Generator")).collect(Collectors.toList());

        List<String> lines = Files.readAllLines(Paths.get("/Users/d.bortnichuk/Downloads/temp/ReportingAPI (prod) - top reports (1).csv"));


        int counter = 0;

            for(int i = 0; i < lines.size(); i++){
                int j = i + 1;
                String line = lines.get(i);
                if(line.contains("Generator") && !line.contains("PORTAL_REPORT")){
                    ++counter;
                    System.out.println(counter + ": " + j + ": " + line);
                }


            }


}

}
