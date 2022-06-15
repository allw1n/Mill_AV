package com.m_corp.millav.ui;

import java.util.ArrayList;
import java.util.List;

public class CropsAddedPojo {

    private List<CropListPojo> cropListPojoList;

    private int bags;

    private float weight;

    public CropsAddedPojo() {
        this.cropListPojoList = setCropList();
        bags = 0;
        weight = 0;
    }

    private List<CropListPojo> setCropList() {
        List<CropListPojo> tempList= new ArrayList<>();

        CropListPojo cropListPojo = new CropListPojo();
        cropListPojo.setCropName("Rice");
        cropListPojo.setSelectedCrop(0);
        tempList.add(cropListPojo);

        CropListPojo cropListPojo1 = new CropListPojo();
        cropListPojo1.setCropName("Wheat");
        cropListPojo1.setSelectedCrop(0);
        tempList.add(cropListPojo1);

        CropListPojo cropListPojo2= new CropListPojo();
        cropListPojo2.setCropName("Arhar Dal");
        cropListPojo2.setSelectedCrop(0);
        tempList.add(cropListPojo2);

        return tempList;
    }

    public List<CropListPojo> getCropListPojoList() {
        return cropListPojoList;
    }

    public void setCropListPojoList(List<CropListPojo> cropListPojoList) {
        this.cropListPojoList = cropListPojoList;
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
