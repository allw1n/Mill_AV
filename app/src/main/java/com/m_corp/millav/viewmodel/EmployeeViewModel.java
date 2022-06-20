package com.m_corp.millav.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.m_corp.millav.repository.EmployeeRepository;
import com.m_corp.millav.room.Employee;

public class EmployeeViewModel extends AndroidViewModel {

    private final EmployeeRepository employeeRepository;

    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        employeeRepository = new EmployeeRepository(application);
    }

    public Employee[] getEmployee(String mobile) {
        return employeeRepository.getEmployee(mobile);
    }

    public void insertEmployee(Employee employee) {
        employeeRepository.insertEmployee(employee);
    }

    public void changePassword(String mobile, String password) {
        employeeRepository.changePassword(mobile, password);
    }

    public void loginEmployee(String mobile, String password, boolean loggedIn) {
        employeeRepository.loginEmployee(mobile, password, loggedIn);
    }
}
