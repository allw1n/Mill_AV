package com.m_corp.millav.repository;

import android.app.Application;

import com.m_corp.millav.room.MillAVDatabase;
import com.m_corp.millav.room.Employee;
import com.m_corp.millav.room.EmployeeDao;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EmployeeRepository {

    private final EmployeeDao employeeDao;
    private final Application application;

    public EmployeeRepository(Application application) {
        this.application = application;
        MillAVDatabase database = MillAVDatabase.getDatabase(application);
        employeeDao = database.userDao();
    }

    public Employee[] getEmployee(String mobile) {

        Employee[] employee = new Employee[0];
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Employee[]> callable = new Callable<Employee[]>() {
            @Override
            public Employee[] call() throws Exception {
                return employeeDao.getEmployee(mobile);
            }
        };

        Future<Employee[]> future = executor.submit(callable);
        try {
            employee = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return employee;
    }

    public void insertEmployee(Employee employee) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                employeeDao.insertEmployee(employee);
            }
        }).start();
    }

    public void changePassword(String mobile, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                employeeDao.changePassword(mobile, password);
            }
        }).start();
    }

    public void loginEmployee(String mobile, String password, boolean loggedIn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                employeeDao.loginEmployee(mobile, password, loggedIn);
            }
        }).start();
    }
}
