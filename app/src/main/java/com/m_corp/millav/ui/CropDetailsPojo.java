package com.m_corp.millav.ui;

public class CropDetailsPojo {

    private String cropName;

    private int bags;

    private float weight;

    public CropDetailsPojo() {
        cropName = "none";
        bags = 0;
        weight = 0;
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
