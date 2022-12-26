package com.unlucky.assignment3.shoe;

public class Shoe {
    public String name, style, pictureLink, colorway, releaseDate, description, brand;
    public double price;

    public Shoe() {
        this.name = "";
        this.style = "";
        this.colorway = "";
        this.pictureLink = "";
        this.releaseDate = "";
        this.description = "";
        this.brand = "";
        this.price = 0;
    }

    public Shoe(String name, String style, String colorway, String releaseDate,
                String description, double price) {
        this.name = name.replaceAll("-", " ");
        this.style = style;
        this.colorway = colorway;
        this.pictureLink = convertNameToPicLink(name);
        this.releaseDate = releaseDate;
        this.description = description;
        this.price = price;
    }

    public String convertNameToPicLink(String name) {
        String nameLink = this.name.replaceAll(" ", "-");
        nameLink = nameLink.replaceAll("[()]", "");
        return "https://images.stockx.com/360/" + nameLink +
                "/Images/" + nameLink + "/Lv2/img01.jpg?fm=jpg&amp;";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public String getColorway() {
        return colorway;
    }

    public void setColorway(String colorway) {
        this.colorway = colorway;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceString() {
        return "$" + price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
