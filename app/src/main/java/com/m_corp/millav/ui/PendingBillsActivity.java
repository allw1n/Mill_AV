package com.m_corp.millav.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.databinding.ActivityPendingBillsBinding;
import com.m_corp.millav.room.Bill;
import com.m_corp.millav.viewmodel.BillViewModel;

import java.util.List;

public class PendingBillsActivity extends AppCompatActivity {

    private MaterialTextView viewNothingHeader;

    private RecyclerView recyclerPendingBills;

    private List<CropForBillPojo> cropsForBill;
    private List<Bill> bills;
    private BillAdapter billAdapter;

    private BillViewModel billViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPendingBillsBinding binding = ActivityPendingBillsBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewNothingHeader = binding.viewNothingHeader;
        recyclerPendingBills = binding.recyclerPendingBills;

        billViewModel = new ViewModelProvider(this).get(BillViewModel.class);
        bills = billViewModel.getPendingBills();
        if (bills.size() == 0)
            recyclerPendingBills.setVisibility(View.GONE);
        else {
            viewNothingHeader.setVisibility(View.GONE);
            billAdapter = new BillAdapter(this, cropsForBill);
            recyclerPendingBills.setLayoutManager(new LinearLayoutManager(this));
            recyclerPendingBills.setAdapter(billAdapter);
        }
    }
}