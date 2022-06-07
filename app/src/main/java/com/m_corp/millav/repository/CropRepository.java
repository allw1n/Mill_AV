package com.m_corp.millav.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.m_corp.millav.room.Crop;
import com.m_corp.millav.room.CropDao;
import com.m_corp.millav.room.MillAVDatabase;

import java.util.List;

public class CropRepository {

    private final CropDao cropDao;
    private final Application application;
    private final LiveData<List<Crop>> crops;

    public CropRepository(Application application) {
        this.application = application;
        MillAVDatabase database = MillAVDatabase.getDatabase(application);
        cropDao = database.cropDao();
        crops = cropDao.getCrops();
    }

    public void insertCrop(Crop crop) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cropDao.insertCrop(crop);
            }
        }).start();
    }

    public LiveData<List<Crop>> getCrops() {
        return crops;
    }
}
