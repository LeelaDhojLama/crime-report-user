package com.example.crimereport.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crimereport.R;
import com.example.crimereport.data.repository.ReportRepository;
import com.example.crimereport.models.Report;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageAddFragment extends BaseFragment {

    private ImageView imageView;
    private EditText mDescriptionEditText,mTitleEditText;
    private Button mButton;
    private ReportRepository reportRepository;

    public ImageAddFragment() {
        // Required empty public constructor
    }

    public static ImageAddFragment newInstance(String imagePath) {
        ImageAddFragment imageAddFragment = new ImageAddFragment();

        Bundle bundle = new Bundle();
        bundle.putString("imagePath",imagePath);
        imageAddFragment.setArguments(bundle);

        return imageAddFragment;
    }


    @Override
    protected View onViewReady(View view, Bundle savedInstanceState) {

//        File imgFile = new  File(getArguments().getString("imagePath"));

        Bitmap image = BitmapFactory.decodeFile(getArguments().getString("imagePath"));

        imageView = view.findViewById(R.id.imgPreivew);
        imageView.setImageBitmap(image);

        mButton = view.findViewById(R.id.btnAddLocation);
        mDescriptionEditText = view.findViewById(R.id.detailsTxt);
        mTitleEditText = view.findViewById(R.id.titleText);
        final Report report = new Report();


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()) {
                    report.setImage(getArguments().getString("imagePath"));
                    report.setDescription(mDescriptionEditText.getText().toString());
                    report.setTitle(mTitleEditText.getText().toString());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.root,MapFragment.newInstance(report),"map").addToBackStack("Map").commit();
                }else{
                    Toast.makeText(getContext(),"Please Fill all fields",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private  boolean isValid(){
        if(TextUtils.isEmpty(mTitleEditText.getText())){
            mTitleEditText.setError("Title is required");
            return false;
        }
        if(TextUtils.isEmpty(mDescriptionEditText.getText())){
            mDescriptionEditText.setError("Title is required");
            return false;
        }
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_image_add;
    }

}
