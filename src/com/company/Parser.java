package com.company;

import java.util.*;

public class Parser {
    Map<String, Integer> termToInd;
    Map<Integer, ArrayList<Map.Entry<Integer, Boolean>>> contains;

    Map<Integer, ArrayList<Map.Entry<Integer, Boolean>>> formulaContains;

    ArrayList<Integer> lengths;
    int currFormulaInd;
    int currTermInd;
    Parser(String formula){
        currFormulaInd = -1;
        currTermInd = 0;
        termToInd = new HashMap<>();
        contains = new HashMap<>();
        formulaContains = new HashMap<>();
        lengths = new ArrayList<>();
        Iterable<Iterator<String>> tokenizer = new Tokenizer(formula);
        for (Iterator<String> clause : tokenizer) {
            currFormulaInd++;
            formulaContains.put(currFormulaInd, new ArrayList<>());
            int literalCount = 0;
            boolean prefix = true;
            while (clause.hasNext()) {
                var literal = clause.next();
                if (literal.charAt(0) == '~'){
                    prefix = false;
                } else {
                    int termInd = currTermInd;
                    if (!termToInd.containsKey(literal)){
                        termToInd.put(literal, currTermInd);
                        contains.put(currTermInd, new ArrayList<>());
                        currTermInd++;
                    } else {
                        termInd = termToInd.get(literal);
                    }
                    contains.get(termInd).add(new AbstractMap.SimpleEntry<>(currFormulaInd, prefix));
                    formulaContains.get(currFormulaInd).add(new AbstractMap.SimpleEntry<>(termInd, prefix));
                    prefix = true;
                    literalCount++;
                }
            }
            lengths.add(literalCount);
        }
    }

    public Map<String, Integer> getTermToInd() {
        return termToInd;
    }

    public Map<Integer, ArrayList<Map.Entry<Integer, Boolean>>> getContains() {
        return contains;
    }

    public ArrayList<Integer> getLengths() {
        return lengths;
    }

    public Map<Integer, ArrayList<Map.Entry<Integer, Boolean>>> getFormulaContains() {
        return formulaContains;
    }

    public static void main(String[] args) {
        var parser = new Parser("{a2, ~b1a2cb~g, a1bb1~c}");
        System.out.println("Lengths: " + parser.lengths);
        System.out.println("Term to Ind: " + parser.termToInd);
        System.out.println("mappings: " + parser.contains);
        System.out.println("Formula mappings: " + parser.formulaContains);
    }
}
