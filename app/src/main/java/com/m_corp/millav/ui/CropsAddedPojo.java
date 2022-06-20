package com.m_corp.millav.ui;

import com.m_corp.millav.room.Crop;

public class CropsAddedPojo {

    private String cropName;

    private int bags;

    private float weight;

    public CropsAddedPojo() {
        this.cropName = "None";
        this.bags = 0;
        this.weight = 0;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getBags() {
        return bags;
    }

    public void setBags(int bags) {
        this.bags = bags;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
