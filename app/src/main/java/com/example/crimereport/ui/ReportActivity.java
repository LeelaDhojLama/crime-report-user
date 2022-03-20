package com.example.crimereport.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;
import com.example.crimereport.R;

public class ReportActivity extends BaseActivity {

    Toolbar toolbar;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        Log.d("test","test");
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Highest Crime List");
        getSupportFragmentManager().beginTransaction().replace(R.id.root,TopCrimeReport.getInstance()).commit();
    }

    @Override
    int getContentView() {
        return R.layout.activity_report;
    }
}
