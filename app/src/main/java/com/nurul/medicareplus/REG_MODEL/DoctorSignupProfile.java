package com.nurul.medicareplus.REG_MODEL;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

public class DoctorSignupProfile implements Serializable {

    private String name;
    private String degination;
    private String sepecility;
    private String consultantType;
    private String chember;
    private String phone;
    private String description;
    private String gender;
    private String userId;
    private String pic_uri;
    private boolean emailVerification = false;

    private final String userType = "Doctor";


    public DoctorSignupProfile() {}




    public void setPic_uri(String pic_uri) {
        this.pic_uri = pic_uri;
    }

    public String getPic_uri() {
        return pic_uri;

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDegination(String degination) {
        this.degination = degination;
    }

    public void setSepecility(String sepecility) {
        this.sepecility = sepecility;
    }

    public void setConsultantType(String consultantType) {
        this.consultantType = consultantType;
    }

    public void setChember(String chember) {
        this.chember = chember;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmailVerification(boolean emailVerification) {
        this.emailVerification = emailVerification;
    }


    public String getName() {
        return name;
    }

    public String getDegination() {
        return degination;
    }

    public String getSepecility() {
        return sepecility;
    }

    public String getConsultantType() {
        return consultantType;
    }

    public String getChember() {
        return chember;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public String getUserType() {
        return userType;
    }
    public boolean isEmailVerification() {
        return emailVerification;
    }
}
