package com.m_corp.millav.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.m_corp.millav.repository.UserRepository;
import com.m_corp.millav.room.User;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public User[] getUser(String mobile) {
        return userRepository.getUser(mobile);
    }

    public void insertUser(User user) {
        userRepository.insertUser(user);
    }

    public void changePassword(String mobile, String password) {
        userRepository.changePassword(mobile, password);
    }
}
