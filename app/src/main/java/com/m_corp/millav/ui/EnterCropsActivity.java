package com.m_corp.millav.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.databinding.ActivityEnterCropsBinding;
import com.m_corp.millav.room.Crop;
import com.m_corp.millav.viewmodel.CropViewModel;
import com.m_corp.millav.viewmodel.EmployeeViewModel;
import com.m_corp.millav.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnterCropsActivity extends AppCompatActivity {

    private EmployeeViewModel employeeViewModel;
    private CropViewModel cropViewModel;
    private List<Crop> cropsListFromSource = new ArrayList<>();
    private int cropsTotalWeighed = -1;
    private CropsAdapter cropsAdapter;

    private static final String MOBILE = "mobile";
    private static final String PASSWORD = "password";
    private String savedMobile, savedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEnterCropsBinding binding = ActivityEnterCropsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextInputLayout layoutLoadedWeight, layoutUnloadedWeight;
        TextInputEditText inputLoadedWeight, inputUnloadedWeight;

        layoutLoadedWeight = binding.layoutLoadedWeight;
        layoutUnloadedWeight = binding.layoutUnloadedWeight;

        inputLoadedWeight = binding.inputLoadedWeight;
        inputUnloadedWeight = binding.inputUnloadedWeight;

        ExtendedFloatingActionButton fabSend = binding.fabSend;
        ExtendedFloatingActionButton fabAddNew = binding.fabAddNew;

        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        savedMobile = getIntent().getStringExtra(MOBILE);
        savedPassword = getIntent().getStringExtra(PASSWORD);

        RecyclerView recyclerCrops = binding.recyclerCrops;
        recyclerCrops.setLayoutManager(new LinearLayoutManager(this));
        cropsAdapter = new CropsAdapter(this);
        recyclerCrops.setAdapter(cropsAdapter);

        cropViewModel = new ViewModelProvider(this).get(CropViewModel.class);
        cropViewModel.getCrops().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(@NonNull final List<Crop> cropsList) {
                cropsListFromSource = cropsList;
                cropsAdapter.setCropsList(cropsListFromSource);
            }
        });

        cropsAdapter.setOnRecyclerViewItemClickListener(new CropsAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position, CropsAddedPojo cropAdded) {
                Log.d("Position", String.valueOf(position));
                if (view.getId() == R.id.minusBag || view.getId() == R.id.plusBag) {
                    Log.d("Crop bags set to", String.valueOf(cropAdded.getBags()));
                }
                if (view.getId() == R.id.selectCropItem) {
                    Log.d("Crop name changed", cropAdded.getCropName());
                }
                cropsAdapter.notifyItemChanged(position);

                Log.d("Crop Name - ", cropAdded.getCropName());
                Log.d("Crop Bags - ", String.valueOf(cropAdded.getBags()));
                Log.d("Crop Weight - ", String.valueOf(cropAdded.getWeight()));
            }
        });

        inputLoadedWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutLoadedWeight.isErrorEnabled())
                    layoutLoadedWeight.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputUnloadedWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutUnloadedWeight.isErrorEnabled())
                    layoutUnloadedWeight.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropsTotalWeighed++;
                Log.d("Add new crop on fab", String.valueOf(cropsTotalWeighed));
                cropsAdapter.addNewToCropsTotalWeighed();
                cropsAdapter.notifyItemInserted(cropsTotalWeighed);
                recyclerCrops.smoothScrollToPosition(cropsTotalWeighed);
            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loadedWeight, unloadedWeight;
                float loaded, unloaded, netCalculated;

                loadedWeight = Objects.requireNonNull(inputLoadedWeight.getText()).toString();
                unloadedWeight = Objects.requireNonNull(inputUnloadedWeight.getText()).toString();

                if (TextUtils.isEmpty(loadedWeight)) {
                    layoutLoadedWeight.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(unloadedWeight)) {
                    layoutUnloadedWeight.setError("Required");
                    return;
                }

                loaded = Float.parseFloat(loadedWeight);
                unloaded = Float.parseFloat(unloadedWeight);

                if (validate(loaded, unloaded)) {
                    startActivity(new Intent(EnterCropsActivity.this,
                            EnterCropsActivity.class));
                    finish();
                }
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
                        employeeViewModel.loginUser(savedMobile, savedPassword, false);

                        startActivity(new Intent(EnterCropsActivity.this,
                                EmployeeLogInActivity.class));
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

    private boolean validate(float loaded, float unloaded) {

        boolean validated = false;
        float calculatedWeight = loaded - unloaded;
        float enteredWeight = cropsAdapter.getEnteredWeight();

        if (calculatedWeight == Math.floor(enteredWeight))
            validated = true;
        else if (calculatedWeight == Math.ceil(enteredWeight))
            validated = true;

        return validated;
    }
}