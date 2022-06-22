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

    public abstract BillDao billDao();

    private static volatile MillAVDatabase INSTANCE;

    public static MillAVDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MillAVDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MillAVDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
