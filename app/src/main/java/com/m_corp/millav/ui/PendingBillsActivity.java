package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.BILL_NUMBER;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_INFO_TAG;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.databinding.ActivityPendingBillsBinding;
import com.m_corp.millav.room.Bill;
import com.m_corp.millav.viewmodel.BillViewModel;

import java.util.List;

public class PendingBillsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPendingBillsBinding binding = ActivityPendingBillsBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialTextView viewNothingHeader = binding.viewNothingHeader;
        RecyclerView recyclerPendingBills = binding.recyclerPendingBills;

        BillViewModel billViewModel = new ViewModelProvider(this).get(BillViewModel.class);

        PendingBillsAdapter pendingBillsAdapter = new PendingBillsAdapter(this);
        recyclerPendingBills.setLayoutManager(new LinearLayoutManager(this));
        recyclerPendingBills.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerPendingBills.setAdapter(pendingBillsAdapter);

        billViewModel.getPendingBills().observe(this,
                new Observer<List<Bill>>() {
                    @Override
                    public void onChanged(List<Bill> bills) {
                        if (bills.size() > 0) {
                            viewNothingHeader.setVisibility(View.GONE);
                            pendingBillsAdapter.setBills(bills);
                        } else {
                            viewNothingHeader.setVisibility(View.VISIBLE);
                            recyclerPendingBills.setVisibility(View.GONE);
                        }
                    }
                });

        pendingBillsAdapter.setOnRecyclerItemClickListener(
                new PendingBillsAdapter.OnRecyclerItemClickListener() {
                    @Override
                    public void onItemClickListener(Bill bill) {
                        SharedPreferences sharedPrefs = getSharedPreferences(
                                SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putInt(BILL_NUMBER, bill.getBillNumber());
                        editor.apply();

                        CustomerInfoFragment fragment = CustomerInfoFragment.newInstance();
                        fragment.application = getApplication();
                        fragment.show(getSupportFragmentManager(), CUSTOMER_INFO_TAG);
                    }
                });
    }
}