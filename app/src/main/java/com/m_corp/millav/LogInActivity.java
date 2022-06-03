package com.m_corp.millav;

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
import com.m_corp.millav.databinding.ActivityLogInBinding;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private static final String LOGIN_ID = "login_id";
    private static final String PASSWORD = "password";
    private static final String NONE = "none";
    private static final String SHARED_PREFS = "shared_prefs";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    private String savedLoginID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPrefs.edit();

        TextInputLayout layoutInputLoginID, layoutInputPassword;
        TextInputEditText inputLoginID, inputPassword;
        MaterialCheckBox checkRememberMe;
        MaterialButton buttonLogin, buttonRegister, buttonForgot;

        layoutInputLoginID = binding.layoutInputLoginID;
        layoutInputPassword = binding.layoutInputPassword;
        inputLoginID = binding.inputLoginID;
        inputPassword = binding.inputPassword;

        checkRememberMe = binding.checkRememberMe;

        buttonLogin = binding.buttonLogin;
        buttonRegister = binding.buttonRegister;
        buttonForgot = binding.buttonForgot;

        savedLoginID = sharedPrefs.getString(LOGIN_ID, NONE);
        if (!savedLoginID.equals(NONE)) {
            inputLoginID.setText(savedLoginID);
            inputPassword.setText(sharedPrefs.getString(PASSWORD, ""));
        }

        inputLoginID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputLoginID.isErrorEnabled()) layoutInputLoginID.setError("");
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
                loginID = Objects.requireNonNull(inputLoginID.getText()).toString().toLowerCase();
                password = Objects.requireNonNull(inputPassword.getText()).toString();

                if (TextUtils.isEmpty(loginID)) {
                    layoutInputLoginID.setError(getString(R.string.required));
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
                            editor.putString(LOGIN_ID, loginID);
                            editor.putString(PASSWORD, password);
                            editor.apply();
                        } else {
                            editor.putString(LOGIN_ID, NONE);
                            editor.putString(PASSWORD, NONE);
                            editor.apply();
                        }
                    }
                });

                Intent enterCropsIntent = new Intent(LogInActivity.this, EnterCropsActivity.class);
            }
        });
    }
}