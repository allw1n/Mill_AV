package com.m_corp.millav.ui;

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

import java.util.List;

public class CropsAdapter extends RecyclerView.Adapter<CropsAdapter.CropViewHolder> {

    private final Context context;
    private List<Crop> crops;

    public CropsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CropViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.crop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class CropViewHolder extends RecyclerView.ViewHolder {

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
        }
    }
}
