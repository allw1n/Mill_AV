package com.m_corp.millav.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_details")
public class User {

    @NonNull
    @PrimaryKey
    private final String mobile;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @NonNull
    private final String password;

    @ColumnInfo(name = "logged_in")
    private final boolean loggedIn;

    public User(@NonNull String mobile, @NonNull String name,
                @NonNull String password, boolean loggedIn) {
        this.mobile = mobile;
        this.name = name;
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
    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
