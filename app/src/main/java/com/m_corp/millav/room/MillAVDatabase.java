package com.m_corp.millav.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Employee.class, Employer.class, Crop.class, Bill.class},
        version = 1, exportSchema = false)
public abstract class MillAVDatabase extends RoomDatabase {

    private static final String DB_NAME = "mill_av_db";

    public abstract EmployeeDao employeeDao();

    public abstract EmployerDao employerDao();

    public abstract CropDao cropDao();

    private static volatile MillAVDatabase INSTANCE;

    private static final RoomDatabase.Callback populateCropsList = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CropDao cropDao;
                    cropDao = INSTANCE.cropDao();

                    if (cropDao.getACrop().length == 0) {
                        String[] cropsList = {"None", "Rice", "Wheat", "Arhar Dal"};
                        for (String crop : cropsList) {
                            cropDao.insertCrop(new Crop(crop));
                        }
                    }
                }
            }).start();
        }
    };

    public static MillAVDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MillAVDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MillAVDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(populateCropsList)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
