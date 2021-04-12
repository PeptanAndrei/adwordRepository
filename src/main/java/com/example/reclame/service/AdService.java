package com.example.reclame.service;

import com.example.reclame.dto.CostDto;
import com.example.reclame.model.AdModel;
import com.example.reclame.model.CostModel;
import com.example.reclame.model.ReportModel;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.List;

@Service
public class AdService {
    private double cumulatedDailyCost;
    private double cumulatedMonthlyCost;
    private double maxDailyBudget;
    private double cumulatedMaxDailyBudget;
    private int nrOfDailyCostLeft = 10;

    private final List<CostModel> costList = new ArrayList<>();
    private final List<ReportModel> reportList = new ArrayList<>();
    private CostDto costDTO = new CostDto();


    private void generateDailyCost(Date date1, Date date2, double budget){

        if (date1.equals(date2)){
            //do nothing
            return;
        }
        //init daily and monthly parameters
        setGlobalValues(date1);

        //pause the campaign
        if(budget == 0){
            if(date1.equals(getStartOfDay(date1)) && date2.equals(getEndOfDay(date2))) {
                CostModel costModel = new CostModel();
                costModel.setCost(0);
                costModel.setData(getStartOfDay(date1));
                costList.add(costModel);

                ReportModel reportModel = new ReportModel();
                reportModel.setCosts(0);
                reportModel.setData(getStartOfDay(date1));
                reportModel.setBudget(0);
                reportList.add(reportModel);
            }
            return;
        }

        //set maxDailyBudget and cumulatedMaxDailyBudget
        if(budget>maxDailyBudget){
            cumulatedMaxDailyBudget=cumulatedMaxDailyBudget+(budget-maxDailyBudget);
            maxDailyBudget=budget;
        }


        int nrOfCosts = (int)getRandomNumber(1,nrOfDailyCostLeft);

        for (int i = 1; i <= nrOfCosts; i++) {
            if(cumulatedDailyCost<(budget*2) && (cumulatedMonthlyCost<cumulatedMaxDailyBudget)) {
                double cost = generateCost(budget, cumulatedDailyCost, cumulatedMonthlyCost, cumulatedMaxDailyBudget);
                Date costDate = getRandomDateBetween(date1, date2);
                if (cost != 0) {
                    CostModel costModel = new CostModel();
                    costModel.setCost(cost);
                    costModel.setData(costDate);
                    costList.add(costModel);
                }

                //update cumulatedDailyCost
                BigDecimal bdCcumulatedDailyCost = BigDecimal.valueOf(cumulatedDailyCost);
                BigDecimal bdDailySum;
                bdDailySum = bdCcumulatedDailyCost.add(BigDecimal.valueOf(cost));
                cumulatedDailyCost = bdDailySum.doubleValue();

                //update cumulatedMonthlyCost
                BigDecimal bdCumulatedMonthlyCost = BigDecimal.valueOf(cumulatedMonthlyCost);
                BigDecimal bdMonthlySum;
                bdMonthlySum = bdCumulatedMonthlyCost.add(BigDecimal.valueOf(cost));
                cumulatedMonthlyCost = bdMonthlySum.doubleValue();
            }
        }

        nrOfDailyCostLeft = nrOfDailyCostLeft - nrOfCosts;

        //generate daily report
        if(date2.equals(getEndOfDay(date2))){
            ReportModel reportModel = new ReportModel();
            reportModel.setCosts(cumulatedDailyCost);
            reportModel.setData(getStartOfDay(date1));
            reportModel.setBudget(maxDailyBudget);
            reportList.add(reportModel);

        }
    }

    private double generateCost(double budget, double cumulatedDailyCost, double cumulatedMonthlyCost, double cumulatedMaxDailyBudget){
        while(true) {
            double cost = Math.floor(getRandomNumber(0.01, budget) * 100) / 100;
            if( (((Math.floor(cumulatedDailyCost * 100) / 100 )+cost)<=(2*budget)) && (((Math.floor(cumulatedMonthlyCost * 100) / 100 )+cost)<=cumulatedMaxDailyBudget)){
                //stop while and return cost
                System.out.println("\n cost = "+cost);
                return cost;
            }

        }

    }

    private void setGlobalValues(Date date1) {
        if(date1.equals(getStartOfDay(date1))){
            maxDailyBudget = 0;
            cumulatedDailyCost = 0;
            nrOfDailyCostLeft = 10;
            if(isFirstDayOfTheMonth(date1)){
                cumulatedMaxDailyBudget=0;
                cumulatedMonthlyCost=0;
            }
        }
    }

    private double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    private static Date getRandomDateBetween(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    private static Date getNextXDays(Date date,int x){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, x);
        return c.getTime();
    }

    private static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Date getEndOfDay(Date date) {
        Calendar calendar = toCalendar(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }

    private static boolean isFirstDayOfTheMonth(Date dateToday){
        Calendar c = Calendar.getInstance();
        c.setTime(dateToday );
        if (c.get(Calendar.DAY_OF_MONTH) == 1) {
            if(dateToday.equals(getStartOfDay(dateToday))){
                return true;
            }
        }
        return false;
    }

    private static double getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    private static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }


    public CostDto processData(List<AdModel> adModelList) {
        costList.clear();
        reportList.clear();
        cumulatedDailyCost = 0;
        cumulatedMonthlyCost = 0;
        maxDailyBudget = 0;
        cumulatedMaxDailyBudget=0;
        int i,k;

        //generate all daily cost for each intervals
        for (i = 0; i < adModelList.size() - 1 ; i++) {
            AdModel element = adModelList.get(i);
            AdModel next_element = adModelList.get(i+1);

            if (isSameDay(element.getDate(),next_element.getDate())) {
                generateDailyCost(element.getDate(),next_element.getDate(),element.getBudget());
            }else{
                //generate cost to end of day
                generateDailyCost(element.getDate(),getEndOfDay(element.getDate()),element.getBudget());
                double diffInDays= getDateDiff(element.getDate(),next_element.getDate(),TimeUnit.DAYS );
                //generate daily cost for days in between inputs
                for(k=0;k<=diffInDays;k++){
                    Date startOfNextDay = DateUtils.truncate(getNextXDays(element.getDate(),k+1), Calendar.DAY_OF_MONTH);
                    Date endOfNextDay = getEndOfDay(startOfNextDay);
                    //check if valid interval
                    if (endOfNextDay.before(next_element.getDate())) {
                        generateDailyCost(startOfNextDay, endOfNextDay, element.getBudget());
                    }
                }
                generateDailyCost(DateUtils.truncate(next_element.getDate(), Calendar.DAY_OF_MONTH),next_element.getDate(),element.getBudget());
            }
        }
        generateDailyCost(adModelList.get(i).getDate(),getEndOfDay(adModelList.get(i).getDate()),adModelList.get(i).getBudget());

        costList.sort(Comparator.comparing(CostModel::getData));
        costDTO.setCostList(costList);
        costDTO.setReportModelList(reportList);

        return costDTO;
    }
}

