package com.nurul.medicareplus.pojos;

public class AppoinmentedProfile {
    private String appoinmentId;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String rowId;
    private int appoinmentSerial;

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getRowId() {

        return rowId;
    }

    public String getTime(){
        if (getHour()>11){
            return getHour()-12+":"+getMinute()+" PM";
        }else {
            return getHour()+":"+getMinute()+" AM";
        }
    }

    public String getDate(){
        return getDay()+"/"+getMonth()+"/"+getYear();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setAppoinmentId(String appoinmentId) {
        this.appoinmentId = appoinmentId;
    }

    public void setAppoinmentSerial(int appoinmentSerial) {
        this.appoinmentSerial = appoinmentSerial;
    }

    public String getAppoinmentId() {

        return appoinmentId;
    }

    public int getAppoinmentSerial() {
        return appoinmentSerial;
    }
}
