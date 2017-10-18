package com.franks.restaurantmenuclient;

/**
 * Created by even_and_just on 10/17/17.
 */

public class Dish {
    private String dishName;
    private String dishDetails;
    private String dishPrice;
    private String dishImage;

    public Dish(){

    }

    public Dish(String name, String details, String price, String image){
        this.dishName = name;
        this.dishDetails = details;
        this.dishPrice = price;
        this.dishImage = image;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDetails() {
        return dishDetails;
    }

    public void setDishDetails(String dishDetails) {
        this.dishDetails = dishDetails;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }
}
