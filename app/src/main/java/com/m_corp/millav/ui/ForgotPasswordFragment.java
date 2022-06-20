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
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Employee;
import com.m_corp.millav.viewmodel.EmployeeViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends BottomSheetDialogFragment {

    public Application application;

    private EmployeeViewModel employeeViewModel;

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
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        TextInputLayout layoutInputForgotMobile, layoutInputNewPassword, layoutInputConfirmPassword;
        TextInputEditText inputForgotMobile, inputNewPassword, inputConfirmPassword;
        MaterialButton buttonResetPassword;

        layoutInputForgotMobile = rootView.findViewById(R.id.layoutInputForgotMobile);
        layoutInputNewPassword = rootView.findViewById(R.id.layoutInputNewPassword);
        layoutInputConfirmPassword = rootView.findViewById(R.id.layoutInputConfirmPassword);

        inputForgotMobile = rootView.findViewById(R.id.inputForgotMobile);
        inputNewPassword = rootView.findViewById(R.id.inputNewPassword);
        inputConfirmPassword = rootView.findViewById(R.id.inputConfirmPassword);

        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

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

        buttonResetPassword = rootView.findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword, confirmPassword, inputMobile;

                inputMobile = Objects.requireNonNull(inputForgotMobile.getText()).toString();
                newPassword = Objects.requireNonNull(inputNewPassword.getText()).toString();
                confirmPassword = Objects.requireNonNull(inputConfirmPassword.getText()).toString();

                if (TextUtils.isEmpty(inputMobile)) {
                    layoutInputForgotMobile.setError("Required!");
                    return;
                }

                Employee[] employee = employeeViewModel.getUser(inputMobile);
                if (employee.length == 0) {
                    layoutInputForgotMobile.setError("Enter registered mobile number!");
                    return;
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
                    employeeViewModel.changePassword(inputMobile, confirmPassword);
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