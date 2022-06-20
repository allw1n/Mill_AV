package com.m_corp.millav.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface EmployeeDao {

    @Insert
    void insertEmployee(Employee employee);

    @Query("SELECT * FROM employee_details where mobile = :mobile")
    Employee[] getEmployee(String mobile);

    @Query("UPDATE employee_details SET password = :password WHERE mobile = :mobile")
    void changePassword(String mobile, String password);

    @Query("UPDATE employee_details SET logged_in = :loggedIn WHERE mobile = :mobile AND password = :password")
    void loginEmployee(String mobile, String password, boolean loggedIn);
}
