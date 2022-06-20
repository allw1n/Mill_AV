package com.m_corp.millav.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;
import com.m_corp.millav.databinding.ActivityEmployeeLogInBinding;
import com.m_corp.millav.room.Employee;
import com.m_corp.millav.viewmodel.EmployeeViewModel;

import java.util.Objects;

public class EmployeeLogInActivity extends AppCompatActivity {

    private ActivityEmployeeLogInBinding binding;
    private static final String MOBILE = "mobile";
    private static final String PASSWORD = "password";
    private static final String NONE = "none";
    private static final String SHARED_PREFS = "shared_prefs";
    public static final String BOTTOM_SHEET_TAG = "ForgotBottomSheetFragment";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmployeeLogInBinding.inflate(getLayoutInflater());
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

        EmployeeViewModel employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        String savedMobile = sharedPrefs.getString(MOBILE, NONE);
        String savedPassword = sharedPrefs.getString(PASSWORD, NONE);
        if (!savedMobile.equals(NONE)) {
            inputMobile.setText(savedMobile);
            inputPassword.setText(savedPassword);
            checkRememberMe.setChecked(true);
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
                String mobile, password;
                mobile = Objects.requireNonNull(inputMobile.getText()).toString().toLowerCase();
                password = Objects.requireNonNull(inputPassword.getText()).toString();

                if (TextUtils.isEmpty(mobile)) {
                    layoutInputMobile.setError(getString(R.string.required));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    layoutInputPassword.setError(getString(R.string.required));
                    return;
                }

                Employee[] employee = employeeViewModel.getEmployee(mobile);
                if (employee.length == 0)
                    layoutInputMobile.setError("Enter registered mobile number!");
                else if (!password.equals(employee[0].getPassword())) {
                    inputPassword.setText("");
                    layoutInputPassword.setError("Wrong password!");
                }
                else {
                    if (checkRememberMe.isChecked()) {
                        editor.putString(MOBILE, mobile);
                        editor.putString(PASSWORD, password);
                    } else {
                        editor.putString(MOBILE, NONE);
                        editor.putString(PASSWORD, NONE);
                    }
                    editor.apply();

                    employeeViewModel.loginEmployee(mobile, password, true);

                    Intent enterCropsIntent = new Intent(EmployeeLogInActivity.this, EnterCropsActivity.class);
                    enterCropsIntent.putExtra(MOBILE, mobile).putExtra(PASSWORD, password);
                    startActivity(enterCropsIntent);
                    finish();
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(EmployeeLogInActivity.this, RegisterEmployeeActivity.class);
                startActivity(registerIntent);
            }
        });

        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment fragment = ForgotPasswordFragment.newInstance();
                fragment.application = getApplication();
                fragment.show(getSupportFragmentManager(), BOTTOM_SHEET_TAG);
            }
        });
    }
}