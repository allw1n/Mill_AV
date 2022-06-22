package com.m_corp.millav.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.m_corp.millav.room.Crop;
import com.m_corp.millav.room.CropDao;
import com.m_corp.millav.room.MillAVDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public LiveData<List<Crop>> getCrops() {
        return crops;
    }

    public void insertCrop(Crop crop) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cropDao.insertCrop(crop);
            }
        }).start();
    }

    public Crop[] checkForCrop(String cropName) {

        Crop[] crop = new Crop[0];
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Crop[]> callable = new Callable<Crop[]>() {
            @Override
            public Crop[] call() throws Exception {
                return cropDao.checkForCrop(cropName);
            }
        };

        Future<Crop[]> future = executor.submit(callable);

        try {
            crop = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return crop;
    }
}
