package com.m_corp.millav.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CropDao {

    @Insert
    void insertCrop(Crop crop);

    @Query("SELECT * FROM crops_list")
    LiveData<List<Crop>> getCrops();

    @Query("SELECT * FROM crops_list WHERE crop_name = :cropName")
    Crop[] checkForCrop(String cropName);
}
