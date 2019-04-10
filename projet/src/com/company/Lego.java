package com.company;

public class Lego {
    public int id;
    public int price;
    public int quantity;

    public Lego(int id, int price, int quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Lego{" +
                "id=" + id +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
