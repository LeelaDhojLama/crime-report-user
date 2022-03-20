package com.example.crimereport.data.repository;

import com.example.crimereport.models.Report;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public abstract void addReport(Report report);
    public abstract void deleteReport();
    public abstract void getReports();

}
