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

    @ColumnInfo(name = "fixed_weight")
    private final boolean fixedWeight;

    public Crop(@NonNull String cropName, float pricePerKg, boolean fixedWeight) {
        this.cropName = cropName;
        this.pricePerKg = pricePerKg;
        this.fixedWeight = fixedWeight;
    }

    @NonNull
    public String getCropName() {
        return cropName;
    }

    public float getPricePerKg() {
        return pricePerKg;
    }

    public boolean isFixedWeight() {
        return fixedWeight;
    }
}
