package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.BILL_NUMBER;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_ADDRESS;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_NAME;
import static com.m_corp.millav.utils.MillAVUtils.REQUIRED;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class CustomerInfoFragment extends BottomSheetDialogFragment {

    public Application application;

    public CustomerInfoFragment() {
        // Required empty public constructor
    }

    public static CustomerInfoFragment newInstance() {
        return new CustomerInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customer_info, container,
                false);

        TextInputLayout layoutInputCustomerName, layoutInputCustomerMobile,
                layoutInputCustomerAddress;

        TextInputEditText inputCustomerName, inputCustomerMobile, inputCustomerAddress;

        MaterialButton buttonConfirm;

        layoutInputCustomerName = rootView.findViewById(R.id.layoutInputCustomerName);
        layoutInputCustomerMobile = rootView.findViewById(R.id.layoutInputCustomerMobile);
        layoutInputCustomerAddress = rootView.findViewById(R.id.layoutInputCustomerAddress);
        inputCustomerName = rootView.findViewById(R.id.inputCustomerName);
        inputCustomerMobile = rootView.findViewById(R.id.inputCustomerMobile);
        inputCustomerAddress = rootView.findViewById(R.id.inputCustomerAddress);
        buttonConfirm = rootView.findViewById(R.id.buttonConfirm);

        inputCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputCustomerName.isErrorEnabled())
                    layoutInputCustomerName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputCustomerMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputCustomerMobile.isErrorEnabled())
                    layoutInputCustomerMobile.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputCustomerAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutInputCustomerAddress.isErrorEnabled())
                    layoutInputCustomerAddress.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, mobile, address;

                name = Objects.requireNonNull(inputCustomerName.getText()).toString();
                mobile = Objects.requireNonNull(inputCustomerMobile.getText()).toString();
                address = Objects.requireNonNull(inputCustomerAddress.getText()).toString();

                if (TextUtils.isEmpty(name)) {
                    layoutInputCustomerName.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    layoutInputCustomerMobile.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    layoutInputCustomerAddress.setError(REQUIRED);
                    return;
                }

                Intent makeBillIntent = new Intent(requireActivity(), MakeBillActivity.class);
                makeBillIntent.putExtra(CUSTOMER_NAME, name).putExtra(CUSTOMER_MOBILE, mobile)
                        .putExtra(CUSTOMER_ADDRESS, address);

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .remove(CustomerInfoFragment.this)
                        .commit();

                startActivity(makeBillIntent);
            }
        });

        return rootView;
    }
}