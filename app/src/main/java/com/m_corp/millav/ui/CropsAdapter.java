package com.m_corp.millav.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Crop;

import java.util.ArrayList;
import java.util.List;

public class CropsAdapter extends RecyclerView.Adapter<CropsAdapter.CropViewHolder> {

    private final Context context;
    private List<Crop> cropsListFromSource = new ArrayList<>();
    private List<CropDetailsPojo> cropsTotalWeighed;
    private onRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener
                                                           onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClickListener(View view, int position, CropDetailsPojo cropDetails);
    }

    public CropsAdapter(Context context, List<CropDetailsPojo> cropsTotalWeighed) {
        this.context = context;
        this.cropsTotalWeighed = cropsTotalWeighed;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CropViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.crop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        CropDetailsPojo cropDetails =cropsTotalWeighed.get(position);
        holder.bindTo(cropDetails);
    }

    @Override
    public int getItemCount() {
        return cropsTotalWeighed.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCropsList (List<Crop> cropsListFromSource) {
        this.cropsListFromSource = cropsListFromSource;
        notifyDataSetChanged();
    }

    public void addNewCropTotal(CropDetailsPojo cropDetails) {
        cropsTotalWeighed.add(cropDetails);
    }

    class CropViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemClickListener{

        private final MaterialAutoCompleteTextView selectCrop;
        private final MaterialTextView viewTotalBags, viewTotalWeight;
        private final AppCompatImageButton minusBag, plusBag;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
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
            List<CharSequence> crops = new ArrayList<>();
            for (Crop crop: cropsListFromSource) {
                crops.add(crop.getCropName());
            }
            CustomArrayAdapter adapter = new CustomArrayAdapter(context, R.layout.crops_list_item, crops);
            selectCrop.setAdapter(adapter);


            int bags = cropDetails.getBags();
            float totalWeight = bags * 1;
            viewTotalBags.setText(String.valueOf(bags));
            viewTotalWeight.setText(String.valueOf(totalWeight));
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int adapterPosition = getAdapterPosition();
            CropDetailsPojo cropChangedDetails;
            cropChangedDetails = cropsTotalWeighed.get(adapterPosition);

            if (id == R.id.minusBag) {
                int numberOfBags = cropsTotalWeighed.get(adapterPosition).getBags();
                if (numberOfBags >= 1) {
                    numberOfBags--;
                    cropChangedDetails.setBags(numberOfBags);
                    if (numberOfBags == 0) {
                        minusBag.setClickable(false);
                        minusBag.setImageResource(R.drawable.ic_minus_disabled);
                        minusBag.setBackgroundResource(R.drawable.button_disabled_bg);
                    }
                }
            }
            if (id == R.id.plusBag) {
                int numberOfBags = cropsTotalWeighed.get(adapterPosition).getBags();
                cropChangedDetails.setBags(numberOfBags);
                if (numberOfBags == 1) {
                    minusBag.setClickable(true);
                    minusBag.setImageResource(R.drawable.ic_minus_enabled);
                    minusBag.setBackgroundResource(R.drawable.button_enabled_bg);
                }
            }
            onRecyclerViewItemClickListener.onItemClickListener(view, adapterPosition,
                    cropsTotalWeighed.get(adapterPosition));
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int adapterPosition = getAdapterPosition();
            cropsTotalWeighed.get(adapterPosition)
                    .setCropName(selectCrop.getText().toString());
            onRecyclerViewItemClickListener.onItemClickListener(view, adapterPosition,
                    cropsTotalWeighed.get(adapterPosition));
        }
    }
}
