package com.example.myapplication2.utils;

public class EditParametersFilters {
    String color;
    double percentage;

    public EditParametersFilters(String color, double percentage) {
        setColor(color);
        setPercentage(percentage);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
