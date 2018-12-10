package com.nurul.medicareplus.pojos;

import java.io.Serializable;

public class MedicineTimer implements Serializable {

    private String name;
    private String rowId;
    private int hour;
    private int minute;
    private boolean onOFF = true;

    public boolean isOnOFF() {
        return onOFF;
    }

    public void setOnOFF(boolean onOFF) {
        this.onOFF = onOFF;
    }

    public MedicineTimer() {
    }

    public String getName() {
        return name;
    }

    public String getRowId() {
        return rowId;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTimeString(){
        if (getHour() > 12){
            return getHour()-12 + ":" + getMinute() + " PM";
        }else {
            return getHour() + ":" + getMinute() + " AM";
        }
    }
}
