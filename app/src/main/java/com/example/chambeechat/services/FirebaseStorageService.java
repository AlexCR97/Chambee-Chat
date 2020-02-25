package com.example.chambeechat.services;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageService {

    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public void downloadImage(String path, OnSuccessListener<Uri> onSuccessListener, OnFailureListener onFailureListener) {
        firebaseStorage.getReference()
                .child(path)
                .getDownloadUrl()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void uploadImage(String path, Uri image, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        firebaseStorage.getReference()
                .child(path)
                .putFile(image)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}

