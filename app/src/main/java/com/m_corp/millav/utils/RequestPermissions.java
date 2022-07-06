package com.m_corp.millav.utils;

import static com.m_corp.millav.utils.MillAVUtils.DENIED;
import static com.m_corp.millav.utils.MillAVUtils.MANAGE_PERMISSION;
import static com.m_corp.millav.utils.MillAVUtils.SDK_VERSION;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;
import static com.m_corp.millav.utils.MillAVUtils.WRITE_PERMISSION;
import static com.m_corp.millav.utils.MillAVUtils.ZERO;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RequestPermissions {

    private final AppCompatActivity activity;
    private final String permission;
    private final ActivityResultLauncher<String> requestPermission;
    private boolean permissionGranted = false;
    private final SharedPreferences sharedPrefs;
    private final SharedPreferences.Editor editor;

    public RequestPermissions(AppCompatActivity activity, String permission) {
        this.activity = activity;
        this.permission = permission;

        requestPermission = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            permissionGranted = true;
                            editor.putInt(DENIED, 0);
                        }
                        else {
                            if (sharedPrefs.getInt(DENIED, ZERO) < 2)
                                editor.putInt(DENIED, sharedPrefs.getInt(DENIED, ZERO) + 1);
                        }
                        editor.apply();
                        Log.d("Permission result", String.valueOf(result));
                    }
                }
        );

        sharedPrefs = activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        if (!sharedPrefs.contains(DENIED)) {
            Log.d("DENIED", "initialized");
            editor.putInt(DENIED, ZERO);
            editor.apply();
        }
    }

    public boolean permissionsGranted() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Permission required!");
        builder.setMessage("Please grant required permission to save Bill PDF to device storage.");
        builder.setCancelable(false);

        if (SDK_VERSION >= Build.VERSION_CODES.R) {
            Log.d("R-version check", String.valueOf(SDK_VERSION));

            if (Environment.isExternalStorageManager()) {
                permissionGranted = true;
                Log.d("R-selfCheck granted", String.valueOf(true));
            } else {
                Log.d("R-selfCheck granted", String.valueOf(false));
                builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent settingsIntent = new Intent(
                                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        settingsIntent.setData(
                                Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivity(settingsIntent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else if (SDK_VERSION == Build.VERSION_CODES.Q) {
            Log.d("Q-version check", String.valueOf(SDK_VERSION));

            if (Environment.isExternalStorageLegacy()) {
                permissionGranted = true;
                Log.d("Q-selfCheck granted", String.valueOf(true));

            } else {
                Log.d("Q-selfCheck granted", String.valueOf(false));
                builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent settingsIntent = new Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        settingsIntent.setData(
                                Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivity(settingsIntent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else if (SDK_VERSION >= Build.VERSION_CODES.M) {
            Log.d("Others-version check", String.valueOf(SDK_VERSION));

            if (sharedPrefs.getInt(DENIED, ZERO) < 2) {
                Log.d("Others-DENIED check", String.valueOf(sharedPrefs.getInt(DENIED, ZERO)));

                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    Log.d("Others-rationale check",
                            String.valueOf(
                                    activity.shouldShowRequestPermissionRationale(permission)));

                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d("Others-Permission","requested in rationale");
                            requestPermission.launch(permission);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d("Others-rationale Cancel", String.valueOf(permissionGranted));
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Log.d("Q-M-rationale check",
                            String.valueOf(
                                    activity.shouldShowRequestPermissionRationale(permission)));
                    requestPermission.launch(permission);
                }

            } else {
                Log.d(DENIED,"value equal or more than 2");
                builder.setPositiveButton(
                        "Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Others-Permission","requested from settings");
                                Intent settingsIntent = new Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                settingsIntent.setData(
                                        Uri.parse("package:" + activity.getPackageName()));
                                activity.startActivity(settingsIntent);
                            }
                        });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Others-rationale Cancel", String.valueOf(permissionGranted));
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else {
                Log.d("Others-version Check", String.valueOf(SDK_VERSION));
        }

        Log.d("Permission Check", "exit");
        return permissionGranted;
    }
}