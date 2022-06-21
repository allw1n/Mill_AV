package com.m_corp.millav.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bill_details")
public class Bill {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_number")
    private final int billNumber;

    public Bill(int billNumber) {
        this.billNumber = billNumber;
    }

    public int getBillNumber() {
        return billNumber;
    }
}
