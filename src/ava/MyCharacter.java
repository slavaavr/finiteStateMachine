package ava;

import java.util.Collections;
import java.util.Set;

/**
 * Created by slava on 24.11.2017.
 */
public class MyCharacter {
    public String value;
    public Set<Integer> before;
    public Set<Integer> after;

    @Override
    public String toString() {
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        for (Integer integer : before) {
            s1.append(" ").append(integer);
        }
        for (Integer integer : after) {
            s2.append(" ").append(integer);
        }
        return "MyCharacter{" +
                "value='" + value + '\'' +
                ", before=" + s1.toString() +
                ", after=" + s2.toString() +
                '}';
    }
}
