package com.m_corp.millav.ui;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;

import java.util.Objects;

public class ChangePriceFragment extends BottomSheetDialogFragment {

    private TextInputLayout layoutInputChangePrice;
    private TextInputEditText inputChangePrice;

    private OnButtonSetClickListener buttonSetClickListener;

    public ChangePriceFragment() {
        // Required empty public constructor
    }

    public static ChangePriceFragment newInstance() {
        return new ChangePriceFragment();
    }

    public void setOnButtonSetClickListener(OnButtonSetClickListener buttonSetClickListener) {
        this.buttonSetClickListener = buttonSetClickListener;
    }

    public interface OnButtonSetClickListener {
        void onButtonSetClickListener(String newPrice);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater
                .inflate(R.layout.fragment_change_price, container, false);

        layoutInputChangePrice = rootView.findViewById(R.id.layoutInputChangePrice);
        inputChangePrice = rootView.findViewById(R.id.inputChangePrice);
        MaterialButton buttonSet = rootView.findViewById(R.id.buttonSet);
        MaterialButton buttonCancel = rootView.findViewById(R.id.buttonCancel);

        inputChangePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputChangePrice != null && layoutInputChangePrice.isErrorEnabled())
                    layoutInputChangePrice.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPrice = Objects.
                        requireNonNull(inputChangePrice.getText()).toString();
                if (TextUtils.isEmpty(newPrice)) {
                    if (layoutInputChangePrice != null) {
                        layoutInputChangePrice.setError("Required");
                        return;
                    }
                }

                buttonSetClickListener.onButtonSetClickListener(newPrice);
                closeFragment();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        return rootView;
    }

    void closeFragment() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(ChangePriceFragment.this)
                .commit();
    }
}