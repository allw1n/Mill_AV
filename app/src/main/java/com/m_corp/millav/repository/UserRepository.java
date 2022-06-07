package com.m_corp.millav.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.m_corp.millav.room.MillAVDatabase;
import com.m_corp.millav.room.User;
import com.m_corp.millav.room.UserDao;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserRepository {

    private final UserDao userDao;
    private final Application application;

    public UserRepository(Application application) {
        this.application = application;
        MillAVDatabase database = MillAVDatabase.getDatabase(application);
        userDao = database.userDao();
    }

    public User[] getUser(String mobile) {

        User[] user = new User[0];
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<User[]> callable = new Callable<User[]>() {
            @Override
            public User[] call() throws Exception {
                return userDao.getUser(mobile);
            }
        };

        Future<User[]> future = executor.submit(callable);
        try {
            user = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return user;
    }

    public void insertUser(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.insertUser(user);
            }
        }).start();
    }

    public void changePassword(String mobile, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.changePassword(mobile, password);
            }
        }).start();
    }

    public void loginUser(String mobile, String password, boolean loggedIn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.loginUser(mobile, password, loggedIn);
            }
        }).start();
    }
}
