package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.BILL_NUMBER;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_ADDRESS;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_NAME;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;
import static com.m_corp.millav.utils.MillAVUtils.ZERO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.databinding.ActivityMakeBillBinding;
import com.m_corp.millav.viewmodel.EmployerViewModel;

public class MakeBillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMakeBillBinding binding = ActivityMakeBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialTextView viewShopName, viewCustomerName, viewCustomerMobile, viewCustomerAddress,
                viewBillDate, viewBillNumber;
        RecyclerView recyclerBill;

        viewShopName = binding.viewShopName;
        viewCustomerName = binding.viewCustomerName;
        viewCustomerMobile = binding.viewCustomerMobile;
        viewCustomerAddress = binding.viewCustomerAddress;
        viewBillDate = binding.viewBillDate;
        viewBillNumber = binding.viewBillNumber;
        recyclerBill = binding.recyclerBill;

        EmployerViewModel employerViewModel = new ViewModelProvider(this)
                .get(EmployerViewModel.class);

        viewCustomerName.setText(getIntent().getStringExtra(CUSTOMER_NAME));
        viewCustomerMobile.setText(getIntent().getStringExtra(CUSTOMER_MOBILE));
        viewCustomerAddress.setText(getIntent().getStringExtra(CUSTOMER_ADDRESS));
        //viewBillDate.setText(getDate());

        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        viewBillNumber.setText(String.valueOf(sharedPrefs.getInt(BILL_NUMBER, ZERO)));

        recyclerBill.setLayoutManager(new LinearLayoutManager(this));
    }

    /*private String getDate() {
    }*/
}