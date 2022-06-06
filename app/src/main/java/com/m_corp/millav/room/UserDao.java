package com.m_corp.millav.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user_details where mobile = :mobile")
    User[] getUser(String mobile);

    @Query("UPDATE user_details SET password = :password WHERE mobile = :mobile")
    void changePassword(String mobile, String password);
}
