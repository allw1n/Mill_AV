package com.m_corp.millav.ui;

import static com.m_corp.millav.utils.MillAVUtils.A4_HEIGHT;
import static com.m_corp.millav.utils.MillAVUtils.A4_WIDTH;
import static com.m_corp.millav.utils.MillAVUtils.BILL_NUMBER;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_ADDRESS;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.CUSTOMER_NAME;
import static com.m_corp.millav.utils.MillAVUtils.EMPLOYER_MOBILE;
import static com.m_corp.millav.utils.MillAVUtils.MANAGE_PERMISSION;
import static com.m_corp.millav.utils.MillAVUtils.NONE;
import static com.m_corp.millav.utils.MillAVUtils.SDK_VERSION;
import static com.m_corp.millav.utils.MillAVUtils.SHARED_PREFS;
import static com.m_corp.millav.utils.MillAVUtils.WRITE_PERMISSION;
import static com.m_corp.millav.utils.MillAVUtils.ZERO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.databinding.ActivityMakeBillBinding;
import com.m_corp.millav.room.Employer;
import com.m_corp.millav.utils.RequestPermissions;
import com.m_corp.millav.viewmodel.EmployerViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MakeBillActivity extends AppCompatActivity {

    private int billNumber;

    private RequestPermissions requestPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMakeBillBinding binding = ActivityMakeBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (SDK_VERSION >= Build.VERSION_CODES.R)
            requestPermissions = new RequestPermissions(this, MANAGE_PERMISSION);
        else
            requestPermissions = new RequestPermissions(this, WRITE_PERMISSION);

        MaterialTextView viewShopName, viewCustomerName, viewCustomerMobile, viewCustomerAddress,
                viewBillDate, viewBillNumber, viewCumulativeAmount;
        RecyclerView recyclerBill;

        viewShopName = binding.viewShopName;
        viewCustomerName = binding.viewCustomerName;
        viewCustomerMobile = binding.viewCustomerMobile;
        viewCustomerAddress = binding.viewCustomerAddress;
        viewBillDate = binding.viewBillDate;
        viewBillNumber = binding.viewBillNumber;
        recyclerBill = binding.recyclerBill;
        viewCumulativeAmount = binding.viewCumulativeAmount;
        ExtendedFloatingActionButton fabPrint = binding.fabPrint;

        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        EmployerViewModel employerViewModel = new ViewModelProvider(this)
                .get(EmployerViewModel.class);
        Employer[] employer = employerViewModel.getEmployer(
                sharedPrefs.getString(EMPLOYER_MOBILE, NONE));

        viewShopName.setText(employer[0].getName());
        viewCustomerName.setText(getIntent().getStringExtra(CUSTOMER_NAME));
        viewCustomerMobile.setText(getIntent().getStringExtra(CUSTOMER_MOBILE));
        viewCustomerAddress.setText(getIntent().getStringExtra(CUSTOMER_ADDRESS));
        viewBillDate.setText(getDate());
        billNumber = sharedPrefs.getInt(BILL_NUMBER, ZERO);
        viewBillNumber.setText(String.valueOf(billNumber));

        recyclerBill.setLayoutManager(new LinearLayoutManager(this));
        MakeBillAdapter makeBillAdapter = new MakeBillAdapter(
                MakeBillActivity.this, billNumber);
        recyclerBill.setAdapter(makeBillAdapter);
        recyclerBill.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        viewCumulativeAmount.setText(String.valueOf(makeBillAdapter.getCumulativeAmount()));

        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("calendar", String.valueOf(calendar));

        return day + "/" + (month + 1) + "/" + year;
    }

    private void generatePDF() {

        if (!requestPermissions.permissionsGranted()) {
            return;
        }

        File documentsDir;

        documentsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);

        File appDir = new File(documentsDir, getString(R.string.app_name));
        Log.d("appDir", appDir.getAbsolutePath());

        boolean dirExists = appDir.exists();
        Log.d("dirExists", String.valueOf(dirExists));
        boolean dirCreated = false;
        if (!dirExists)
            dirCreated = appDir.mkdirs();
        Log.d("dirCreated", String.valueOf(dirCreated));

        String pdfName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String pdfFileName = pdfName + "_" + billNumber + ".pdf";

        File pdfFile = new File(appDir + "/" + pdfFileName);

        PdfDocument pdfDocument = new PdfDocument();

        try {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int dpi = metrics.densityDpi;
            Log.d("DPI", String.valueOf(dpi));

            View content = findViewById(R.id.pdfLayout);

            float pageWidthInPixel = content.getWidth();
            Log.d("pageWidthInPixel", String.valueOf(pageWidthInPixel));
            float pageHeightInPixel = content.getHeight();
            Log.d("pageHeightInPixel", String.valueOf(pageHeightInPixel));

            content.measure(
                    View.MeasureSpec.makeMeasureSpec(
                            (int) pageWidthInPixel, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(
                            (int) pageHeightInPixel, View.MeasureSpec.EXACTLY));

            pageWidthInPixel = content.getMeasuredWidth();
            Log.d("measuredPageWidth", String.valueOf(pageWidthInPixel));
            pageHeightInPixel = content.getMeasuredHeight();
            Log.d("measuredPageHeight", String.valueOf(pageHeightInPixel));

            float ratio = pageWidthInPixel / A4_WIDTH;
            pageHeightInPixel = A4_HEIGHT * ratio;

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                    (int) pageWidthInPixel, (int) pageHeightInPixel, 1).create();

            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            content.layout(0, 0, (int) pageWidthInPixel, (int) pageHeightInPixel);
            content.draw(page.getCanvas());

            content.invalidate();
            content.requestLayout();

            pdfDocument.finishPage(page);

            FileOutputStream fOS = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(fOS);
            fOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }
    }
}

