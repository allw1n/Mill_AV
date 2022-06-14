package com.m_corp.millav.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Crop;

import java.util.ArrayList;
import java.util.List;

public class CropsAdapter extends RecyclerView.Adapter<CropsAdapter.CropViewHolder> {

    private final Context context;
    private final List<CharSequence> cropsListForCAA = new ArrayList<>();
    private final List<CropDetailsPojo> cropsTotalWeighed = new ArrayList<>();
    private static final String NONE = "none";
    private String weighedCrop = NONE;
    private onRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener
                                                           onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClickListener(View view, int position, CropDetailsPojo cropDetails);
    }

    public CropsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CropViewHolder(LayoutInflater.from(context).inflate(R.layout.crop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        CropDetailsPojo cropDetails = cropsTotalWeighed.get(position);
        holder.bindTo(cropDetails);
    }

    @Override
    public int getItemCount() {
        return cropsTotalWeighed.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCropsList (List<Crop> cropsListFromSource) {
        for (Crop crop: cropsListFromSource) {
            cropsListForCAA.add(crop.getCropName());
        }
        notifyDataSetChanged();
    }

    public void addNewToCropsTotalWeighed() {
        weighedCrop = NONE;
        Log.d("weighedCrop", weighedCrop);
        cropsTotalWeighed.add(new CropDetailsPojo());
    }

    class CropViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemClickListener{

        private final TextInputLayout layoutSelectCrop;
        private final MaterialAutoCompleteTextView selectCrop;
        private final MaterialTextView viewTotalBags, viewTotalWeight;
        private final AppCompatImageButton minusBag, plusBag;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutSelectCrop = itemView.findViewById(R.id.layoutSelectCrop);
            selectCrop = itemView.findViewById(R.id.selectCrop);
            viewTotalBags = itemView.findViewById(R.id.viewTotalBags);
            viewTotalWeight = itemView.findViewById(R.id.viewTotalWeight);
            minusBag = itemView.findViewById(R.id.minusBag);
            plusBag = itemView.findViewById(R.id.plusBag);

            selectCrop.setOnItemClickListener(this);
            minusBag.setOnClickListener(this);
            plusBag.setOnClickListener(this);
        }

        void bindTo(CropDetailsPojo cropDetails) {

            //populate selectCrops AutoComplete
            CustomArrayAdapter adapter = new CustomArrayAdapter(context, R.layout.crops_list_item, cropsListForCAA);
            selectCrop.setAdapter(adapter);

            if (!weighedCrop.equals(NONE)) {
                selectCrop.setText(weighedCrop, false);
                Log.d("selectCrop set to", weighedCrop);
            }

            int bags = cropDetails.getBags();
            float totalWeight = bags * 1;
            viewTotalBags.setText(String.valueOf(bags));
            if (bags > 1) {
                Log.d("minusBag", "already enabled");
            } else if (bags == 1){
                minusBag.setClickable(true);
                minusBag.setImageResource(R.drawable.ic_minus_enabled);
                minusBag.setBackgroundResource(R.drawable.button_enabled_bg);
                Log.d("minusBag", "enabled");
            } else {
                minusBag.setClickable(false);
                minusBag.setImageResource(R.drawable.ic_minus_disabled);
                minusBag.setBackgroundResource(R.drawable.button_disabled_bg);
                Log.d("minusBag", "disabled");
            }
            viewTotalWeight.setText(String.valueOf(totalWeight));
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Log.d("Adapter position", String.valueOf(adapterPosition));
            int numberOfBags = cropsTotalWeighed.get(adapterPosition).getBags();
            weighedCrop = cropsTotalWeighed.get(adapterPosition).getCropName();
            Log.d("weighedCrop", weighedCrop);

            if (view.getId() == R.id.minusBag) {
                if (numberOfBags >= 1) {
                    cropsTotalWeighed.get(adapterPosition).setBags(numberOfBags - 1);
                }
            } else {
                if (weighedCrop.equals(NONE)) {
                    layoutSelectCrop.setError("Required");
                    return;
                }
                cropsTotalWeighed.get(adapterPosition).setBags(numberOfBags + 1);
            }

            onRecyclerViewItemClickListener.onItemClickListener(view, adapterPosition,
                    cropsTotalWeighed.get(adapterPosition));

            notifyItemChanged(adapterPosition);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int adapterPosition = getAdapterPosition();
            cropsTotalWeighed.get(adapterPosition)
                    .setCropName(selectCrop.getText().toString());
            weighedCrop = cropsTotalWeighed.get(adapterPosition).getCropName();
            Log.d("weighedCrop", weighedCrop);

            onRecyclerViewItemClickListener.onItemClickListener(view, adapterPosition,
                    cropsTotalWeighed.get(adapterPosition));

            notifyItemChanged(adapterPosition);
        }
    }
}
