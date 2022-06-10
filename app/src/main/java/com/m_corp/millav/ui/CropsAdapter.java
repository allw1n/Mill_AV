package com.m_corp.millav.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<Crop> cropsList;
    private List<CropDetailsPojo> cropsTotal;
    private onRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener
                                                           onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClickListener(View view, int position);
    }

    public CropsAdapter(Context context, List<CropDetailsPojo> cropsTotal) {
        this.context = context;
        this.cropsTotal = cropsTotal;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CropViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.crop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {

        List<CharSequence> crops = new ArrayList<>();
        for (Crop crop: cropsList) {
            crops.add(crop.getCropName());
        }
        CustomArrayAdapter adapter = new CustomArrayAdapter(context, R.layout.crops_list_item, crops);
        holder.selectCrop.setAdapter(adapter);

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.crops_list, R.layout.crops_list_item);
        holder.selectCrop.setAdapter(adapter);*/
    }

    @Override
    public int getItemCount() {
        return cropsTotal.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCropsList (List<Crop> cropsList) {
        this.cropsList = cropsList;
        notifyDataSetChanged();
    }

    class CropViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

            selectCrop.setOnClickListener(this);
            minusBag.setOnClickListener(this);
            plusBag.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int adapterPosition = getAdapterPosition();

            if (id == R.id.selectCrop) {

            }
            if (id == R.id.minusBag) {
                int numberOfBags = cropsTotal.get(adapterPosition).getBags();
                if (numberOfBags >= 1) {
                    numberOfBags--;
                    cropsTotal.get(adapterPosition).setBags(numberOfBags);
                    viewTotalBags.setText(String.valueOf(numberOfBags));
                    if (numberOfBags == 0) {
                        minusBag.setClickable(false);
                        minusBag.setImageResource(R.drawable.ic_minus_disabled);
                        minusBag.setBackgroundResource(R.drawable.button_disabled_bg);
                    }
                }
            }
            if (id == R.id.plusBag) {
                int numberOfBags = cropsTotal.get(adapterPosition).getBags();
                cropsTotal.get(adapterPosition).setBags(numberOfBags++);
                viewTotalBags.setText(String.valueOf(numberOfBags));
                if (numberOfBags == 1) {
                    minusBag.setClickable(true);
                    minusBag.setImageResource(R.drawable.ic_minus_enabled);
                    minusBag.setBackgroundResource(R.drawable.button_enabled_bg);
                }
            }
        }
    }
}
