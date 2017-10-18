package com.franks.restaurantmenu;

/**
 * Created by Frank on 10/17/17.
 */

// Define a class for order
public class Order {
    // Be the same with vars name in the database
    private String username;
    private String dishname;

    public Order(){

    }

    public Order(String username, String dishname){
        this.username = username;
        this.dishname = dishname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }
}
