package com.m_corp.millav.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.m_corp.millav.repository.BillRepository;
import com.m_corp.millav.room.Bill;

import java.util.List;

public class BillViewModel extends AndroidViewModel {

    private final BillRepository billRepository;

    private final LiveData<List<Bill>> bills;

    public BillViewModel(@NonNull Application application) {
        super(application);
        billRepository = new BillRepository(application);
        bills = billRepository.getPendingBills();
    }

    public LiveData<List<Bill>> getPendingBills() {
        return bills;
    }

    public void insertBill(Bill bill) {
        billRepository.insertBill(bill);
    }

    public Bill getBillDetails(int billNumber) {
        return billRepository.getBillDetails(billNumber);
    }

    public void updateBill(int billNumber) {
        billRepository.updateBill(billNumber);
    }
}
