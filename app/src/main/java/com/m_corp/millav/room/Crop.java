package com.m_corp.millav.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "crop_details")
public class Crop {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "crop_name")
    private final String cropName;

    private int bags;

    private float weight;

    public Crop(@NonNull String cropName, int bags, float weight) {
        this.cropName = cropName;
        this.bags = bags;
        this.weight = weight;
    }

    @NonNull
    public String getCropName() {
        return cropName;
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
