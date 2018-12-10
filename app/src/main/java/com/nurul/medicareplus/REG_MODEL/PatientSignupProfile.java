package com.nurul.medicareplus.REG_MODEL;

import java.io.Serializable;

public class PatientSignupProfile implements Serializable {

    private String name;
    private String gender;
    private String age;
    private String phone;
    private String weight;
    private String address;
    private String type;
    private String userId;
    private boolean isVerfied;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVerfied() {
        return isVerfied;
    }

    public void setVerfied(boolean verfied) {
        isVerfied = verfied;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {

        return userId;
    }

    public PatientSignupProfile() {
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setAge(String age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {

        return name;
    }


    public String getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getWeight() {
        return weight;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }
}
