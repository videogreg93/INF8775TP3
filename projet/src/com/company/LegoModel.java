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

    /**
     * Returns how many pieces this model removes from my collection
     *
     * @return
     */
    public int totalCoveredPieces() {
        int total = 0;
        for (Lego lego : myPieces) {
            Lego modelLego = findById(lego.id);
            if (lego.quantity - modelLego.quantity >= 0) {
                total += modelLego.quantity;
            } else {
                total += Math.max(lego.quantity, 0);
            }
//            else {
//                total += (lego.quantity - modelLego.quantity) * modelLego.price; // factor in price when choosing models that require buying pieces
//            }
        }
        return total;
    }


    public boolean bringsAQuantityBelowZero() {
        for (Lego lego : myPieces) {
            Lego modelLego = findById(lego.id);
            if (lego.quantity - modelLego.quantity < 0)
                return true;
        }
        return false;
    }

    /**
     * Cost to buy the missing pieces of the model
     * @return cost
     */
    public int costOfModel() {
        int totalCost = 0;
        for (Lego lego : myPieces) {
            Lego modelLego = findById(lego.id);
            if (lego.quantity - modelLego.quantity < 0) {
                totalCost += (Math.abs(lego.quantity - modelLego.quantity) * lego.price);
            }
        }
        return totalCost;
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
        // Favor the model that doesnt bring a quantity below 0
        if (other == null) {
            return 0;
        } else {
            if (!this.bringsAQuantityBelowZero() && !other.bringsAQuantityBelowZero()) {
                // If neither requires buying additional legos, pick the one that covers the most legos
                if (this.totalCoveredPieces() > other.totalCoveredPieces())
                    return 1;
                else
                    return -1;
            } else if (this.bringsAQuantityBelowZero() && other.bringsAQuantityBelowZero()) {
                // If both require buying legos, choose the one that cost lestt
                if (this.costOfModel() < other.costOfModel())
                    return 1;
                else
                    return -1;
            } else {
                if (other.bringsAQuantityBelowZero())
                    return 1;
                else
                    return -1;
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
