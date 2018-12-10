package com.nurul.medicareplus.pojos;

import java.io.Serializable;

public class Medicine implements Serializable {

    private String name;
    private String beforeORafter;
    private int perDos;
    private int dailyDos;
    private int totalPicsMedicine;
    private int day;
    private int month;
    private int year;
    private String mImageUri;

    public int getPerDos() {
        return perDos;
    }

    public void setPerDos(int perDos) {
        this.perDos = perDos;
    }

    public int getDailyDos() {
        return dailyDos;
    }

    public void setDailyDos(int dailyDos) {
        this.dailyDos = dailyDos;
    }

    public int getTotalPicsMedicine() {
        return totalPicsMedicine;
    }

    public void setTotalPicsMedicine(int totalPicsMedicine) {
        this.totalPicsMedicine = totalPicsMedicine;
    }

    public String getDate(){
        return getDay() +"/"+getMonth()+"/"+getYear();
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    private String rowId;

    public String getName() {
        return name;
    }

    public String getBeforeORafter() {
        return beforeORafter;
    }


    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getRowId() {
        return rowId;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setBeforeORafter(String beforeORafter) {
        this.beforeORafter = beforeORafter;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }
}
