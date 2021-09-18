package com.example.myapplication2.utils;

public class EditParameters {
    double color;
    double angle;


    public EditParameters(double color, double angle) {
        setColor(color);
        setAngle(angle);
    }

    public double getColor() {
        return color;
    }

    public void setColor(double color) {
        this.color = color;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
