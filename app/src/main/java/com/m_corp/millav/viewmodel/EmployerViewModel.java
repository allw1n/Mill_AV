package com.m_corp.millav.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.m_corp.millav.repository.EmployerRepository;
import com.m_corp.millav.room.Employer;

public class EmployerViewModel extends AndroidViewModel {

    private final EmployerRepository employerRepository;

    public EmployerViewModel(@NonNull Application application) {
        super(application);
        employerRepository = new EmployerRepository(application);
    }

    public Employer[] getEmployer(String mobile) {
        return employerRepository.getEmployer(mobile);
    }

    public void insertEmployer(Employer employer) {
        employerRepository.insertEmployer(employer);
    }

    public void changePassword(String mobile, String password) {
        employerRepository.changePassword(mobile, password);
    }

    public void loginEmployer(String mobile, String password, boolean loggedIn) {
        employerRepository.loginEmployer(mobile, password, loggedIn);
    }
}
