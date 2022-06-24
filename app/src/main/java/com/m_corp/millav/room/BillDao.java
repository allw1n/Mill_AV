package com.m_corp.millav.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BillDao {

    @Insert
    public void insertBill(Bill bill);

    @Query("SELECT * FROM bill_details WHERE pending = :pending")
    public LiveData<List<Bill>> getPendingBills(boolean pending);

    @Query("SELECT * FROM bill_details WHERE bill_number =:billNumber")
    public Bill getBillDetails(int billNumber);

    @Query("UPDATE bill_details SET pending = :pending WHERE bill_number =:billNumber")
    public void updatePendingBill(int billNumber, boolean pending);
}
