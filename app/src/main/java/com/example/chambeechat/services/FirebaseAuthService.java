package com.example.chambeechat.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chambeechat.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseAuthService {

    public interface OnEmailAvailableCheck {
        void onComplete(boolean isEmailAvailable);
    }

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void isEmailAvailable(String email, final OnEmailAvailableCheck onEmailAvailableCheck, final OnFailureListener onFailureListener) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
        .addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
            @Override
            public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {
                List<String> methods = signInMethodQueryResult.getSignInMethods();

                if (methods == null) {
                    onFailureListener.onFailure(new Exception("Could not retrieve sign in methods"));
                }
                else {
                    Log.e("chambee", "Sign in methods: " + methods.toString());
                    onEmailAvailableCheck.onComplete(methods.size() == 0);
                }
            }
        })
        .addOnFailureListener(onFailureListener);
    }

    public void signInWithEmail(String email, String password, OnSuccessListener<AuthResult> onSuccessListener, OnFailureListener onFailureListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener);
    }

    public void signUpWithEmail(final String email, final String password, final OnSuccessListener<Void> onSuccessListener, final OnFailureListener onFailureListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String uid = authResult.getUser().getUid();
                Map<String, Object> userData = new HashMap<>();
                userData.put("uid", uid);
                userData.put("email", email);
                userData.put("password", password);

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("users")
                .document(uid)
                .set(userData)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
            }
        })
        .addOnFailureListener(onFailureListener);
    }
}
