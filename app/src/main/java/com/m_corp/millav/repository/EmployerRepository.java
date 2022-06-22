package com.m_corp.millav.repository;

import android.app.Application;

import com.m_corp.millav.room.Employer;
import com.m_corp.millav.room.EmployerDao;
import com.m_corp.millav.room.MillAVDatabase;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EmployerRepository {

    private final EmployerDao employerDao;

    public EmployerRepository(Application application) {
        MillAVDatabase database = MillAVDatabase.getDatabase(application);
        employerDao = database.employerDao();
    }

    public Employer[] getEmployer(String mobile) {

        Employer[] employer = new Employer[0];
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Employer[]> callable = new Callable<Employer[]>() {
            @Override
            public Employer[] call() throws Exception {
                return employerDao.getEmployer(mobile);
            }
        };

        Future<Employer[]> future = executor.submit(callable);
        try {
            employer = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return employer;
    }

    public void insertEmployer(Employer employer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                employerDao.insertEmployer(employer);
            }
        }).start();
    }

    public void changePassword(String mobile, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                employerDao.changePassword(mobile, password);
            }
        }).start();
    }

    public void loginEmployer(String mobile, String password, boolean loggedIn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                employerDao.loginEmployer(mobile, password, loggedIn);
            }
        }).start();
    }
}
