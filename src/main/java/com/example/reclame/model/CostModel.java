package com.example.reclame.model;

import java.util.Date;

public class CostModel {
    private Date data;
    private double cost;

    public CostModel()
    {
        super();
    }
    public CostModel(Date data, double cost) {
        this.data = data;
        this.cost = cost;
    }

    public Date getData() {
        return data;
    }

    public double getCost() {
        return cost;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
