package com.m_corp.millav.room;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface CropDao {

    @Insert
    void insertCrop(Crop crop);

    @Query("SELECT * FROM crop_details")
    LiveData<List<Crop>> getCrops();
}
