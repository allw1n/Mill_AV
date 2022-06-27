package com.m_corp.millav.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.material.internal.ParcelableSparseArray;

@Entity(tableName = "bill_details")
public class Bill {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_number")
    public int billNumber;

    @ColumnInfo(name = "crop_names")
    private String cropNames;

    @ColumnInfo(name = "crop_bags")
    private String cropBags;

    @ColumnInfo(name = "crop_prices")
    private String cropPrices;

    @ColumnInfo(name = "total_weights")
    private String totalWeights;

    private boolean pending = true;

    public int getBillNumber() {
        return billNumber;
    }

    public String getCropNames() {
        return cropNames;
    }

    public void setCropNames(String cropNames) {
        this.cropNames = cropNames;
    }

    public String getCropBags() {
        return cropBags;
    }

    public void setCropBags(String cropBags) {
        this.cropBags = cropBags;
    }

    public String getCropPrices() {
        return cropPrices;
    }

    public void setCropPrices(String cropPrices) {
        this.cropPrices = cropPrices;
    }

    public String getTotalWeights() {
        return totalWeights;
    }

    public void setTotalWeights(String totalWeights) {
        this.totalWeights = totalWeights;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
