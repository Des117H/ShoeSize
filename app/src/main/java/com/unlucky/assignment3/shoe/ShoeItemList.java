package com.unlucky.assignment3.shoe;

public class ShoeItemList {
    private String name;
    private String pictureLink;
    private int price;

    public ShoeItemList(String name, String pictureLink, int price){
        this.name = name;
        this.pictureLink = pictureLink;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    @Override
    public String toString(){
        return this.name + " " + this.price;
    }
}
