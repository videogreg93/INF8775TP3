package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static String filename; //= "./exemplaires/LEGO_100_100_2000";
    static int totalLegoTypes;
    public static ArrayList<Lego> myLegos = new ArrayList<>();
    static ArrayList<LegoModel> models = new ArrayList<>();
    static int[] formattedSolution;
    static String[] prices;

    public static void main(String[] args) {
        try {
            filename = args[0];
            readTextFile(filename);
            Solver solver = new Solver(models, prices);
            List<LegoModel> solution = solver.solve();
            System.out.println("Solution cost: " + solver.getCostOfSolution());
            formattedSolution = new int[models.size()];
            for (int i = 0; i < models.size(); i++) {
                formattedSolution[i] = 0;
            }
            for (int i = 0; i < solution.size(); i++) {
                int index = solution.get(i).id;
                formattedSolution[index] += 1;
            }
            for (int i = 0; i < formattedSolution.length; i++) {
                System.out.print(formattedSolution[i] + " ");
            }
            write("solution", formattedSolution);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        totalLegoTypes = Integer.parseInt(allLines.remove(0));
        String[] quantities = allLines.remove(0).split(" ");
        prices = allLines.remove(0).split(" ");
        int id = 0;
        for (String quantity : quantities) {
            myLegos.add(new Lego(id, Integer.parseInt(prices[id]), Integer.parseInt(quantity)));
            id++;
        }
        // Read models
        int modelQuantity = Integer.parseInt(allLines.remove(0));
        for (int i = 0; i < modelQuantity; i++) {
            String[] legoStrings = allLines.remove(0).split(" ");
            id = 0;
            ArrayList<Lego> modelLegos = new ArrayList<>();
            for (String legoString : legoStrings) {
                modelLegos.add(new Lego(id, Integer.parseInt(prices[id]), Integer.parseInt(legoString)));
                id++;
            }
            models.add(new LegoModel(i, new ArrayList<>(modelLegos)));
        }
        System.out.println("done reading textfile");
    }

    public static void write (String filename, int[]x) throws IOException{
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < x.length; i++) {
            outputWriter.write(x[i]+ " ");
        }
        outputWriter.flush();
        outputWriter.close();
    }
}
