package com.m_corp.millav.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;
import com.m_corp.millav.databinding.ActivityLogInBinding;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private static final String MOBILE = "mobile";
    private static final String PASSWORD = "password";
    private static final String NONE = "none";
    private static final String SHARED_PREFS = "shared_prefs";
    public static final String BOTTOM_SHEET_TAG = "ForgotBottomSheetFragment";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    private String savedMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPrefs.edit();

        TextInputLayout layoutInputMobile, layoutInputPassword;
        TextInputEditText inputMobile, inputPassword;
        MaterialCheckBox checkRememberMe;
        MaterialButton buttonLogin, buttonRegister, buttonForgot;

        layoutInputMobile = binding.layoutInputMobile;
        layoutInputPassword = binding.layoutInputPassword;
        inputMobile = binding.inputMobile;
        inputPassword = binding.inputPassword;

        checkRememberMe = binding.checkRememberMe;

        buttonLogin = binding.buttonLogin;
        buttonRegister = binding.buttonRegister;
        buttonForgot = binding.buttonForgot;

        savedMobile = sharedPrefs.getString(MOBILE, NONE);
        if (!savedMobile.equals(NONE)) {
            inputMobile.setText(savedMobile);
            inputPassword.setText(sharedPrefs.getString(PASSWORD, ""));
        }

        inputMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputMobile.isErrorEnabled()) layoutInputMobile.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputPassword.isErrorEnabled()) layoutInputPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginID, password;
                loginID = Objects.requireNonNull(inputMobile.getText()).toString().toLowerCase();
                password = Objects.requireNonNull(inputPassword.getText()).toString();

                if (TextUtils.isEmpty(loginID)) {
                    layoutInputMobile.setError(getString(R.string.required));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    layoutInputPassword.setError(getString(R.string.required));
                    return;
                }

                checkRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            editor.putString(MOBILE, loginID);
                            editor.putString(PASSWORD, password);
                        } else {
                            editor.putString(MOBILE, NONE);
                            editor.putString(PASSWORD, NONE);
                        }
                        editor.apply();
                    }
                });

                Intent enterCropsIntent = new Intent(LogInActivity.this, EnterCropsActivity.class);
                startActivity(enterCropsIntent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotBottomSheetFragment fragment = ForgotBottomSheetFragment.newInstance();
                fragment.application = getApplication();
                fragment.show(getSupportFragmentManager(), BOTTOM_SHEET_TAG);
            }
        });
    }
}