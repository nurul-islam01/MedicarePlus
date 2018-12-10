package com.nurul.medicareplus.REG_MODEL;

public class SignUp {
    public static String name;
    public static String email;
    public static String password;
    public static String userTYpe;
    public static String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public SignUp(String name, String email, String password, String userTYpe) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userTYpe = userTYpe;
    }

    public SignUp(String name, String email, String password, String userTYpe, String userID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userTYpe = userTYpe;
        this.userID = userID;
    }

    public SignUp() {
    }


    enum USER_TYPE {
        Docotr, Patient
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserTYpe() {
        return userTYpe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserTYpe(String userTYpe) {
        this.userTYpe = userTYpe;
    }
}
