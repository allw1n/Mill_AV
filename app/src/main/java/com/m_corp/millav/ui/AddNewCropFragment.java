package com.m_corp.millav.ui;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Crop;
import com.m_corp.millav.viewmodel.CropViewModel;

import java.util.Objects;

public class AddNewCropFragment extends BottomSheetDialogFragment {

    public Application application;

    private TextInputLayout layoutInputNewCropName, layoutInputPricePerKg;
    private TextInputEditText inputNewCropName, inputPricePerKg;

    private CropViewModel cropViewModel;

    public AddNewCropFragment() {
        // Required empty public constructor
    }

    public static AddNewCropFragment newInstance() {
        return new AddNewCropFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_new_crop, container, false);

        layoutInputNewCropName = rootView.findViewById(R.id.layoutInputNewCropName);
        layoutInputPricePerKg = rootView.findViewById(R.id.layoutInputPricePerKg);

        inputNewCropName = rootView.findViewById(R.id.inputNewCropName);
        inputPricePerKg = rootView.findViewById(R.id.inputPricePerKg);

        MaterialButton buttonAddCrop = rootView.findViewById(R.id.buttonAddCrop);

        cropViewModel = new ViewModelProvider(this).get(CropViewModel.class);

        inputNewCropName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputNewCropName.isErrorEnabled())
                    layoutInputNewCropName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputPricePerKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputPricePerKg.isErrorEnabled())
                    layoutInputPricePerKg.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonAddCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCropName, pricePerKg;
                boolean fixedWeight;

                newCropName = Objects.requireNonNull(inputNewCropName.getText()).toString();
                pricePerKg = Objects.requireNonNull(inputPricePerKg.getText()).toString();

                if (TextUtils.isEmpty(newCropName)) {
                    layoutInputNewCropName.setError("Required!");
                    return;
                }
                if (TextUtils.isEmpty(pricePerKg)) {
                    layoutInputPricePerKg.setError("Required!");
                    return;
                }

                Crop[] crop = cropViewModel.checkForCrop(newCropName);
                if (crop.length == 0) {
                    Crop newCrop = new Crop(newCropName, Float.parseFloat(pricePerKg));
                    cropViewModel.insertCrop(newCrop);

                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .remove(AddNewCropFragment.this)
                            .commit();
                } else {
                    layoutInputNewCropName.setError("Crop already exists!");
                }
            }
        });

        return rootView;
    }
}