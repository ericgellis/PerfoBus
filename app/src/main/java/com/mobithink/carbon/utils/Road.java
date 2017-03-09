package com.mobithink.carbon.utils;

/**
 * Created by mplaton on 09/03/2017.
 */

public class Road {

    public String mName;
    public String mDescription;
    public int mColor;
    public int mWidth;
    public double[][] mRoute = new double[][] {};
    public Point[] mPoints = new Point[] {};

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public double[][] getmRoute() {
        return mRoute;
    }

    public void setmRoute(double[][] mRoute) {
        this.mRoute = mRoute;
    }

    public Point[] getmPoints() {
        return mPoints;
    }

    public void setmPoints(Point[] mPoints) {
        this.mPoints = mPoints;
    }
}
