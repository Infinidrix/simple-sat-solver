package com.company;

public class TestRunner {

    public static void main(String[] args) {
        String startingForm = "C:\\\\Users\\\\Biruk\\\\IdeaProjects\\\\SATSolver\\\\tests\\\\uuf50-0";
        String suffix = ".cnf";
        int end = 1000;
        boolean satisfiable = false;
        int count = 0;
        for (int i = 1; i <= end; i++ ){
            String filename = startingForm + i + suffix;
            TestFormatter formatter = new TestFormatter(filename);
            String result = new Solver(formatter.formatTest()).toString();
            if (result.equals("Unsatisfiable") == satisfiable){
                count++;
                System.out.println("Wrong on test case: " + i);
            }
        }
        System.out.println("Finished " + end + " test cases. Failed on " + count + " testcases.");
    }

}
