package com.m_corp.millav.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employee_details")
public class Employee {

    @NonNull
    @PrimaryKey
    private final String mobile;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @NonNull
    @ColumnInfo(name = "employer_mobile")
    private final String employerMobile;

    @NonNull
    private final String password;

    @ColumnInfo(name = "logged_in")
    private final boolean loggedIn;

    public Employee(@NonNull String mobile, @NonNull String name, @NonNull String employerMobile,
                    @NonNull String password, boolean loggedIn) {
        this.mobile = mobile;
        this.name = name;
        this.employerMobile = employerMobile;
        this.password = password;
        this.loggedIn = loggedIn;
    }

    @NonNull
    public String getMobile() {
        return mobile;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getEmployerMobile() {
        return employerMobile;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
