package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.ADD_NEW_CROP_TAG;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER_PASSWORD;
import static com.m_corp.millav.utils.MillAVUtils.NONE;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.m_corp.millav.databinding.ActivityEmployerDashboardBinding;
import com.m_corp.millav.viewmodel.EmployerViewModel;

public class EmployerDashboard extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private String loginType, savedMobile, savedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEmployerDashboardBinding binding = ActivityEmployerDashboardBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPrefs.edit();
        savedMobile = sharedPrefs.getString(EMPLOYER_MOBILE, NONE);
        savedPassword = sharedPrefs.getString(EMPLOYER_PASSWORD, NONE);

        MaterialButton buttonMakeBill, buttonAddNewCrop, buttonEnterCrops;

        buttonMakeBill = binding.buttonMakeBill;
        buttonAddNewCrop = binding.buttonAddNewCrop;
        buttonEnterCrops = binding.buttonEnterCrops;

        buttonMakeBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployerDashboard.this,
                        PendingBillsActivity.class));
            }
        });

        buttonAddNewCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewCropFragment newCropFragment = AddNewCropFragment.newInstance();
                newCropFragment.application = getApplication();
                newCropFragment.show(getSupportFragmentManager(), ADD_NEW_CROP_TAG);
            }
        });

        buttonEnterCrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployerDashboard.this,
                        EnterCropsActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out!")
                .setMessage("Do you want to Log Out?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EmployerViewModel employerViewModel = new ViewModelProvider(
                                EmployerDashboard.this)
                                .get(EmployerViewModel.class);
                        employerViewModel.loginEmployer(savedMobile, savedPassword,
                                false);
                        startActivity(new Intent(EmployerDashboard.this,
                                MainActivity.class));
                        finish();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}