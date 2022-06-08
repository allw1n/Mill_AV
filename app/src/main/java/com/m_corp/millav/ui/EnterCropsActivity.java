package com.m_corp.millav.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.m_corp.millav.databinding.ActivityEnterCropsBinding;
import com.m_corp.millav.room.Crop;
import com.m_corp.millav.viewmodel.CropViewModel;
import com.m_corp.millav.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class EnterCropsActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private CropViewModel cropViewModel;

    private static final String MOBILE = "mobile";
    private static final String PASSWORD = "password";
    private String savedMobile, savedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEnterCropsBinding binding = ActivityEnterCropsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ExtendedFloatingActionButton fabCreateBill = binding.fabCreateBill;
        ExtendedFloatingActionButton fabAddNew = binding.fabAddNew;

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        cropViewModel = new ViewModelProvider(this).get(CropViewModel.class);

        savedMobile = getIntent().getStringExtra(MOBILE);
        savedPassword = getIntent().getStringExtra(PASSWORD);

        List<CropDetailsPojo> cropsTotal = new ArrayList<>();
        cropsTotal.add(new CropDetailsPojo());
        Log.d("CROP", cropsTotal.get(0).getCropName());

        RecyclerView recyclerCrops = binding.recyclerCrops;
        recyclerCrops.setLayoutManager(new LinearLayoutManager(this));
        CropsAdapter cropsAdapter = new CropsAdapter(this, cropsTotal);
        recyclerCrops.setAdapter(cropsAdapter);

        cropViewModel.getCrops().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> cropsList) {
                cropsAdapter.setCropsList(cropsList);
            }
        });

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropsTotal.add(new CropDetailsPojo());
                cropsAdapter.notifyItemInserted(cropsTotal.size());
            }
        });

        fabCreateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        userViewModel.loginUser(savedMobile, savedPassword, false);

                        startActivity(new Intent(EnterCropsActivity.this,
                                LogInActivity.class));
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