package com.company;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    public List<Lego> myPieces = Main.myLegos;
    public List<LegoModel> allModels;

    public Solver(List<LegoModel> allModels, String[] stringPrices) {
        this.allModels = allModels;
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
        int index = 0;
        Boolean hasBetterSolution = true;
        while (hasBetterSolution) {
            List<LegoModel> neighbors = findNeighbors(solution, index);
            LegoModel bestNeighbor;
            if (neighbors.size() <= 0) {
                index ++;
            } else {
                bestNeighbor = findBestNeighbors(neighbors);
                removeModel(solution, index);
                solution.add(bestNeighbor);
                calculateNewQuantities(bestNeighbor);
            }
            if (index >= solution.size()) {
                hasBetterSolution = false;
            }
        }
        removeUnnecessaryModels(solution);
        return solution;
    }

    private void removeUnnecessaryModels(List<LegoModel> solution) {
        int i = 0;
        while (i < solution.size()) {
            if (canRemoveModel(solution.get(i))) {
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

    private boolean canRemoveModel(LegoModel legoModel) {
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

    public List<LegoModel> findNeighbors(List<LegoModel> solution, int index) {
        List<LegoModel> neighbors = new ArrayList<LegoModel>();
        for (LegoModel model : allModels) {
            if(canBeNeighbor(solution.get(index), model)) {
                neighbors.add(model);
            }
        }
        return neighbors;
    }

    private boolean canBeNeighbor(LegoModel currentModel, LegoModel possibleNeighbor) {
        if (currentModel.priceOfModel() < possibleNeighbor.priceOfModel() || currentModel.id == possibleNeighbor.id) {
            return false;
        }
        for (int i = 0; i < myPieces.size(); i++) {
            Lego myLego = myPieces.get(i);
            Lego currentLego = currentModel.findById(myLego.id);
            Lego possibleLego = possibleNeighbor.findById(myLego.id);
            if (myLego.quantity + currentLego.quantity - possibleLego.quantity > 0)
                return false;
        }
        return true;
    }

    public LegoModel findBestNeighbors(List<LegoModel> neighbors) {
        LegoModel bestNeighbor = neighbors.get(0);
        for (LegoModel model : neighbors) {
            if (model.priceOfModel() > bestNeighbor.priceOfModel()) {
                bestNeighbor = model;
            }
        }
        return  bestNeighbor;
    }
}
