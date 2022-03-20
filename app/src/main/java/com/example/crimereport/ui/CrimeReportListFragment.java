package com.example.crimereport.ui;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crimereport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeReportListFragment extends BaseFragment {


    public CrimeReportListFragment() {
        // Required empty public constructor
    }

    @Override
    protected View onViewReady(View view, Bundle savedInstanceState) {
        return view;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_crime_report_list;
    }

}
