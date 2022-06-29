package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE_PASSWORD;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER_PASSWORD;
import static com.m_corp.millav.utils.MillAVUtils.LOG_IN_TYPE;
import static com.m_corp.millav.utils.MillAVUtils.NONE;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.databinding.ActivityEnterCropsBinding;
import com.m_corp.millav.room.Bill;
import com.m_corp.millav.room.Crop;
import com.m_corp.millav.viewmodel.BillViewModel;
import com.m_corp.millav.viewmodel.CropViewModel;
import com.m_corp.millav.viewmodel.EmployeeViewModel;
import com.m_corp.millav.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnterCropsActivity extends AppCompatActivity {

    private List<Crop> cropsListFromSource = new ArrayList<>();
    private int cropsTotalWeighed = -1;
    private EnterCropsAdapter enterCropsAdapter;

    private String savedMobile, savedPassword, loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEnterCropsBinding binding = ActivityEnterCropsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextInputLayout layoutLoadedWeight, layoutTruckWeight;
        TextInputEditText inputLoadedWeight, inputTruckWeight;

        layoutLoadedWeight = binding.layoutLoadedWeight;
        layoutTruckWeight = binding.layoutTruckWeight;

        inputLoadedWeight = binding.inputLoadedWeight;
        inputTruckWeight = binding.inputTruckWeight;

        ExtendedFloatingActionButton fabSend = binding.fabSend;
        ExtendedFloatingActionButton fabAddNew = binding.fabAddNew;

        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loginType = sharedPrefs.getString(LOG_IN_TYPE, NONE);

        if (loginType.equals(EMPLOYEE)) {
            savedMobile = sharedPrefs.getString(EMPLOYEE_MOBILE, NONE);
            savedPassword = sharedPrefs.getString(EMPLOYEE_PASSWORD, NONE);
        } else {

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            savedMobile = sharedPrefs.getString(EMPLOYER_MOBILE, NONE);
            savedPassword = sharedPrefs.getString(EMPLOYER_PASSWORD, NONE);
        }

        RecyclerView recyclerCrops = binding.recyclerCrops;
        recyclerCrops.setLayoutManager(new LinearLayoutManager(this));
        enterCropsAdapter = new EnterCropsAdapter(this);
        recyclerCrops.setAdapter(enterCropsAdapter);

        CropViewModel cropViewModel = new ViewModelProvider(this).get(CropViewModel.class);
        cropViewModel.getCrops().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(@NonNull final List<Crop> cropsList) {
                cropsListFromSource = cropsList;
                enterCropsAdapter.setCropsList(cropsListFromSource);
            }
        });

        enterCropsAdapter.setOnRecyclerItemClickListener(
                new EnterCropsAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position, CropsAddedPojo cropAdded) {
                Log.d("Position", String.valueOf(position));
                if (view.getId() == R.id.minusBag || view.getId() == R.id.plusBag) {
                    Log.d("Crop bags set to", String.valueOf(cropAdded.getBags()));
                }
                if (view.getId() == R.id.selectCropItem) {
                    Log.d("Crop name changed", cropAdded.getCropName());
                }
                enterCropsAdapter.notifyItemChanged(position);

                Log.d("Crop Name - ", cropAdded.getCropName());
                Log.d("Crop Bags - ", String.valueOf(cropAdded.getBags()));
                Log.d("Crop Weight - ", String.valueOf(cropAdded.getWeight()));
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                cropsTotalWeighed--;
                enterCropsAdapter.removeFromCropsTotalWeighed(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerCrops);

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

        inputTruckWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutTruckWeight.isErrorEnabled())
                    layoutTruckWeight.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropsTotalWeighed++;
                Log.d("Add new crop on fab", String.valueOf(cropsTotalWeighed));
                enterCropsAdapter.addNewToCropsTotalWeighed();
                enterCropsAdapter.notifyItemInserted(cropsTotalWeighed);
                recyclerCrops.smoothScrollToPosition(cropsTotalWeighed);
            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loadedWeight, truckWeight;
                float loaded, truck, netCalculated;

                loadedWeight = Objects.requireNonNull(inputLoadedWeight.getText()).toString();
                truckWeight = Objects.requireNonNull(inputTruckWeight.getText()).toString();

                if (TextUtils.isEmpty(loadedWeight)) {
                    layoutLoadedWeight.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(truckWeight)) {
                    layoutTruckWeight.setError("Required");
                    return;
                }

                loaded = Float.parseFloat(loadedWeight);
                truck = Float.parseFloat(truckWeight);

                if (isValid(loaded, truck)) {
                    BillViewModel billViewModel = new ViewModelProvider(
                            EnterCropsActivity.this).get(BillViewModel.class);

                    Bill newBill = enterCropsAdapter.setBillDetails();

                    billViewModel.insertBill(newBill);

                    Toast.makeText(EnterCropsActivity.this, "Bill added to database.",
                            Toast.LENGTH_SHORT).show();

                    if (loginType.equals(EMPLOYEE)) {
                        startActivity(new Intent(
                                EnterCropsActivity.this, EnterCropsActivity.class));
                    }
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.layoutEnterCrops),
                            R.string.weights_do_not_match, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (loginType.equals(EMPLOYEE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Log Out!")
                    .setMessage("Do you want to Log Out?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            EmployeeViewModel employeeViewModel = new ViewModelProvider(
                                    EnterCropsActivity.this)
                                    .get(EmployeeViewModel.class);
                            employeeViewModel.loginEmployee(savedMobile, savedPassword,
                                    false);

                            startActivity(new Intent(EnterCropsActivity.this,
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
        } else
            finish();
    }

    private boolean isValid(float loaded, float truck) {
        return enterCropsAdapter.validate(loaded - truck);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}