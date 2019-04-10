package com.company;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    public List<Lego> myPieces = Main.myLegos;
    public List<LegoModel> allModels;
    int[] prices;

    public Solver(List<LegoModel> allModels, String[] stringPrices) {
        this.allModels = allModels;
        prices = new int[stringPrices.length];
        for (int i = 0; i < stringPrices.length; i++) {
            prices[i] = Integer.parseInt(stringPrices[i]);
        }
    }

    public List<LegoModel> solve() {
        boolean isSolved = false;
        List<LegoModel> solution = new ArrayList<>();
        while (!isSolved) {
            allModels.sort(LegoModel::compareTo);
            int i = 0;
            LegoModel modelToAdd = allModels.get(i);
            while (modelToAdd.totalCoveredPieces() <= 0 && i < allModels.size() - 1) {
                i++;
                modelToAdd = allModels.get(i);
            }
            solution.add(modelToAdd);
            calculateNewQuantities(modelToAdd);
            isSolved = calculateIfSolved();
        }
        // TODO now we have a suboptimal solution, lets remove models until we cant anymore
        removeUnnecessaryModels(solution);
        return solution;
    }

    private void removeUnnecessaryModels(List<LegoModel> solution) {
        int i = 0;
        while (i < solution.size()) {
            if (canRemoveModal(solution.get(i))) {
                removeModel(solution, i);
            } else
                i++;
        }
    }

    private void removeModel(List<LegoModel> solution, int i) {
        LegoModel modelToRemove = solution.remove(i);
        for (Lego lego : myPieces) {
            Lego other = modelToRemove.findById(lego.id);
            lego.quantity += other.quantity;
        }
    }

    private boolean canRemoveModal(LegoModel legoModel) {
        for (int i = 0; i < myPieces.size(); i++) {
            Lego myLego = myPieces.get(i);
            Lego other = legoModel.findById(myLego.id);
            if (myLego.quantity + other.quantity > 0)
                return false;
        }
        return true;
    }

    /**
     * Call once solution has been solved to get the cost of this solution
     *
     * @return cost
     */
    public int getCostOfSolution() {
        int totalCost = 0;
        for (Lego lego : myPieces) {
            if (lego.quantity < 0) {
                totalCost += (Math.abs(lego.quantity) * lego.price);
            }
        }
        return totalCost;
    }

    private void calculateNewQuantities(LegoModel legoModel) {
        for (int i = 0; i < legoModel.legos.size(); i++) {
            myPieces.get(i).quantity -= legoModel.legos.get(i).quantity;
        }
    }

    private boolean calculateIfSolved() {
        for (Lego lego : myPieces) {
            if (lego.quantity > 0)
                return false;
        }
        return true;
    }

    private List<Lego> legosRemainingToBeSpend() {
        List<Lego> remaining = new ArrayList<>();
        for (Lego lego : myPieces) {
            if (lego.quantity > 0) {
                remaining.add(lego);
            }
        }
        return remaining;
    }

}
