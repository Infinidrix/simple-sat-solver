package com.company;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class Tokenizer implements Iterable<Iterator<String>>{
    int index;
    String formula;
    Tokenizer(String formula){
        index = 0;
        this.formula = formula;
    }

    @Override
    public Iterator<Iterator<String>> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                if (formula.charAt(index) == '}') {
                    return false;
                }
                return true;
            }

            @Override
            public Iterator<String> next() {
                index++;
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        if (formula.charAt(index) == '}' || formula.charAt(index) == ',') {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public String next() {
                        StringBuilder stringBuilder = new StringBuilder();
                        while (true) {
                            char currentChar = formula.charAt(index);
                            if (currentChar == ',' || currentChar == '}') {
                                return stringBuilder.toString();
                            } else if (currentChar == ' ' || currentChar == '\n') {

                            } else if (Character.isAlphabetic(currentChar) || currentChar == '~') {
                                if (stringBuilder.length() == 0) {
                                    stringBuilder.append(currentChar);
                                } else {
                                    return stringBuilder.toString();
                                }
                            } else {
                                stringBuilder.append(currentChar);
                            }
                            index++;
                        }
                    }
                };
            }
        };
    }

    public static void main(String[] args) {
        var fomula = new Tokenizer("{ab, ~a~b, a12, b12~a12}");

        for (Iterator<String> o : fomula) {
            System.out.println("Clause: ");
            while (o.hasNext()) {
                var literal = o.next();
                System.out.println("\tLiteral: " + literal);
            }
        }
    }
}
