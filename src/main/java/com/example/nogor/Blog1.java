package com.example.nogor;

public class Blog1 {
    String sizeOfhouse, areaName, areaName1, rentCharge, describeHouse, extraContact, district, bergain, image, key, User_phone;

    public Blog1()
    {

    }

    public Blog1(String sizeOfhouse, String areaName, String areaName1, String rentCharge, String describeHouse, String extraContact, String district, String bergain, String image, String key, String User_phone) {
        this.sizeOfhouse = sizeOfhouse;
        this.areaName = areaName;
        this.areaName1 = areaName1;
        this.rentCharge = rentCharge;
        this.describeHouse = describeHouse;
        this.extraContact = extraContact;
        this.district = district;
        this.bergain=bergain;
        this.image=image;
        this.key=key;
        this.User_phone=User_phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser_phone() {
        return User_phone;
    }

    public void setUser_phone(String user_phone) {
        User_phone = user_phone;
    }

    public String getAreaName1() {
        return areaName1;
    }

    public void setAreaName1(String areaName1) {
        this.areaName1 = areaName1;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
