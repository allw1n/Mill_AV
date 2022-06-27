package com.m_corp.millav.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Bill;
import com.m_corp.millav.viewmodel.BillViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeBillAdapter extends RecyclerView.Adapter<MakeBillAdapter.MakeBillViewHolder> {

    private final AppCompatActivity activity;
    private List<String> cropNames = new ArrayList<>();
    private List<String> cropBags = new ArrayList<>();
    private List<String> cropPrices = new ArrayList<>();
    private List<String> cropWeights = new ArrayList<>();
    private float totalWeight;

    public MakeBillAdapter(AppCompatActivity activity, int billNumber) {
        this.activity = activity;
        setBillDetails(billNumber);
    }

    @NonNull
    @Override
    public MakeBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MakeBillViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.bill_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MakeBillViewHolder holder, int position) {
        holder.bindTo(position);
    }

    @Override
    public int getItemCount() {
        return cropNames.size();
    }

    private void setBillDetails(int billNumber) {
        BillViewModel billViewModel = new ViewModelProvider(activity).get(BillViewModel.class);
        Bill bill = billViewModel.getBillDetails(billNumber);

        cropNames = Arrays.asList(bill.getCropNames().split(","));
        cropBags = Arrays.asList(bill.getCropBags().split(","));
        cropPrices = Arrays.asList(bill.getCropPrices().split(","));
        cropWeights = Arrays.asList(bill.getTotalWeights().split(","));

        for (String weight: cropWeights) {
            totalWeight += Float.parseFloat(weight);
        }
    }

    float getCumulativeAmount() {
        float cumulativeAmount = 1000000;
        return cumulativeAmount;
    }

    class MakeBillViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView viewSerial, viewCrop, viewCropBags, viewWeight,
                viewPricePerKg, viewTotalAmount;

        public MakeBillViewHolder(@NonNull View itemView) {
            super(itemView);

            viewSerial = itemView.findViewById(R.id.viewSerial);
            viewCrop = itemView.findViewById(R.id.viewCrop);
            viewCropBags = itemView.findViewById(R.id.viewCropBags);
            viewWeight = itemView.findViewById(R.id.viewWeight);
            viewPricePerKg = itemView.findViewById(R.id.viewPricePerKg);
            viewTotalAmount = itemView.findViewById(R.id.viewTotalAmount);
        }

        void bindTo(int position) {
            viewSerial.setText(String.valueOf(position + 1));
            viewCrop.setText(cropNames.get(position));
            viewCropBags.setText(cropBags.get(position));
            viewWeight.setText(cropWeights.get(position));
        }
    }
}
