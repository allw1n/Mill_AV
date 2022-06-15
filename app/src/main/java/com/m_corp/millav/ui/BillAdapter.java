package com.m_corp.millav.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private Context context;
    private List<CropForBillPojo> cropsForBill;

    public BillAdapter(Context context, List<CropForBillPojo> cropsForBill) {
        this.context = context;
        this.cropsForBill = cropsForBill;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillViewHolder(LayoutInflater.from(context).inflate(R.layout.bill_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        CropForBillPojo tempCrop = cropsForBill.get(position);
        holder.bindTo(tempCrop);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BillViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView viewSerial, viewCrop, viewCropBags, viewWeight, viewPricePerKg
                , viewTotalAmount;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);

            this.viewSerial = itemView.findViewById(R.id.viewSerial);
            this.viewCrop = itemView.findViewById(R.id.viewCrop);
            this.viewCropBags = itemView.findViewById(R.id.viewCropBags);
            this.viewWeight = itemView.findViewById(R.id.viewWeight);
            this.viewPricePerKg = itemView.findViewById(R.id.viewPricePerKg);
            this.viewTotalAmount = itemView.findViewById(R.id.viewTotalAmount);
        }

        void bindTo(CropForBillPojo cropForBill) {

            viewSerial.setText(cropForBill.getSerial());
            viewCrop.setText(cropForBill.getCropName());
            viewCropBags.setText(cropForBill.getBags());
            viewWeight.setText(String.valueOf(cropForBill.getWeight()));
            viewPricePerKg.setText(String.valueOf(cropForBill.getPricePerKg()));
            viewTotalAmount.setText(String.valueOf(cropForBill.getPriceTotal()));
        }
    }
}
