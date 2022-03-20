package com.example.crimereport.data.repository;

import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import com.example.crimereport.FireStoreListener;
import com.example.crimereport.data.firestore.*;
import com.example.crimereport.models.Report;
import com.example.crimereport.ui.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import okhttp3.ResponseBody;
import retrofit2.Callback;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ReportRepository extends BaseRepository {

    FireStoreListener fireStoreListener;
    public ReportRepository(MapFragment mapFragment){
        fireStoreListener = mapFragment;
    }

    @Override
    public void addReport(final Report report) {

        Uri file = Uri.fromFile(new File((report.getImage())));
        final StorageReference fileReference = storageReference.child("images/"+file.getLastPathSegment());
        final UploadTask uploadTask = fileReference.putFile(file);

        Log.d("list",report.getDescription());
        Log.d("list",report.getLocation());
        Log.d("list",report.getImage());

        fileReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("download",uri.toString());
                        final Map<String,String> reportData = new HashMap<>();
                        reportData.put("image",uri.toString());
                        reportData.put("description",report.getDescription());
                        reportData.put("location",report.getLocation());
                        reportData.put("title",report.getTitle());
                        reportData.put("createAt", String.valueOf(new Timestamp(System.currentTimeMillis())));

                        db.collection("report").add(reportData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("ReportRepository","Success");

                                fireStoreListener.success(true);
                                sendNotificationToUser(reportData);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void deleteReport() {

    }

    @Override
    public void getReports() {

    }

    private void sendNotificationToUser(String token,String title, String body) {
        RootModel rootModel = new RootModel(token, new NotificationModel(title, body), new DataModel("Name", "30"));

        FirebaseServices apiService =  FirebaseApiClient.getClient().create(FirebaseServices.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG","Successfully notification send by using retrofit.");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void sendNotificationToUser(final Map<String,String> reportData){

        db.collection("police").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String[] latlang1 =  document.getString("location").split(",");
                                String[] latlang2 =  reportData.get("location").split(",");

                                sendNotificationToUser(document.getString("token"),reportData.get("title"),reportData.get("body"));


                                if(getDistanceBetweenTwoPoints(Double.valueOf(latlang1[0]),Double.valueOf(latlang1[1]),Double.valueOf(latlang2[0]),Double.valueOf(latlang2[1]))<600){
                                    sendNotificationToUser(document.getString("token"),reportData.get("title"),reportData.get("body"));
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
            }
        });
    }

    private void sendNotificationToPolice(Map<String,String> reportData){

    }

    private float getDistanceBetweenTwoPoints(double lat1,double lon1,double lat2,double lon2) {

        float[] distance = new float[2];

        Location.distanceBetween( lat1, lon1,
                lat2, lon2, distance);

        return distance[0];
    }

}
