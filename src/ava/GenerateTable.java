package ava;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by slava on 25.11.2017.
 */
public class GenerateTable {
    private MyCharacter[] arrChar;
    private FileWriter fw;
    private int countOfStates;
    private Table table;

    public GenerateTable(MyCharacter[] arrChar, String outputFile, int countOfStates) {
        this.arrChar = arrChar;
        this.countOfStates = countOfStates;

        try {
            fw = new FileWriter(outputFile, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        table = new Table();

    }

    public void print() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= countOfStates; i++) {
            stringBuilder.append("\t").append(i);
        }
        stringBuilder.append("\n");
        Set<String> set = new HashSet<>();
        for (MyCharacter myCharacter : arrChar) {
            if (!myCharacter.value.equals("(") && !myCharacter.value.equals(")") && !myCharacter.value.equals("{") && !myCharacter.value.equals("}") && !myCharacter.value.equals("|")) {
                if (set.add(myCharacter.value)) {
                    stringBuilder.append(myCharacter.value).append("\t");
                    for (int i = 0; i < countOfStates; i++) {
                        stringBuilder.append(table.getCeil(myCharacter.value, i)).append("\t");
                    }
                    stringBuilder.append("\n");
                }
            }
        }
        fw.write(stringBuilder.toString());
        fw.flush();
    }

    class Table {
        private StringBuffer[][] table;
        private Map<String, Integer> rows;

        Table() {
            this.rows = new HashMap<>();
            Set<String> set = new HashSet<>();
            int temp = 0;
            for (int i = 0; i < arrChar.length; i++) {
                if (!arrChar[i].value.equals("(") && !arrChar[i].value.equals(")") && !arrChar[i].value.equals("{") && !arrChar[i].value.equals("}") && !arrChar[i].value.equals("|")) {
                    if (set.add(arrChar[i].value)) {
                        this.rows.put(arrChar[i].value, temp++);
                    }
                }
            }
            table = new StringBuffer[set.size()][countOfStates];
            for (int j = 0; j < set.size(); j++) {
                for (int k = 0; k < countOfStates; k++) {
                    table[j][k] = new StringBuffer();
                }
            }
            for (MyCharacter myCharacter : arrChar) {
                if (!myCharacter.value.equals("(") && !myCharacter.value.equals(")") && !myCharacter.value.equals("{") && !myCharacter.value.equals("}") && !myCharacter.value.equals("|")) {
                    for (Integer integer : myCharacter.before) {
                        getCeil(myCharacter.value, integer - 1).append(myCharacter.after.toArray()[0]).append(" ");
                    }
                }
            }
        }

        StringBuffer getCeil(String row, Integer column) {
            return table[rows.get(row)][column];
        }
    }
}
