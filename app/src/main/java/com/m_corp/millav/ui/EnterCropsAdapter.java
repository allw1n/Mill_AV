package com.m_corp.millav.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Bill;
import com.m_corp.millav.room.Crop;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnterCropsAdapter extends RecyclerView.Adapter<EnterCropsAdapter.CropViewHolder> {

    private final Context context;
    private final List<CharSequence> cropsListForCAA = new ArrayList<>();
    private final List<CharSequence> cropPricesList = new ArrayList<>();
    private final List<CropsAddedPojo> cropsTotalWeighed = new ArrayList<>();
    private static final String NONE = "None";
    private OnRecyclerItemClickListener itemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener
                                                           itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnRecyclerItemClickListener {
        void onItemClickListener(View view, int position, CropsAddedPojo cropDetails);
    }

    public EnterCropsAdapter(AppCompatActivity activity) {
        this.context = activity;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CropViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.enter_crop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        holder.bindTo(position);
    }

    @Override
    public int getItemCount() {
        return cropsTotalWeighed.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCropsList (List<Crop> cropsListFromSource) {
        for (Crop crop: cropsListFromSource) {
            cropsListForCAA.add(crop.getCropName());
            cropPricesList.add(String.valueOf(crop.getPricePerKg()));
        }
        notifyDataSetChanged();
    }

    public void addNewToCropsTotalWeighed() {
        cropsTotalWeighed.add(new CropsAddedPojo());
    }

    public void removeFromCropsTotalWeighed(int position) {
        Log.d("Removed crop", cropsTotalWeighed.get(position).getCropName());
        cropsTotalWeighed.remove(position);
        notifyItemRemoved(position);
    }

    public boolean validate(float calculatedWeight) {
        Log.d("calculatedWeight", String.valueOf(calculatedWeight));

        float netCalculatedWeight = calculatedWeight;
        float bagWeight = 0.2f;

        int unfixedCropsBags = 0;
        float unfixedCropsTotalWeight, unfixedCropsAvgWeight;

        int fixedCropsBags = 0;
        float fixedCropsTotalWeight = 0;

        for (CropsAddedPojo crop: cropsTotalWeighed) {
            if (crop.getWeight() == 0)
                unfixedCropsBags += crop.getBags();
            else {
                fixedCropsBags += crop.getBags();
                fixedCropsTotalWeight += crop.getWeight();
            }
        }

        float totalWeightOfBags = (unfixedCropsBags + fixedCropsBags) * bagWeight;
        Log.d("totalWeightOfBags", String.valueOf(totalWeightOfBags));

        netCalculatedWeight -= totalWeightOfBags;
        Log.d("netCalculatedWeight", String.valueOf(netCalculatedWeight));
        Log.d("unfixedCropsBags", String.valueOf(unfixedCropsBags));
        Log.d("fixedCropsBags", String.valueOf(fixedCropsBags));
        Log.d("fixedCropsTotalWeight", String.valueOf(fixedCropsTotalWeight));

        unfixedCropsTotalWeight = netCalculatedWeight - fixedCropsTotalWeight;
        Log.d("unfixedCropsTotalWeight", String.valueOf(unfixedCropsTotalWeight));

        unfixedCropsAvgWeight = unfixedCropsTotalWeight / unfixedCropsBags;
        Log.d("unfixedCropsAvgWeight", String.valueOf(unfixedCropsAvgWeight));

        float enteredWeight = 0;

        DecimalFormat dF = new DecimalFormat("#.##");

        for (int i = 0; i < cropsTotalWeighed.size(); i++) {
            float previousWeight = cropsTotalWeighed.get(i).getWeight();
            if (previousWeight == 0) {
                int bags =  cropsTotalWeighed.get(i).getBags();
                float newWeight = Float.parseFloat(dF.format(bags * unfixedCropsAvgWeight));
                cropsTotalWeighed.get(i).setWeight(newWeight);
            }
            enteredWeight += cropsTotalWeighed.get(i).getWeight();
        }

        Log.d("enteredWeight", String.valueOf(enteredWeight));

        float weightDifference = netCalculatedWeight - enteredWeight;

        return -100 <= weightDifference && weightDifference <= 100;
    }

    public Bill setBillDetails() {
        Bill tempBill = new Bill();

        StringBuilder namesBuilder = new StringBuilder();
        StringBuilder bagsBuilder = new StringBuilder();
        StringBuilder pricesBuilder = new StringBuilder();
        StringBuilder weightBuilder = new StringBuilder();

        for (CropsAddedPojo crop: cropsTotalWeighed) {
            namesBuilder.append(crop.getCropName()).append(",");
            bagsBuilder.append(crop.getBags()).append(",");

            int index = cropsListForCAA.indexOf(crop.getCropName());
            pricesBuilder.append(cropPricesList.get(index)).append(",");

            weightBuilder.append(crop.getWeight()).append(",");
        }
        namesBuilder.deleteCharAt(namesBuilder.length() - 1);
        bagsBuilder.deleteCharAt(bagsBuilder.length() - 1);
        pricesBuilder.deleteCharAt(pricesBuilder.length() - 1);
        weightBuilder.deleteCharAt(weightBuilder.length() - 1);

        tempBill.setCropNames(namesBuilder.toString());
        tempBill.setCropBags(bagsBuilder.toString());
        tempBill.setCropPrices(pricesBuilder.toString());
        tempBill.setTotalWeights(weightBuilder.toString());

        return tempBill;
    }

    class CropViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, AdapterView.OnItemClickListener{

        private final TextInputLayout layoutSelectCrop, layoutInputWeightOfBagAdded;
        private final TextInputEditText inputWeightOfBagAdded;
        private final MaterialAutoCompleteTextView selectCrop;
        private final MaterialTextView viewTotalBags;
        private final AppCompatImageButton minusBag, plusBag;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutSelectCrop = itemView.findViewById(R.id.layoutSelectCrop);
            selectCrop = itemView.findViewById(R.id.selectCrop);
            minusBag = itemView.findViewById(R.id.minusBag);
            viewTotalBags = itemView.findViewById(R.id.viewBagsAdded);
            plusBag = itemView.findViewById(R.id.plusBag);
            layoutInputWeightOfBagAdded = itemView.findViewById(R.id.layoutInputWeightOfBagAdded);
            inputWeightOfBagAdded = itemView.findViewById(R.id.inputWeightOfBagAdded);

            selectCrop.setOnItemClickListener(this);
            minusBag.setOnClickListener(this);
            plusBag.setOnClickListener(this);
            inputWeightOfBagAdded.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (layoutInputWeightOfBagAdded.isErrorEnabled())
                        layoutInputWeightOfBagAdded.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        float changedWeight = Float.parseFloat(s.toString());
                        cropsTotalWeighed.get(getAdapterPosition())
                                .setWeight(changedWeight);
                    } else {
                        cropsTotalWeighed.get(getAdapterPosition())
                                .setWeight(0);
                    }
                    Log.d("changed weight", String.valueOf(
                            cropsTotalWeighed.get(getAdapterPosition()).getWeight()));
                }
            });
        }

        void bindTo(int position) {

            CropsAddedPojo tempCropAdded = cropsTotalWeighed.get(position);

            //populate selectCrops AutoComplete
            CustomArrayAdapter adapter = new CustomArrayAdapter(context, R.layout.crops_list_item,
                    new ArrayList<>(cropsListForCAA));
            selectCrop.setAdapter(adapter);

            selectCrop.setText(tempCropAdded.getCropName(), false);
            Log.d("selectCrop set to", tempCropAdded.getCropName());

            viewTotalBags.setText(String.valueOf(tempCropAdded.getBags()));
            if (tempCropAdded.getBags() >= 1) {
                minusBag.setClickable(true);
                minusBag.setImageResource(R.drawable.ic_minus_enabled);
                Log.d("minusBag", "enabled");
            } else {
                minusBag.setClickable(false);
                minusBag.setImageResource(R.drawable.ic_minus_disabled);
                Log.d("minusBag", "disabled");
            }

            inputWeightOfBagAdded.setText(String.valueOf(tempCropAdded.getWeight()));
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Log.d("Adapter position", String.valueOf(adapterPosition));

            if (cropsTotalWeighed.get(adapterPosition).getCropName().equals(NONE)) {
                layoutSelectCrop.setError("Required!");
                return;
            }

            int numberOfBags = cropsTotalWeighed.get(adapterPosition).getBags();
            float weightAltered = 0;

            String inputWeight = Objects.requireNonNull(inputWeightOfBagAdded.getText()).toString();
            if (!TextUtils.isEmpty(inputWeight)) {
                weightAltered = Float.parseFloat(inputWeight);
            }

            if (view.getId() == R.id.minusBag) {
                cropsTotalWeighed.get(adapterPosition).setBags(numberOfBags - 1);
            } else {
                cropsTotalWeighed.get(adapterPosition).setBags(numberOfBags + 1);
            }
            cropsTotalWeighed.get(adapterPosition).setWeight(weightAltered);

            itemClickListener.onItemClickListener(view, adapterPosition,
                    cropsTotalWeighed.get(adapterPosition));
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (layoutSelectCrop.isErrorEnabled()) layoutSelectCrop.setErrorEnabled(false);

            int adapterPosition = getAdapterPosition();

            String selectedCrop = selectCrop.getText().toString();
            cropsTotalWeighed.get(adapterPosition).setCropName(selectedCrop);

            Log.d("weighedCrop", cropsTotalWeighed.get(adapterPosition).getCropName());

            itemClickListener.onItemClickListener(view, adapterPosition,
                    cropsTotalWeighed.get(adapterPosition));
        }
    }
}
