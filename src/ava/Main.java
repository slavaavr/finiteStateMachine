package ava;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by slava on 24.11.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("line.txt"), StandardCharsets.UTF_8);
        String line = lines.remove(0);
        System.out.println(line);
        GenerateRule gr = new GenerateRule(line);
        MyCharacter[] rules = gr.generate();
        Integer countOfStates = gr.getCountOfStates();
        GenerateTable gt = new GenerateTable(rules, "output.txt", countOfStates);
        gt.print();
        System.out.println(Arrays.deepToString(rules));
    }

}
