package com.company;

import java.util.*;
import java.util.stream.IntStream;

public class Solver {
    int[] values, lengths;
    boolean[] deleted;
    Parser parser;

    final static int ASS = 1;
    final static int DEL = 2;
    final static int DEC = 3;

    Map<Integer, ArrayList<Map.Entry<Integer, Boolean>>> contains, formulaContains;
    Stack<Map.Entry<Integer, Integer>> actionStack;

    Solver(String formula){
        parser = new Parser(formula);
        int literalCount = parser.currTermInd;
        values = new int[literalCount];
        lengths = parser.lengths.stream().mapToInt(i -> i).toArray();
        deleted = new boolean[lengths.length];
        actionStack = new Stack<>();

        contains = parser.contains;
        formulaContains = parser.formulaContains;
    }

    public ArrayDeque<Map.Entry<Integer, Boolean>> getUnitLiterals(){
        ArrayDeque<Map.Entry<Integer, Boolean>> queue = new ArrayDeque<>();
        // Check for unit literals && pure literals
        for (int i = 0; i < lengths.length; i++){
            if (!deleted[i] && lengths[i] == 1){
                for (var terms : formulaContains.get(i)) {
                    if (values[terms.getKey()] == 0) {
                        queue.add(terms);
                    }
                }
            }
        }
        return queue;
    }

    public boolean findClashes(){
        for (int i = 0; i < lengths.length; i++){
            if (!deleted[i] && lengths[i] == 0) { // check for clashes
                    return true;
            }
        }
        return false;
    }

    public void propagateChanges(int index){
        for (var formulas : contains.get(index)){
            int ind = formulas.getKey();
            if (deleted[ind])
                continue;
            if (formulas.getValue()  == (values[index] == 1)){
                deleted[ind] = true;
                actionStack.add(Map.entry(DEL, ind));
            } else {
                lengths[ind]--;
                actionStack.add(Map.entry(DEC, ind));
                if (lengths[ind] == 0){
                    return ;
                }
            }
        }
    }

    public void revertUntil(int size){
        while (actionStack.size() > size){
            var curr = actionStack.pop();
            switch (curr.getKey()){
                case ASS:
                    values[curr.getValue()] = 0;
                    break;
                case DEL:
                    deleted[curr.getValue()] = false;
                    break;
                case DEC:
                    lengths[curr.getValue()]++;
            }
        }
    }

    public boolean solve(){
        int currSize = actionStack.size();
        if (findClashes()){
            return false;
        }
        var queue = getUnitLiterals();

        while (!queue.isEmpty()){
            var curr = queue.pollFirst();
            if (values[curr.getKey()] != 0){
                continue;
            }
            values[curr.getKey()] = 1;
            if (!curr.getValue()){
                values[curr.getKey()] = -1;
            }
            actionStack.add(Map.entry(ASS, curr.getKey()));
            propagateChanges(curr.getKey());
            if (findClashes()){
                revertUntil(currSize);
                return false;
            }
            queue.addAll(getUnitLiterals());
        }
        int targetInd = -1;
        for (int i = 0; i < values.length; i++){
            if (values[i] == 0){
                targetInd = i;
                break;
            }
        }
        if (targetInd == -1){
            return true;
        }
        int localSize = actionStack.size();

        values[targetInd] = 1;
        actionStack.add(Map.entry(ASS, targetInd));
        propagateChanges(targetInd);

        if (solve()){
            return true;
        }
        revertUntil(localSize);

        values[targetInd] = -1;
        actionStack.add(Map.entry(ASS, targetInd));

        propagateChanges(targetInd);
        if (solve()){
            return true;
        }
        revertUntil(currSize);
        return false;
    }

    public String toString(){
        boolean canSolve = solve();
        if (!canSolve){
            return "Unsatisfiable";
        }
        StringBuilder stringBuilder = new StringBuilder();

        for (var term : parser.termToInd.entrySet()){
            stringBuilder.append(term.getKey())
                    .append(" = ")
                    .append(values[term.getValue()] == 1 ? "True" : "False")
                    .append("\n");

        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        var solver = new Solver("{a4~a18a19, a3a18~a5, ~a16a9a15, a11~a5~a14, a18~a10a13, ~a3a11a12, ~a6~a17~a8, ~a18a14a1, ~a18~a2a20, a20a12a4, a19a11a14, ~a16a18~a4, a6a10a7, a20a14~a16, ~a5a12a15, ~a4~a9~a13, a12~a11~a7, ~a5a19~a8, a1a16a17, a20~a14~a15, a13~a4a10, a14a7a10, ~a16~a15~a1, a16a3~a11, ~a15~a10a4, a4~a15~a3, ~a13~a5~a1, ~a7~a2a12, a1~a20a19, ~a2~a13~a8, a15a18a4, ~a11a14a9, ~a6~a15~a2, a5~a12~a15, ~a6a17a5, ~a13a5~a19, a20~a1a14, a9~a17a15, ~a5a19~a18, ~a12a8~a10, ~a18a14~a4, a15~a9a13, a9~a5~a1, a10~a19~a14, a20a9a4, ~a9~a2a19, ~a5a13~a17, a2~a10~a18, ~a9a1~a5, ~a19a9a17, a12~a2a17, a4~a16~a5}");
        System.out.println(solver);
    }
}
