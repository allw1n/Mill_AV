package com.m_corp.millav.ui;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m_corp.millav.R;
import com.m_corp.millav.databinding.ActivityRegisterBinding;
import com.m_corp.millav.room.User;
import com.m_corp.millav.viewmodel.UserViewModel;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextInputLayout layoutInputNewMobile, layoutInputNewName, layoutInputSetPassword,
                layoutInputConfirmSetPassword;
        TextInputEditText inputNewMobile, inputNewName, inputSetPassword, inputConfirmSetPassword;
        MaterialButton buttonNewRegister;

        layoutInputNewMobile = binding.layoutInputNewMobile;
        layoutInputNewName = binding.layoutInputNewName;
        layoutInputSetPassword = binding.layoutInputSetPassword;
        layoutInputConfirmSetPassword = binding.layoutInputConfirmSetPassword;

        inputNewMobile = binding.inputNewMobile;
        inputNewName = binding.inputNewName;
        inputSetPassword = binding.inputSetPassword;
        inputConfirmSetPassword = binding.inputConfirmSetPassword;

        buttonNewRegister = binding.buttonNewRegister;

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
                String mobile, name, password, confirmPassword;
                mobile = Objects.requireNonNull(inputNewMobile.getText()).toString();
                name = Objects.requireNonNull(inputNewName.getText()).toString();
                password = Objects.requireNonNull(inputSetPassword.getText()).toString();
                confirmPassword = Objects.requireNonNull(inputConfirmSetPassword.getText()).toString();

                if (TextUtils.isEmpty(mobile)) {
                    layoutInputNewMobile.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    layoutInputNewName.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    layoutInputSetPassword.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    layoutInputConfirmSetPassword.setError("Required");
                    return;
                }
                if (password.equals(confirmPassword)) {
                    User[] checkUser = userViewModel.getUser(mobile);
                    if (checkUser.length == 0) {
                        User newUser = new User(mobile, name, confirmPassword, false);
                        userViewModel.insertUser(newUser);
                        Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        layoutInputNewMobile.setError("Already registered! Use a different number.");
                    }
                } else {
                    layoutInputSetPassword.setError("Password do not match!");
                    layoutInputConfirmSetPassword.setError("Password do not match!");
                }
            }
        });
    }
}