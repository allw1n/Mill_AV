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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Bill;
import com.m_corp.millav.room.Crop;
import com.m_corp.millav.viewmodel.CropViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CropsAdapter extends RecyclerView.Adapter<CropsAdapter.CropViewHolder> {

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

    public CropsAdapter(AppCompatActivity activity) {
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

    public float getEnteredWeight() {
        float totalWeightInKg = 0;
        for (CropsAddedPojo crop: cropsTotalWeighed) {
            totalWeightInKg +=  crop.getWeight() - (crop.getBags() * 0.2);
        }
        Log.d("totalWeightInKg", String.valueOf(totalWeightInKg));
        return totalWeightInKg;
    }

    public Bill setBillDetails() {
        Bill tempBill = new Bill();

        StringBuilder namesBuilder = new StringBuilder();
        StringBuilder bagsBuilder = new StringBuilder();
        StringBuilder pricesBuilder = new StringBuilder();
        StringBuilder weightBuilder = new StringBuilder();

        for (CropsAddedPojo crop: cropsTotalWeighed) {
            namesBuilder.append(crop.getCropName()).append(", ");
            bagsBuilder.append(crop.getBags()).append(", ");

            int index = cropsListForCAA.indexOf(crop.getCropName());
            pricesBuilder.append(cropPricesList.get(index)).append(", ");

            weightBuilder.append(crop.getWeight()).append(", ");
        }
        namesBuilder
                .deleteCharAt(namesBuilder.length() - 1).deleteCharAt(namesBuilder.length() - 1);
        bagsBuilder
                .deleteCharAt(bagsBuilder.length() - 1).deleteCharAt(bagsBuilder.length() - 1);
        pricesBuilder
                .deleteCharAt(pricesBuilder.length() - 1).deleteCharAt(pricesBuilder.length() - 1);
        weightBuilder
                .deleteCharAt(weightBuilder.length() - 1).deleteCharAt(weightBuilder.length() - 1);

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
        private final MaterialTextView viewTotalBags, viewTotalWeightOfBags;
        private final AppCompatImageButton minusBag, plusBag;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutSelectCrop = itemView.findViewById(R.id.layoutSelectCrop);
            selectCrop = itemView.findViewById(R.id.selectCrop);
            viewTotalWeightOfBags = itemView.findViewById(R.id.viewTotalWeightOfBags);
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
                public void afterTextChanged(Editable s) {}
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
            viewTotalWeightOfBags.setText(String.valueOf(tempCropAdded.getWeight()));

            inputWeightOfBagAdded.setText(String.valueOf(tempCropAdded.getWeightAltered()));
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
            float weightAltered;

            String inputWeight = Objects.requireNonNull(inputWeightOfBagAdded.getText()).toString();
            if (TextUtils.isEmpty(inputWeight)) {
                layoutInputWeightOfBagAdded.setError("Set!");
                return;
            } else {
                weightAltered = Float.parseFloat(inputWeight);
                if (weightAltered == 0) {
                    layoutInputWeightOfBagAdded.setError("Set!");
                    return;
                }
            }

            if (view.getId() == R.id.minusBag) {
                if (numberOfBags >= 1) {
                    cropsTotalWeighed.get(adapterPosition).setBags(numberOfBags - 1);
                    float totalWeight = cropsTotalWeighed.get(adapterPosition).getWeight();
                    cropsTotalWeighed.get(adapterPosition).setWeight(totalWeight - weightAltered);
                    cropsTotalWeighed.get(adapterPosition).setWeightAltered(weightAltered);
                }
            } else {
                cropsTotalWeighed.get(adapterPosition).setBags(numberOfBags + 1);
                float totalWeight = cropsTotalWeighed.get(adapterPosition).getWeight();
                cropsTotalWeighed.get(adapterPosition).setWeight(totalWeight + weightAltered);
                cropsTotalWeighed.get(adapterPosition).setWeightAltered(weightAltered);
            }

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
