package com.company;
import java.io.*;
import java.util.*;

public class TestFormatter {
    MyScanner scanner;
    String filename;
    TestFormatter(String filename) {
        this.filename = filename;
    }

    String formatTest() {
        var file = new File(filename);
        FileReader reader;
        try {
            reader = new FileReader(file);
            scanner = new MyScanner(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Error on file";
        }
        String line;
        for(line = scanner.nextLine(); line.charAt(0) != 'p'; line=scanner.nextLine());
        var sp = line.split(" ");

        int n = Integer.parseInt(sp[2]), m = Integer.parseInt(sp[4]);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        for (int i = 0; i < m; i++){
            while (true){
                int nextNum = scanner.nextInt();
                if (nextNum == 0){
                    break;
                }
                if (nextNum < 0){
                    stringBuilder.append('~');
                }
                stringBuilder.append('a').append(Math.abs(nextNum));
            }
            if (i + 1 != m)
                stringBuilder.append(", ");
        }
        stringBuilder.append('}');
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        var test = new TestFormatter("C:\\Users\\Biruk\\IdeaProjects\\SATSolver\\tests\\uf20-01.cnf");
        System.out.println(test.formatTest());
    }

    //-----------MyScanner class for faster input----------
    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;

        public MyScanner(InputStreamReader inputStream) {
            br = new BufferedReader(inputStream);
        }

        public MyScanner(){
            this(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine(){
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

    }
    //--------------------------------------------------------
}
