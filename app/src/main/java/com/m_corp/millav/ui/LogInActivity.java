package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.BOTTOM_SHEET_TAG;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE_PASSWORD;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER_PASSWORD;
import static com.m_corp.millav.utils.MillAVUtils.LOG_IN_TYPE;
import static com.m_corp.millav.utils.MillAVUtils.NONE;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;
import com.m_corp.millav.databinding.ActivityLogInBinding;
import com.m_corp.millav.room.Employee;
import com.m_corp.millav.room.Employer;
import com.m_corp.millav.viewmodel.EmployeeViewModel;
import com.m_corp.millav.viewmodel.EmployerViewModel;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private TextInputEditText inputMobile, inputPassword;
    private TextInputLayout layoutInputMobile, layoutInputPassword;
    private MaterialCheckBox checkRememberMe;

    private String loginType, savedEmployerMobile, savedEmployerPassword,
            savedEmployeeMobile, savedEmployeePassword;

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPrefs.edit();

        MaterialButton buttonLogin, buttonRegister, buttonForgot;

        layoutInputMobile = binding.layoutInputMobile;
        layoutInputPassword = binding.layoutInputPassword;
        inputMobile = binding.inputMobile;
        inputPassword = binding.inputPassword;

        checkRememberMe = binding.checkRememberMe;

        buttonLogin = binding.buttonLogin;
        buttonRegister = binding.buttonRegister;
        buttonForgot = binding.buttonForgot;

        loginType = sharedPrefs.getString(LOG_IN_TYPE, NONE);

        getSetMobilePassword();

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

                if (loginType.equals(EMPLOYEE)) {
                    loginEmployee(mobile, password);
                } else {
                    loginEmployer(mobile, password);
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
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

    private void getSetMobilePassword() {
        if (loginType.equals(EMPLOYEE)) {
            savedEmployeeMobile = sharedPrefs.getString(EMPLOYEE_MOBILE, NONE);
            savedEmployeePassword = sharedPrefs.getString(EMPLOYEE_PASSWORD, NONE);

            if (!savedEmployeeMobile.equals(NONE)) {
                inputMobile.setText(savedEmployeeMobile);
                inputPassword.setText(savedEmployeePassword);
                checkRememberMe.setChecked(true);
            }
        } else {
            savedEmployerMobile = sharedPrefs.getString(EMPLOYER_MOBILE, NONE);
            savedEmployerPassword = sharedPrefs.getString(EMPLOYER_PASSWORD, NONE);

            if (!savedEmployerMobile.equals(NONE)) {
                inputMobile.setText(savedEmployerMobile);
                inputPassword.setText(savedEmployerPassword);
                checkRememberMe.setChecked(true);
            }
        }
    }

    private void loginEmployee(String mobile, String password) {
        EmployeeViewModel employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        Employee[] employee = employeeViewModel.getEmployee(mobile);
        if (employee.length == 0)
            layoutInputMobile.setError("Enter registered mobile number!");
        else if (!password.equals(employee[0].getPassword())) {
            inputPassword.setText("");
            layoutInputPassword.setError("Wrong password!");
        }
        else {
            if (checkRememberMe.isChecked()) {
                editor.putString(EMPLOYEE_MOBILE, mobile);
                editor.putString(EMPLOYEE_PASSWORD, password);
            } else {
                editor.putString(EMPLOYEE_MOBILE, NONE);
                editor.putString(EMPLOYEE_PASSWORD, NONE);
            }
            editor.apply();

            employeeViewModel.loginEmployee(mobile, password, true);

            Intent enterCropsIntent = new Intent(LogInActivity.this,
                    EnterCropsActivity.class);
            enterCropsIntent.putExtra(EMPLOYEE_MOBILE, mobile)
                    .putExtra(EMPLOYEE_PASSWORD, password);
            setResult(RESULT_OK);
            startActivity(enterCropsIntent);
            finish();
        }
    }

    private void loginEmployer(String mobile, String password) {
        EmployerViewModel employerViewModel = new ViewModelProvider(this).get(EmployerViewModel.class);

        Employer[] employer = employerViewModel.getEmployer(mobile);
        if (employer.length == 0)
            layoutInputMobile.setError("Enter registered mobile number!");
        else if (!password.equals(employer[0].getPassword())) {
            inputPassword.setText("");
            layoutInputPassword.setError("Wrong password!");
        }
        else {
            if (checkRememberMe.isChecked()) {
                editor.putString(EMPLOYER_MOBILE, mobile);
                editor.putString(EMPLOYER_PASSWORD, password);
            } else {
                editor.putString(EMPLOYER_MOBILE, NONE);
                editor.putString(EMPLOYER_PASSWORD, NONE);
            }
            editor.apply();

            employerViewModel.loginEmployer(mobile, password, true);

            Intent enterCropsIntent = new Intent(LogInActivity.this,
                    EnterCropsActivity.class);
            enterCropsIntent.putExtra(EMPLOYER_MOBILE, mobile)
                    .putExtra(EMPLOYER_PASSWORD, password);
            setResult(RESULT_OK);
            startActivity(enterCropsIntent);
            finish();
        }
    }
}