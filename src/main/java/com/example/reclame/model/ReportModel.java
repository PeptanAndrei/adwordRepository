package com.example.reclame.model;

import java.util.Date;

public class ReportModel {
    private Date data;
    private double budget;
    private double costs;

    public ReportModel() {
    }

    public ReportModel(Date data, double budget, double costs) {
        this.data = data;
        this.budget = budget;
        this.costs = costs;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    public Date getData() {
        return data;
    }

    public double getBudget() {
        return budget;
    }

    public double getCosts() {
        return costs;
    }
}
