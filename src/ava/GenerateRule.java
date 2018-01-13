package ava;

import java.lang.*;
import java.util.*;

/**
 * Created by slava on 24.11.2017.
 */
public class GenerateRule {
    private String line;
    private MyCharacter[] arrChar;
    private Integer countOfStates = 0;

    public GenerateRule(String line) {
        line = line.trim();
        this.line = line;
        arrChar = new MyCharacter[this.line.length()];
        for (int i = 0; i < this.line.length(); i++) {
            arrChar[i] = new MyCharacter();
            arrChar[i].value = Character.toString(this.line.charAt(i));
            arrChar[i].before = new HashSet<>();
            arrChar[i].after = new HashSet<>();
        }
    }

    public Integer getCountOfStates() {
        return countOfStates;
    }

    public MyCharacter[] generate() {
        for (int i = 0; i < this.arrChar.length; i++) {
            if(!arrChar[i].value.equals("|") && !arrChar[i].value.equals("{") && !arrChar[i].value.equals("(") && !arrChar[i].value.equals(")") && !arrChar[i].value.equals("}") ){
                rule1(i);
            } else if (arrChar[i].value.equals("(") || arrChar[i].value.equals("{")){
                rule2(new Int(i));
            } else if (arrChar[i].value.equals("}") || arrChar[i].value.equals(")")){
                rule3(i);
                if(arrChar[i].value.equals("}")){
                    rule4(i);
                }
            }
        }
        return arrChar;
    }

    // обычный символ
    private void rule1(int index) {
        if (arrChar[index].before.size() == 0) {
            arrChar[index].before.add(++countOfStates);
        }
        arrChar[index].after.add(++countOfStates);
        if (index < this.arrChar.length - 1) {
            if(!arrChar[index+1].value.equals("}") && !arrChar[index+1].value.equals(")")){
                arrChar[index + 1].before.add(countOfStates);
            }
        }
    }

    // Начальные места всех термов, помещенного в обычные или итерационные скобки, подчинены месту, расположенному непосредственно слева от открывающей скобки
    // index - индекс открывающей скобки обычной или итерационной.
    private void rule2(Int index) {
        int permanentIndex = index.value;
        if (arrChar[index.value].before.size() == 0) {
            arrChar[index.value].before.add(++countOfStates);
        }
        if (arrChar[index.value].value.equals("{")) {
            while (!arrChar[index.value].value.equals("}")) {
                ++index.value;
                if(index.value > arrChar.length-1){
                    System.err.println("Нехватает закрывающей скобки!1");
                    System.exit(1);
                }
                if(!arrChar[index.value].value.equals("|") && !arrChar[index.value].value.equals("}")){
                    copy(arrChar[index.value].before,arrChar[permanentIndex].before);
                }
                if (arrChar[index.value].value.equals("{") || arrChar[index.value].value.equals("(")) {
                    rule2(index);
                }
            }
        } else if (arrChar[index.value].value.equals("(")) {
            while (!arrChar[index.value].value.equals(")")) {
                ++index.value;
                if(index.value > arrChar.length-1){
                    System.err.println("Нехватает закрывающей скобки!2");
                    System.exit(1);
                }
                if(!arrChar[index.value].value.equals("|") && !arrChar[index.value].value.equals(")")){
                    copy(arrChar[index.value].before,arrChar[permanentIndex].before);
                }
                if (arrChar[index.value].value.equals("{") || arrChar[index.value].value.equals("(")) {
                    rule2(index);
                }
            }
        }
    }
    //Место расположенное непосредственно справа от закрывающей скобки, подчинено
    //конечным местам всех термов многочлена, заключенного в эти скобки; а в случае
    //итерационных скобок – еще и месту, расположенному непосредственно слева от
    //соответствующей открывающей скобки.
    // index - индекс закрывающей скобки обычной или итерационной.
    private void rule3(int index){
        int permanentIndex = index;
        if(arrChar[index].value.equals("}")){
            while (!arrChar[index].value.equals("{")) {
                --index;
                if (arrChar[index].value.equals("}") || arrChar[index].value.equals(")")) {
                    rule3(index);
                }else if(arrChar[index].value.equals("{")){
                    copy(arrChar[permanentIndex].after, arrChar[index].before);
                    if(permanentIndex+1 < arrChar.length){
                        copy(arrChar[permanentIndex+1].before, arrChar[permanentIndex].after);
                    }
                }
                if(!arrChar[index].value.equals("|")){
                    copy(arrChar[permanentIndex].after, arrChar[index].after);
                }
            }
        } else if (arrChar[index].value.equals(")")) {
            while (!arrChar[index].value.equals("(")) {
                --index;
                if (arrChar[index].value.equals("}") || arrChar[index].value.equals(")")) {
                    rule3(index);
                } else if(arrChar[index].value.equals("(")){
                    if(permanentIndex+1 < arrChar.length){
                        copy(arrChar[permanentIndex+1].before, arrChar[permanentIndex].after);
                    }
                }
                if(!arrChar[index].value.equals("|")){
                    copy(arrChar[permanentIndex].after, arrChar[index].after);
                }
            }
        }
    }

    //Начальные места всех термов многочлена, заключенного в итерационные скобки,
    //подчинены месту, расположенному непосредственно справа от соответствующей
    //закрывающей скобки.
    private void rule4(int index){
        if(arrChar[index].value.equals("}")){
            int permanentIndex = index;
            while(!arrChar[index].value.equals("{")){
                --index;
                if(arrChar[index].value.equals("}")){
                    rule4(index);
                }
                if(!arrChar[index].value.equals("|") && !arrChar[index].value.equals("{")){
                    copy(arrChar[index].before,arrChar[permanentIndex].after);
                }
            }
        }
    }

    private void copy(Set<Integer> destination, Set<Integer> source){
        for (Integer integer : source) {
            destination.add(integer);
        }
    }

    public class Int {
        public int value;
        public Int(int value) {
            this.value = value;
        }
    }

}
