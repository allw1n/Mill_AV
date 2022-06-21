package com.m_corp.millav.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface EmployerDao {

    @Insert
    void insertEmployer(Employer employer);

    @Query("SELECT * FROM employer_details where mobile = :mobile")
    Employer[] getEmployer(String mobile);

    @Query("UPDATE employer_details SET password = :password WHERE mobile = :mobile")
    void changePassword(String mobile, String password);

    @Query("UPDATE employer_details SET logged_in = :loggedIn WHERE mobile = :mobile AND password = :password")
    void loginEmployer(String mobile, String password, boolean loggedIn);
}
