package com.m_corp.millav.repository;

import android.app.Application;

import com.m_corp.millav.room.Bill;
import com.m_corp.millav.room.BillDao;
import com.m_corp.millav.room.MillAVDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BillRepository {

    private final BillDao billDao;
    private List<Bill> bills;

    public BillRepository(Application application) {
        MillAVDatabase database = MillAVDatabase.getDatabase(application);
        billDao = database.billDao();
        setBills();
    }

    private void setBills() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<List<Bill>> callable = new Callable<List<Bill>>() {
            @Override
            public List<Bill> call() throws Exception {
                return billDao.getPendingBills(true);
            }
        };

        Future<List<Bill>> future = executor.submit(callable);

        try {
            bills = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertBill(Bill bill) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                billDao.insertBill(bill);
            }
        }).start();
    }

    public List<Bill> getPendingBills() {
        return bills;
    }

    public Bill getBillDetails(int billNumber) {
        Bill bill = new Bill();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Bill> callable = new Callable<Bill>() {
            @Override
            public Bill call() throws Exception {
                return billDao.getBillDetails(billNumber);
            }
        };

        Future<Bill> future = executor.submit(callable);

        try {
            bill = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return bill;
    }

    public void updateBill(int billNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                billDao.updatePendingBill(billNumber, false);
            }
        }).start();
    }
}
