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

    public Crop(@NonNull String cropName) {
        this.cropName = cropName;
    }

    @NonNull
    public String getCropName() {
        return cropName;
    }
}
