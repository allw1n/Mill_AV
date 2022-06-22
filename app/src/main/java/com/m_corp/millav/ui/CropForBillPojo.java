package com.m_corp.millav.ui;

public class CropForBillPojo {

    private final int serial;

    private final String cropName;

    private final int bags;

    private final float totalWeight;

    private final float pricePerKg;

    private final float priceTotal;

    public CropForBillPojo(int serial, String cropName, int bags, float totalWeight,
                           float pricePerKg, float priceTotal) {
        this.serial = serial;
        this.cropName = cropName;
        this.bags = bags;
        this.totalWeight = totalWeight;
        this.pricePerKg = pricePerKg;
        this.priceTotal = priceTotal;
    }

    public int getSerial() {
        return serial;
    }

    public String getCropName() {
        return cropName;
    }

    public int getBags() {
        return bags;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public float getPricePerKg() {
        return pricePerKg;
    }

    public float getPriceTotal() {
        return priceTotal;
    }

}