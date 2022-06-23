package com.m_corp.millav.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "crops_list")
public class Crop {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "crop_name")
    private final String cropName;

    @ColumnInfo(name = "price_per_kg")
    private final float pricePerKg;

    public Crop(@NonNull String cropName, float pricePerKg) {
        this.cropName = cropName;
        this.pricePerKg = pricePerKg;
    }

    @NonNull
    public String getCropName() {
        return cropName;
    }

    public float getPricePerKg() {
        return pricePerKg;
    }
}
