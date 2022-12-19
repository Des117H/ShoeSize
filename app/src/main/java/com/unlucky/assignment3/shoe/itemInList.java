package com.unlucky.assignment3.shoe;

public class itemInList {

    private String name;
    private String subName;
    private int price;

    public itemInList(String name, String subName, int price){
        this.name = name;
        this.subName = subName;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getSubName() {
        return subName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @Override
    public String toString(){
        return this.name + this.subName + this.price;
    }
}
