package com.m_corp.millav.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.m_corp.millav.room.Crop;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<CharSequence> {

    private List<Crop> cropsList;

    public CustomArrayAdapter(Context context, int textViewResourceId, List<Crop> cropsList) {
        super(context, textViewResourceId);
        this.cropsList = cropsList;
    }

    @Override
    public int getCount() {
        return this.cropsList.size();
    }

    @Nullable
    @Override
    public CharSequence getItem(int position) {
        return this.cropsList.get(position).getCropName();
    }
}