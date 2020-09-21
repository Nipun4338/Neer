package com.example.nogor;

import android.widget.EditText;

public class userHelperClass {
    String name, password, email, address, phone, dp;

    public userHelperClass()
    {

    }

    public userHelperClass(String name, String password, String email, String address, String phone, String dp) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.dp=dp;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.address = phone;
    }

}
