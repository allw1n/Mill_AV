package com.m_corp.millav.ui;

import java.util.ArrayList;
import java.util.List;

public class CropListPojo {

    private String cropName;

    private int selectedCrop;
    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getSelectedCrop() {
        return selectedCrop;
    }

    public void setSelectedCrop(int selectedCrop) {
        this.selectedCrop = selectedCrop;
    }

    /*private List<CropListPojo> cropListPojoList;

    public List<CropListPojo> getCropListPojoList() {
        return cropListPojoList;
    }

    public void setCropListPojoList(List<CropListPojo> cropListPojoList) {
        this.cropListPojoList = cropListPojoList;
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
    }*/
}
