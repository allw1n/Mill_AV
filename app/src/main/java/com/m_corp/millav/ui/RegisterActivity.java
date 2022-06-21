package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE;
import static com.m_corp.millav.utils.MillAVUtils.LOG_IN_TYPE;
import static com.m_corp.millav.utils.MillAVUtils.NONE;
import static com.m_corp.millav.utils.MillAVUtils.REQUIRED;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.databinding.ActivityRegisterBinding;
import com.m_corp.millav.room.Employee;
import com.m_corp.millav.room.Employer;
import com.m_corp.millav.viewmodel.EmployeeViewModel;
import com.m_corp.millav.viewmodel.EmployerViewModel;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private String loginType;

    private TextInputLayout layoutInputNewMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextInputLayout layoutInputNewName, layoutInputEmployerMobile,
                layoutInputSetPassword, layoutInputConfirmSetPassword;
        TextInputEditText inputNewMobile, inputNewName, inputEmployerMobile, inputSetPassword,
                inputConfirmSetPassword;
        MaterialButton buttonNewRegister;

        layoutInputNewMobile = binding.layoutInputNewMobile;
        layoutInputNewName = binding.layoutInputNewName;
        layoutInputEmployerMobile = binding.layoutInputEmployerMobile;
        layoutInputSetPassword = binding.layoutInputSetPassword;
        layoutInputConfirmSetPassword = binding.layoutInputConfirmSetPassword;

        inputNewMobile = binding.inputNewMobile;
        inputNewName = binding.inputNewName;
        inputEmployerMobile = binding.inputEmployerMobile;
        inputSetPassword = binding.inputSetPassword;
        inputConfirmSetPassword = binding.inputConfirmSetPassword;

        buttonNewRegister = binding.buttonNewRegister;

        loginType = getIntent().getStringExtra(LOG_IN_TYPE);
        if (loginType.equals(EMPLOYEE)) layoutInputEmployerMobile.setVisibility(View.VISIBLE);

        inputNewMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputNewMobile.isErrorEnabled())
                    layoutInputNewMobile.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputNewName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputNewName.isErrorEnabled())
                    layoutInputNewName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (loginType.equals(EMPLOYEE)) {
            inputEmployerMobile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (layoutInputEmployerMobile.isErrorEnabled())
                        layoutInputEmployerMobile.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        inputSetPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputSetPassword.isErrorEnabled())
                    layoutInputSetPassword.setErrorEnabled(false);
                if (layoutInputConfirmSetPassword.isErrorEnabled())
                    layoutInputConfirmSetPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputConfirmSetPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputSetPassword.isErrorEnabled())
                    layoutInputSetPassword.setErrorEnabled(false);
                if (layoutInputConfirmSetPassword.isErrorEnabled())
                    layoutInputConfirmSetPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonNewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile, name, employerMobile = NONE, password, confirmPassword;

                mobile = Objects.requireNonNull(inputNewMobile.getText()).toString();
                name = Objects.requireNonNull(inputNewName.getText()).toString();
                if (loginType.equals(EMPLOYEE)) {
                    employerMobile = Objects.requireNonNull(inputEmployerMobile.getText()).toString();
                }
                password = Objects.requireNonNull(inputSetPassword.getText()).toString();
                confirmPassword = Objects.requireNonNull(inputConfirmSetPassword.getText()).toString();

                if (TextUtils.isEmpty(mobile)) {
                    layoutInputNewMobile.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    layoutInputNewName.setError(REQUIRED);
                    return;
                }
                if (loginType.equals(EMPLOYEE)) {
                    if (TextUtils.isEmpty(employerMobile)) {
                        layoutInputNewName.setError(REQUIRED);
                        return;
                    }
                }
                if (TextUtils.isEmpty(password)) {
                    layoutInputSetPassword.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    layoutInputConfirmSetPassword.setError(REQUIRED);
                    return;
                }

                if (loginType.equals(EMPLOYEE)){
                    EmployerViewModel employerViewModel = new ViewModelProvider(
                            RegisterActivity.this).get(EmployerViewModel.class);
                    Employer[] checkEmployer = employerViewModel.getEmployer(employerMobile);
                    if (checkEmployer.length == 0) {
                        layoutInputEmployerMobile.setError("Enter valid employer number!");
                        return;
                    }
                }

                if (password.equals(confirmPassword)) {
                    if (loginType.equals(EMPLOYEE))
                        registerEmployee(mobile, name, employerMobile, confirmPassword);
                    else
                        registerEmployer(mobile, name, confirmPassword);
                } else {
                    layoutInputSetPassword.setError("Password do not match!");
                    layoutInputConfirmSetPassword.setError("Password do not match!");
                }
            }
        });
    }

    private void registerEmployee(String mobile, String name, String employerMobile,
                                  String confirmPassword) {

        EmployeeViewModel employeeViewModel = new ViewModelProvider(
                RegisterActivity.this).get(EmployeeViewModel.class);

        Employee[] checkEmployee = employeeViewModel.getEmployee(mobile);

        if (checkEmployee.length == 0) {
            Employee newEmployee = new Employee(mobile, name, employerMobile, confirmPassword,
                    false);
            employeeViewModel.insertEmployee(newEmployee);
            Toast.makeText(
                    RegisterActivity.this,
                    "Registered successfully!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            layoutInputNewMobile.setError("Already registered! Use a different number.");
        }
    }

    private void registerEmployer(String mobile, String name, String confirmPassword) {

        EmployerViewModel employerViewModel = new ViewModelProvider(
                RegisterActivity.this).get(EmployerViewModel.class);

        Employer[] checkEmployer = employerViewModel.getEmployer(mobile);

        if (checkEmployer.length == 0) {
            Employer newEmployer = new Employer(mobile, name, confirmPassword,
                    false);
            employerViewModel.insertEmployer(newEmployer);
            Toast.makeText(
                    RegisterActivity.this,
                    "Registered successfully!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            layoutInputNewMobile.setError("Already registered! Use a different number.");
        }
    }
}