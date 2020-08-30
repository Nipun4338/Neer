package com.example.nogor;

public class Blog1 {
    String sizeOfhouse, areaName, rentCharge, describeHouse, extraContact, district, bergain, image;

    public Blog1()
    {

    }

    public Blog1(String sizeOfhouse, String areaName, String rentCharge, String describeHouse, String extraContact, String district, String bergain, String image) {
        this.sizeOfhouse = sizeOfhouse;
        this.areaName = areaName;
        this.rentCharge = rentCharge;
        this.describeHouse = describeHouse;
        this.extraContact = extraContact;
        this.district = district;
        this.bergain=bergain;
        this.image=image;
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
