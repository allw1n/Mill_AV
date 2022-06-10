package com.m_corp.millav.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.m_corp.millav.repository.CropRepository;
import com.m_corp.millav.room.Crop;

import java.util.List;

public class CropViewModel extends AndroidViewModel {

    private final LiveData<List<Crop>> crops;
    private final CropRepository cropRepository;

    public CropViewModel(@NonNull Application application) {
        super(application);
        cropRepository = new CropRepository(application);
        crops = cropRepository.getCrops();
        for (int i = 0; i < 3; i++) {
            Log.d("CropViewModel crop", String.valueOf(crops.getValue()));
        }
    }

    public void insertCrop(Crop crop) {
        cropRepository.insertCrop(crop);
    }

    public LiveData<List<Crop>> getCrops() {
        return crops;
    }
}
