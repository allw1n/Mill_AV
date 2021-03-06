package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.CHANGE_PRICE_TAG;

import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeBillAdapter extends RecyclerView.Adapter<MakeBillAdapter.MakeBillViewHolder> {

    private final AppCompatActivity activity;
    private List<String> cropNames;
    private List<String> cropBags;
    private List<String> cropPrices;
    private List<String> cropWeights;
    private List<String> cropTotalAmounts = new ArrayList<>();
    private float cumulativeAmount = 0;
    private int totalBags = 0;
    private int laborCostPerBag;

    private final DecimalFormat dF = new DecimalFormat("#.##");

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

        cropNames = new ArrayList<>(Arrays.asList(bill.getCropNames().split(",")));
        cropBags = new ArrayList<>(Arrays.asList(bill.getCropBags().split(",")));
        cropPrices = new ArrayList<>(Arrays.asList(bill.getCropPrices().split(",")));
        cropWeights = new ArrayList<>(Arrays.asList(bill.getTotalWeights().split(",")));

        laborCostPerBag = 3;

        for (int i = 0; i < cropNames.size(); i++) {
            float unformattedAmount =
                    Float.parseFloat(cropWeights.get(i)) * Float.parseFloat(cropPrices.get(i));
            Log.d("unformattedAmount", String.valueOf(unformattedAmount));

            float amount = Float.parseFloat(dF.format(unformattedAmount));
            Log.d("formattedAmount", String.valueOf(amount));

            cumulativeAmount += amount;
            cropTotalAmounts.add(i, String.valueOf(amount));

            totalBags += Integer.parseInt(cropBags.get(i));
            Log.d("totalBags", String.valueOf(totalBags));
        }

        Log.d("cumulativeAmount before", String.valueOf(cumulativeAmount));
        cumulativeAmount -= totalBags * laborCostPerBag;
        Log.d("cumulativeAmount after", String.valueOf(cumulativeAmount));
    }

    float getCumulativeAmount() {
        return cumulativeAmount;
    }

    void calculateNewCumulativeAmount() {
        cumulativeAmount = 0;
        for (String amount: cropTotalAmounts) {
            cumulativeAmount += Float.parseFloat(amount);
        }
        cumulativeAmount -= totalBags * laborCostPerBag;
    }

    class MakeBillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

            viewPricePerKg.setOnClickListener(this);
        }

        void bindTo(int position) {
            viewSerial.setText(String.valueOf(position + 1));
            viewCrop.setText(cropNames.get(position));
            viewCropBags.setText(cropBags.get(position));
            viewPricePerKg.setText(cropPrices.get(position));
            viewWeight.setText(cropWeights.get(position));
            viewTotalAmount.setText(cropTotalAmounts.get(position));
        }

        @Override
        public void onClick(View view) {
            ChangePriceFragment fragment = ChangePriceFragment.newInstance();
            fragment.show(activity.getSupportFragmentManager(), CHANGE_PRICE_TAG);

            fragment.setOnButtonSetClickListener(new ChangePriceFragment.OnButtonSetClickListener() {
                @Override
                public void onButtonSetClickListener(String newPrice) {
                    int adapterPosition = getAdapterPosition();

                    cropPrices.remove(adapterPosition);
                    cropPrices.add(adapterPosition, newPrice);

                    cropTotalAmounts.remove(adapterPosition);
                    float newTotalAmount =
                            Float.parseFloat(cropWeights.get(adapterPosition)) *
                                    Float.parseFloat(cropPrices.get(adapterPosition));
                    cropTotalAmounts.add(adapterPosition, dF.format(newTotalAmount));

                    notifyItemChanged(adapterPosition);

                    calculateNewCumulativeAmount();

                    MaterialTextView viewCumulativeAmount =
                            activity.findViewById(R.id.viewCumulativeAmount);
                    viewCumulativeAmount.setText(String.valueOf(cumulativeAmount));
                }
            });
        }
    }
}
