package com.example.crimereport.ui;


import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crimereport.FireStoreListener;
import com.example.crimereport.R;
import com.example.crimereport.data.repository.ReportRepository;
import com.example.crimereport.models.Report;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements FireStoreListener {


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };
    Button mSubmitButton ;
    ReportRepository reportRepository;
    ProgressBar mProgressBar;
    public MapFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Report report) {
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("report",report);
        mapFragment.setArguments(bundle);
        return  mapFragment;
    }


    @Override
    protected View onViewReady(View view, Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION},1);

        GPSTracker gps = new GPSTracker(getContext());
        final double[] latitude = {gps.getLatitude()};
        final double[] longitude = {gps.getLongitude()};
        reportRepository = new ReportRepository(this);

        mSubmitButton = view.findViewById(R.id.submit_report);
        mProgressBar = view.findViewById(R.id.progress_bar);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report = (Report) getArguments().getSerializable("report");
                report.setLocation(String.valueOf(latitude[0])+","+String.valueOf(longitude[0]));
                Toast.makeText(getContext(),"Report Submitting...",Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.VISIBLE);
                reportRepository.addReport(report);
            }
        });

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(latitude[0], longitude[0]))
                        .zoom(17)
                        .bearing(0)
                        .tilt(0)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude[0], longitude[0]))
                        .title("Your Current Location"));
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        latitude[0] = latLng.latitude;
                        longitude[0] = latLng.longitude;
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude,latLng.longitude)));
                    }
                });
//                        .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.spider)));

//                mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(37.4629101,-122.2449094))
//                        .title("Iron Man")
//                        .snippet("His Talent : Plenty of money"));
//
//                mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(37.3092293,-122.1136845))
//                        .title("Captain America"));
            }
        });

        return view;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_map;
    }

    @Override
    public void success(boolean status) {
        if(status){
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Successfully Report Submitted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finisehed(boolean status) {

    }
}
