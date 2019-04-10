package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static String filename = "./exemplaires/LEGO_50_50_1000.txt";
    static int totalLegoTypes;
    public static ArrayList<Lego> myLegos = new ArrayList<>();
    static ArrayList<LegoModel> models = new ArrayList<>();
    static String[] prices;

    public static void main(String[] args) {
        try {
            readTextFile(filename);
            Solver solver = new Solver(models, prices);
            List<LegoModel> solution = solver.solve();
            System.out.println("Solution cost: " + solver.getCostOfSolution());
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
}
