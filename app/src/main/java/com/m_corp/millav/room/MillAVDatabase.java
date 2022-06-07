package com.m_corp.millav.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class MillAVDatabase extends RoomDatabase {

    private static final String DB_NAME = "mill_av_db";

    public abstract UserDao userDao();

    public abstract CropDao cropDao();

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
