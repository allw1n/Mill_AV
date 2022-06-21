package com.m_corp.millav.ui;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class StartNewActivity {

    private final Intent intent;
    private final ActivityResultLauncher<Intent> resultLauncher;

    public StartNewActivity(Intent intent, AppCompatActivity activity) {
        this.intent = intent;
        resultLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                            activity.setResult(Activity.RESULT_OK);
                            activity.finish();
                        }
                    }
                }
        );
    }

    public void setResultLauncher() {
        resultLauncher.launch(intent);
    }
}
