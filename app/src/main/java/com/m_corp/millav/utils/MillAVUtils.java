package com.m_corp.millav.utils;

import android.Manifest;
import android.os.Build;

public class MillAVUtils {

    public static final String LOG_IN_TYPE = "log_in_type";
    public static final String EMPLOYEE_LOG_IN_SAVED = "employee_log_in_saved";
    public static final String EMPLOYER_LOG_IN_SAVED = "employer_log_in_saved";

    public static final String EMPLOYER = "employer";
    public static final String EMPLOYER_MOBILE = "employer_mobile";
    public static final String EMPLOYER_PASSWORD = "employer_password";

    public static final String EMPLOYEE = "employee";
    public static final String EMPLOYEE_MOBILE = "employee_mobile";
    public static final String EMPLOYEE_PASSWORD = "employee_password";

    public static final String NONE = "none";
    public static final String REQUIRED = "Required";
    public static final String SHARED_PREFS = "shared_prefs";

    public static final String FORGOT_PASSWORD_TAG = "ForgotPasswordFragment";
    public static final String ADD_NEW_CROP_TAG = "AddNewCropFragment";
    public static final String CUSTOMER_INFO_TAG = "CustomerInfoFragment";
    public static final String CHANGE_PRICE_TAG = "ChangePriceFragment";

    public static final String BILL_NUMBER = "bill_number";
    public static final int ZERO = 0;

    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_MOBILE = "customer_mobile";
    public static final String CUSTOMER_ADDRESS = "customer_address";

    public static final int A4_WIDTH = 595;
    public static final int A4_HEIGHT = 842;

    public static final String WRITE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String MANAGE_PERMISSION = "android.permission.MANAGE_EXTERNAL_STORAGE";
    public static final int SDK_VERSION = Build.VERSION.SDK_INT;
    public static final String DENIED = "denied";
}
