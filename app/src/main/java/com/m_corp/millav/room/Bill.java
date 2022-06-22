package com.m_corp.millav.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bill_details")
public class Bill {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_number")
    public int billNumber;

    @ColumnInfo(name = "crop_names")
    private String cropNames;

    @ColumnInfo(name = "crop_bags")
    private String cropBags;

    @ColumnInfo(name = "price_per_kg")
    private String pricePerKg;

    private boolean pending;

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
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

    public String getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(String pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
