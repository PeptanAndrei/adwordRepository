package com.example.reclame.model;

import java.util.Date;

public class AdModel {
    public AdModel()
    {
        super();
    }
    public AdModel(Date date, double budget) {
        this.date = date;
        this.budget = budget;
    }


    private Date date;

    private double budget;

    public Date getDate() {
        return date;
    }

    public double getBudget() {
        return budget;
    }

    public void setData(Date date) {
        this.date = date;
    }

    public void setCost(double budget) {
        this.budget = budget;
    }

}
