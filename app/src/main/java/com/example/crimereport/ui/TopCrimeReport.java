package com.example.crimereport.ui;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crimereport.R;
import com.example.crimereport.adapter.TopCrimeReportAdapter;
import com.example.crimereport.models.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopCrimeReport extends BaseFragment {

    RecyclerView recyclerView;
    TopCrimeReportAdapter topCrimeReportAdapter;
    List<Report> reports = new ArrayList<>();
    Geocoder geocoder;
    List<Address> addresses;
    List<String> locations;
    Map<String, String> highestCrime;
    Map<String,Integer> highestCrimeCount;
    ProgressBar mProgressbar;

    public static TopCrimeReport getInstance(){

        TopCrimeReport topCrimeReport = new TopCrimeReport();
        return topCrimeReport;
    }

    public TopCrimeReport() {
        // Required empty public constructor
    }

    @Override
    protected View onViewReady(View view, Bundle savedInstanceState) {

//        getActivity().getActionBar().setTitle("Top Crime List");
        recyclerView = view.findViewById(R.id.report_list);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        highestCrime = new HashMap<>();
        highestCrimeCount = new HashMap<>();
        mProgressbar = view.findViewById(R.id.progress_bar);
        mProgressbar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("report").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                locations = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String[] latlng =  document.getString("location").split(",");
                        Log.d("latlng",latlng[1]);
                        try {
                            addresses = geocoder.getFromLocation(Double.valueOf(latlng[0]), Double.valueOf(latlng[1]), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            Log.d("address",addresses.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        locations.add(city);


                    }
                    Set<String> filterLocation = new HashSet<String>(locations);
                    for(String fLocation:filterLocation){
                        for(String location:locations){
                            if(fLocation.equals(location)){
                                if(highestCrime.get(fLocation)==(null)){
                                    highestCrime.put(fLocation,fLocation);
                                    highestCrimeCount.put(fLocation,1);
                                }else{
                                    int count = highestCrimeCount.get(fLocation);
                                    highestCrimeCount.put(fLocation,count+1);
                                }

                            }
                        }
                    }

                    for (String location:filterLocation){
                        Report report = new Report();
                        report.setLocation(location);
                        report.setReportCount((highestCrimeCount.get(location)));
                        reports.add(report);
                    }
                    Collections.sort(reports, new Comparator<Report>() {
                        @Override
                        public int compare(Report p1, Report p2) {
                            return p2.getReportCount() - p1.getReportCount();
                        }
                    });
                    topCrimeReportAdapter = new TopCrimeReportAdapter(reports);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(topCrimeReportAdapter);
                    mProgressbar.setVisibility(View.GONE);
                    Log.d("filterdata",highestCrimeCount.toString());
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });




        return view;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_top_crime_report;
    }

}
