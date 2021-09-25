package com.example.nogor;

public class Blog {
    String sizeOfhouse, areaName, detailedareaName, rentCharge, describeHouse, extraContact, district, bergain, image, Key, User_phone;

    public Blog()
    {

    }

    public Blog(String sizeOfhouse, String areaName, String detailedareaName, String rentCharge, String describeHouse, String extraContact, String district, String bergain, String image, String Key, String User_phone) {
        this.sizeOfhouse = sizeOfhouse;
        this.areaName = areaName;
        this.detailedareaName=detailedareaName;
        this.rentCharge = rentCharge;
        this.describeHouse = describeHouse;
        this.extraContact = extraContact;
        this.district = district;
        this.bergain=bergain;
        this.image=image;
        this.Key=Key;
        this.User_phone=User_phone;
    }

    public String getDetailedareaName() {
        return detailedareaName;
    }

    public void setDetailedareaName(String detailedareaName) {
        this.detailedareaName = detailedareaName;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getUser_phone() {
        return User_phone;
    }

    public void setUser_phone(String user_phone) {
        User_phone = user_phone;
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
