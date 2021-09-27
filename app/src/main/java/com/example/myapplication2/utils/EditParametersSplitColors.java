package com.example.myapplication2.utils;

public class EditParametersSplitColors {
    double color;
    double spreadX;
    double spreadY;


    public EditParametersSplitColors(double color, double spreadX, double spreadY) {
        setColor(color);
        setSpreadX(spreadX);
        setSpreadY(spreadY);

    }

    public double getColor() {
        return color;
    }

    public void setColor(double color) {
        this.color = color;
    }

    public double getSpreadX() {
        return spreadX;
    }

    public void setSpreadX(double spreadX) {
        this.spreadX = spreadX;
    }

    public double getSpreadY() {
        return spreadY;
    }

    public void setSpreadY(double spreadY) {
        this.spreadY = spreadY;
    }
}
