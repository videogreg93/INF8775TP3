package com.company;

import java.util.ArrayList;
import java.util.List;

public class LegoModel implements Comparable {
    public int id;
    public ArrayList<Lego> legos;
    public List<Lego> myPieces = Main.myLegos;

    public LegoModel(int id, ArrayList<Lego> legos) {
        this.id = id;
        this.legos = legos;
    }

    // Returns how many pieces this model removes from my collection
    public int totalRemovedPieces() {
        int total = 0;
        for (Lego lego : myPieces) {
            Lego modelLego = findById(lego.id);
            if (lego.quantity - modelLego.quantity >= 0) {
                total += modelLego.quantity;
            } else {
                total += Math.max(lego.quantity, 0);
            }
        }
        return total;
    }

    // Return the cost to buy the missing pieces of the model
    public int costOfModel() {
        int totalCost = 0;
        for (Lego lego : myPieces) {
            Lego modelLego = findById(lego.id);
            if (lego.quantity > 0) {
                if (lego.quantity - modelLego.quantity < 0) {
                    totalCost += (Math.abs(lego.quantity - modelLego.quantity) * lego.price);
                }
            } else {
                totalCost += (modelLego.quantity * lego.price);
            }
        }
        return totalCost;
    }

    // Return the global price of the model
    public int priceOfModel() {
        int totalPrice = 0;
        for (Lego lego : legos) {
            totalPrice += (lego.quantity * lego.price);
        }
        return totalPrice;
    }

    public Lego findById(int id) {
        for (Lego lego : legos) {
            if (lego.id == id) {
                return lego;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Object o) {
        LegoModel other = (LegoModel) o;
        // Favor the model that remove the most legos
        if (other == null) {
            return 0;
        } else {
            // If both models cover the same amount of legos, take the one that cost the less to add
            if (this.totalRemovedPieces() > other.totalRemovedPieces()) {
                return -1;
            } else if (this.totalRemovedPieces() == other.totalRemovedPieces()) {
                if (this.costOfModel() > other.costOfModel())
                    return 1;
                else
                    return -1;
            } else {
                return 1;
            }
        }
    }

    @Override
    public String toString() {
        return "LegoModel{" +
                "id=" + id +
                ", legos=" + legos +
                ", myPieces=" + myPieces +
                '}';
    }
}
