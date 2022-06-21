package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER;
import static com.m_corp.millav.utils.MillAVUtils.LOG_IN_TYPE;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.m_corp.millav.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private StartNewActivity newActivity;

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPrefs.edit();

        Intent loginIntent = new Intent(MainActivity.this,
                LogInActivity.class);
        newActivity = new StartNewActivity(loginIntent, MainActivity.this);

        MaterialButton buttonEmployer, buttonEmployee;

        buttonEmployer = binding.buttonEmployer;
        buttonEmployee = binding.buttonEmployee;

        buttonEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(LOG_IN_TYPE, EMPLOYER);
                editor.apply();
                newActivity.setResultLauncher();
            }
        });

        buttonEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(LOG_IN_TYPE, EMPLOYEE);
                editor.apply();
                newActivity.setResultLauncher();
            }
        });
    }
}