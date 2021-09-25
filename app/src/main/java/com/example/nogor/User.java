package com.example.nogor;

public class User {
    String sizeOfhouse, areaName, areaName1, detailedareaName, rentCharge, describeHouse, extraContact, district, district1, bergain, key, user_phone, user_name, user_dp, x;

    public User()
    {

    }

    public User(String areaName, String areaName1, String detailedareaName, String sizeOfhouse, String rentCharge, String describeHouse, String extraContact, String district, String district1, String bergain, String key, String user_phone, String user_name, String user_dp, String x) {
        this.sizeOfhouse = sizeOfhouse;
        this.areaName = areaName;
        this.areaName1 = areaName1;
        this.detailedareaName=detailedareaName;
        this.district1=district1;
        this.rentCharge = rentCharge;
        this.describeHouse = describeHouse;
        this.extraContact = extraContact;
        this.district = district;
        this.bergain=bergain;
        this.key=key;
        this.user_phone=user_phone;
        this.user_name=user_name;
        this.user_dp=user_dp;
        this.x=x;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getDetailedareaName() {
        return detailedareaName;
    }

    public void setDetailedareaName(String detailedareaName) {
        this.detailedareaName = detailedareaName;
    }

    public String getDistrict1() {
        return district1;
    }

    public void setDistrict1(String district1) {
        this.district1 = district1;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_dp() {
        return user_dp;
    }

    public void setUser_dp(String user_dp) {
        this.user_dp = user_dp;
    }

    public String getAreaName1() {
        return areaName1;
    }

    public void setAreaName1(String areaName1) {
        this.areaName1 = areaName1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSizeOfhouse() {
        return sizeOfhouse;
    }

    public void setSizeOfhouse(String sizeOfhouse) {
        this.sizeOfhouse = sizeOfhouse;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRentCharge() {
        return rentCharge;
    }

    public void setRentCharge(String rentCharge) {
        this.rentCharge = rentCharge;
    }

    public String getDescribeHouse() {
        return describeHouse;
    }

    public void setDescribeHouse(String describeHouse) {
        this.describeHouse = describeHouse;
    }

    public String getExtraContact() {
        return extraContact;
    }

    public void setExtraContact(String extraContact) {
        this.extraContact = extraContact;
    }

    public String getBergain() {
        return bergain;
    }

    public void setBergain(String bergain) {
        this.bergain = bergain;
    }
}
