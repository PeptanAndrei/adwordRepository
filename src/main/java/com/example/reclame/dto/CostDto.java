package com.example.reclame.dto;

import com.example.reclame.model.CostModel;
import com.example.reclame.model.ReportModel;

import java.util.List;

public class CostDto {
    private List<CostModel> costList;
    private List<ReportModel> reportModelList;

    public CostDto() {
    }

    public CostDto(List<CostModel> costList, List<ReportModel> reportModelList) {
        this.costList = costList;
        this.reportModelList = reportModelList;
    }

    public void setCostList(List<CostModel> costList) {
        this.costList = costList;
    }

    public void setReportModelList(List<ReportModel> reportModelList) {
        this.reportModelList = reportModelList;
    }

    public List<CostModel> getCostList() {
        return costList;
    }

    public List<ReportModel> getReportModelList() {
        return reportModelList;
    }
}
