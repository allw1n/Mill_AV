package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.EMPLOYEE;
import static com.m_corp.millav.utils.MillAVUtils.LOG_IN_TYPE;
import static com.m_corp.millav.utils.MillAVUtils.NONE;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Employee;
import com.m_corp.millav.room.Employer;
import com.m_corp.millav.viewmodel.EmployeeViewModel;
import com.m_corp.millav.viewmodel.EmployerViewModel;

import java.util.Objects;

public class ForgotPasswordFragment extends BottomSheetDialogFragment {

    public Application application;

    private TextInputLayout layoutInputForgotMobile, layoutInputNewPassword, layoutInputConfirmPassword;
    private TextInputEditText inputForgotMobile, inputNewPassword, inputConfirmPassword;

    private EmployeeViewModel employeeViewModel;
    private EmployerViewModel employerViewModel;

    private SharedPreferences sharedPrefs;

    public String loginType;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_forgot_password, container, false);

        layoutInputForgotMobile = rootView.findViewById(R.id.layoutInputForgotMobile);
        layoutInputNewPassword = rootView.findViewById(R.id.layoutInputNewPassword);
        layoutInputConfirmPassword = rootView.findViewById(R.id.layoutInputConfirmPassword);

        inputForgotMobile = rootView.findViewById(R.id.inputForgotMobile);
        inputNewPassword = rootView.findViewById(R.id.inputNewPassword);
        inputConfirmPassword = rootView.findViewById(R.id.inputConfirmPassword);

        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        employerViewModel = new ViewModelProvider(this).get(EmployerViewModel.class);

        inputForgotMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputForgotMobile.isErrorEnabled())
                    layoutInputForgotMobile.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputNewPassword.isErrorEnabled())
                    layoutInputNewPassword.setErrorEnabled(false);
                if (layoutInputConfirmPassword.isErrorEnabled())
                    layoutInputConfirmPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputNewPassword.isErrorEnabled())
                    layoutInputNewPassword.setErrorEnabled(false);
                if (layoutInputConfirmPassword.isErrorEnabled())
                    layoutInputConfirmPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        MaterialButton buttonResetPassword = rootView.findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMobile, newPassword, confirmPassword;

                inputMobile = Objects.requireNonNull(inputForgotMobile.getText()).toString();
                newPassword = Objects.requireNonNull(inputNewPassword.getText()).toString();
                confirmPassword = Objects.requireNonNull(inputConfirmPassword.getText()).toString();

                if (TextUtils.isEmpty(inputMobile)) {
                    layoutInputForgotMobile.setError("Required!");
                    return;
                }

                sharedPrefs = requireActivity()
                        .getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                loginType = sharedPrefs.getString(LOG_IN_TYPE, NONE);
                if (loginType.equals(EMPLOYEE)) {
                    Employee[] employee = employeeViewModel.getEmployee(inputMobile);
                    if (employee.length == 0) {
                        layoutInputForgotMobile.setError("Enter registered mobile number!");
                        return;
                    }
                } else {
                    Employer[] employer = employerViewModel.getEmployer(inputMobile);
                    if (employer.length == 0) {
                        layoutInputForgotMobile.setError("Enter registered mobile number!");
                        return;
                    }
                }

                if (TextUtils.isEmpty(newPassword)) {
                    layoutInputNewPassword.setError("Required!");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    layoutInputConfirmPassword.setError("Required!");
                    return;
                }
                if (newPassword.equals(confirmPassword)) {
                    if (loginType.equals(EMPLOYEE))
                        employeeViewModel.changePassword(inputMobile, confirmPassword);
                    else
                        employerViewModel.changePassword(inputMobile, confirmPassword);

                    Toast.makeText(application, "Password changed successfully!",
                            Toast.LENGTH_SHORT).show();

                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .remove(ForgotPasswordFragment.this)
                            .commit();
                }
                else {
                    layoutInputNewPassword.setError("Passwords do not match!");
                    layoutInputConfirmPassword.setError("Passwords do not match!");
                }
            }
        });

        return rootView;
    }
}